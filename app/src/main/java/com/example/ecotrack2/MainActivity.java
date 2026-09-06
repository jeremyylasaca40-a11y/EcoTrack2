package com.example.ecotrack2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar, btnReportes, btnEstadisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnReportes = findViewById(R.id.btnReportes);
        btnEstadisticas = findViewById(R.id.btnEstadisticas);

        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        btnReportes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportesActivity.class);
            startActivity(intent);
        });

        btnEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EstadisticasActivity.class);
            startActivity(intent);
        });
    }
}