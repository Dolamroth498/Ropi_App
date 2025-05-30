package com.example.ropiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;
    EditText nameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordConfirmET;
    EditText phoneET;
    EditText addressET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        int secret_key = bundle.getInt("SECRET_KEY");

        if(secret_key != 99){
            finish();
        }

        nameET = findViewById(R.id.name);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        passwordConfirmET = findViewById(R.id.passwordAgain);
        phoneET = findViewById(R.id.mobil);
        addressET = findViewById(R.id.address);


        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userEmail = preferences.getString("emailCim", "");
        String userPassword = preferences.getString("password", "");

        emailET.setText(userEmail);
        passwordET.setText(userPassword);
        passwordConfirmET.setText(userPassword);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();


        Log.i(LOG_TAG, "onCreate");
    }

    public void register(View view) {
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passwordConfirmET.getText().toString();
        String address = addressET.getText().toString();
        String phone = phoneET.getText().toString();

        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(this, "Kérlek, töltsd ki az összes mezőt!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(passwordConfirm)){
            Log.e(LOG_TAG, "Nem egyeszik a két jelszó!");
            return;
        }

        Log.i(LOG_TAG,"Regisztrált: " + name + " Email: " + email);
        //startLeague();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "Felhasználó sikeresen elkészült!");

                    FirebaseUser registeredUser = task.getResult().getUser();
                    if (registeredUser != null) {
                        String uid = registeredUser.getUid();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", name.isEmpty() ? "nincs megadva" : name);
                        userData.put("email", email);
                        userData.put("address", address.isEmpty() ? "nincs megadva" : address);
                        userData.put("phone", phone.isEmpty() ? "nincs megadva" : phone);

                        db.collection("users").document(uid).set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(LOG_TAG, "Felhasználói adatok elmentve.");
                                    startLeague();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(LOG_TAG, "Hiba a Firestore mentés közben: " + e.getMessage());
                                    Toast.makeText(RegisterActivity.this, "Hiba az adatok mentésekor", Toast.LENGTH_SHORT).show();
                                });
                    }

                } else {
                    Log.d(LOG_TAG, "Felhasználó nem készült el!");
                    Toast.makeText(RegisterActivity.this, "Felhasználó nem készült el: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    private void startLeague(){
        Log.d(LOG_TAG, "Indítjuk a BajnoksagActivity-t");
        Intent intent = new Intent(this, BajnoksagActivity.class);
        intent.putExtra("league_id", "extraliga");
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG,"onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG,"onRestart");
    }
}