package com.example.ropiapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GamesActivity extends AppCompatActivity {

    private static final String LOG_TAG = GamesActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_games);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.games_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.leageButton) {
            Intent intent = new Intent(this, BajnoksagActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        } else if (id == R.id.profileButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null && user.isAnonymous()) {
                showGuestDialog();
            } else {
                // Vendég NEM, tehát mehet profilra
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            return true;
        } else if (id == R.id.logoutButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null && user.isAnonymous()) {
                user.delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("LOGOUT", "Vendégfelhasználó törölve.");
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                finish();
                            } else {
                                Log.e("LOGOUT", "Hiba történt a vendég törléskor.", task.getException());
                                // Ilyenkor is célszerű kijelentkeztetni
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                finish();
                            }
                        });
            } else {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

    private void showGuestDialog() {
        FirebaseUser guestUser = FirebaseAuth.getInstance().getCurrentUser();

        new AlertDialog.Builder(this)
                .setTitle("Bejelentkezés szükséges")
                .setMessage("A profil eléréséhez be kell jelentkezned vagy regisztrálnod.")
                .setPositiveButton("Bejelentkezés", (dialog, which) -> {
                    if (guestUser != null && guestUser.isAnonymous()) {
                        guestUser.delete().addOnCompleteListener(task -> {
                            FirebaseAuth.getInstance().signOut(); // Biztos ami biztos
                            Intent intent = new Intent(this, MainActivity.class);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        Intent intent = new Intent(this, MainActivity.class);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Regisztráció", (dialog, which) -> {
                    if (guestUser != null && guestUser.isAnonymous()) {
                        guestUser.delete().addOnCompleteListener(task -> {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(this, RegisterActivity.class);
                            intent.putExtra("SECRET_KEY", 99);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            finish();
                        });
                    } else {
                        Intent intent = new Intent(this, RegisterActivity.class);
                        intent.putExtra("SECRET_KEY", 99);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                })
                .setNeutralButton("Mégse", null)
                .show();
    }
}
