package com.example.ropiapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getName();
    private FirebaseUser user;
    private FirebaseFirestore db;
    private TextView emailTV;
    private EditText nameET, phoneET, addressET;
    private Button editButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission("android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        nameET = findViewById(R.id.nameEditText);
        emailTV = findViewById(R.id.emailTextView);
        phoneET = findViewById(R.id.phoneEditText);
        addressET = findViewById(R.id.addressEditText);
        editButton = findViewById(R.id.editButton);
        saveButton = findViewById(R.id.saveButton);


        if (user != null && !user.isAnonymous()) {
            loadUserData();
        } else {
            Toast.makeText(this, "Nem bejelentkezett felhasználó.", Toast.LENGTH_SHORT).show();
            finish(); // vagy átirányítás loginra
        }

        saveButton.setOnClickListener(v -> {
            // Animáció indítása
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_scale);
            v.startAnimation(anim);

            // Adatok lekérése
            String newName = nameET.getText().toString().trim();
            String newPhone = phoneET.getText().toString().trim();
            String newAddress = addressET.getText().toString().trim();

            // Adatok mentése Firestore-ba
            db.collection("users").document(user.getUid())
                    .update("name", newName,
                            "phone", newPhone,
                            "address", newAddress)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Adatok mentve.", Toast.LENGTH_SHORT).show();

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "mentes_channel")
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("Mentés sikeres")
                                .setContentText("A profil adataid elmentve.")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.notify(100, builder.build());



                        // Beviteli mezők lezárása
                        nameET.setEnabled(false);
                        phoneET.setEnabled(false);
                        addressET.setEnabled(false);

                        // Gombok cseréje
                        editButton.setVisibility(View.VISIBLE);
                        saveButton.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba történt: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        editButton.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_scale);
            v.startAnimation(anim);

            nameET.setEnabled(true);
            phoneET.setEnabled(true);
            addressET.setEnabled(true);
            editButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
        });

        Button alarmButton = findViewById(R.id.alarmButton);
        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    Toast.makeText(this, "A pontos ébresztések nincsenek engedélyezve.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            try {
                long triggerTime = System.currentTimeMillis() + 60 * 60 * 1000; // 10 mp múlva
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                Toast.makeText(this, "Értesítés 10 másodperc múlva...", Toast.LENGTH_SHORT).show();
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this, "Nem sikerült beállítani az ébresztőt: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        Button deleteAccountButton = findViewById(R.id.deleteAccountButton);
        deleteAccountButton.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_scale);
            v.startAnimation(anim);

            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Biztosan törlöd a fiókod?")
                    .setMessage("Ez a művelet nem visszavonható.")
                    .setPositiveButton("Igen", (dialog, which) -> deleteAccount())
                    .setNegativeButton("Mégse", null)
                    .show();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "mentes_channel",
                    "Mentési Értesítések",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profil_menu, menu);
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
        } else if (id == R.id.gamesButton) {
            Intent intent = new Intent(this, GamesActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        } else if (id == R.id.logoutButton) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
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

    private void loadUserData() {
        String uid = user.getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        nameET.setText(document.getString("name"));
                        emailTV.setText("Email: " + document.getString("email"));
                        phoneET.setText(document.getString("phone"));
                        addressET.setText(document.getString("address"));
                    } else {
                        Toast.makeText(this, "Nincs adat a felhasználóról.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba történt az adatok betöltésekor.", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "Firestore hiba: " + e.getMessage());
                });
    }

    private void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (user != null) {
            String uid = user.getUid();

            db.collection("users").document(uid).delete()
                    .addOnSuccessListener(aVoid -> {
                        user.delete()
                                .addOnSuccessListener(a -> {
                                    Toast.makeText(this, "Fiók törölve.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Nem sikerült törölni a felhasználót: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Nem sikerült törölni az adatokat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Értesítési engedély megadva", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Értesítési engedély megtagadva – nem fogsz kapni értesítéseket.", Toast.LENGTH_LONG).show();
            }

        }
    }

}
