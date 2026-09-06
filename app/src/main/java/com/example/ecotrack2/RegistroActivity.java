package com.example.ecotrack2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    EditText etCliente, etPeso, etObservaciones;
    Spinner spinnerTipoResiduo;
    Button btnGuardar, btnVolver;
    DatabaseHelper dbHelper;

    String[] tiposResiduo = {
            "Orgánico",
            "Reciclable - Plástico",
            "Reciclable - Papel/Cartón",
            "Reciclable - Vidrio",
            "Reciclable - Metal",
            "No Aprovechable",
            "Peligroso - Químico",
            "Peligroso - Biológico",
            "Peligroso - Eléctrico"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dbHelper = new DatabaseHelper(this);

        etCliente = findViewById(R.id.etCliente);
        etPeso = findViewById(R.id.etPeso);
        etObservaciones = findViewById(R.id.etObservaciones);
        spinnerTipoResiduo = findViewById(R.id.spinnerTipoResiduo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);

        // Configurar Spinner con los tipos de residuos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, tiposResiduo);
        spinnerTipoResiduo.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> guardarResiduo());

        btnVolver.setOnClickListener(v -> finish());
    }

    private void guardarResiduo() {
        String cliente = etCliente.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();
        String tipo = spinnerTipoResiduo.getSelectedItem().toString();

        // Validaciones
        if (cliente.isEmpty()) {
            etCliente.setError("Ingrese el cliente");
            return;
        }
        if (pesoStr.isEmpty()) {
            etPeso.setError("Ingrese el peso");
            return;
        }

        double peso = Double.parseDouble(pesoStr);

        // Obtener fecha y hora actual
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String fecha = sdfFecha.format(new Date());
        String hora = sdfHora.format(new Date());

        // Crear objeto Residuo
        Residuo residuo = new Residuo(tipo, peso, fecha, hora, cliente, observaciones);

        // 1. Guardar en base de datos local (SQLite)
        boolean resultado = dbHelper.addResiduo(residuo);

        if (resultado) {
            Toast.makeText(this, "✓ Residuo registrado localmente", Toast.LENGTH_SHORT).show();
            limpiarCampos();

            // ==========================================================
            // 2. SIMULACIÓN DE ENVÍO A API RESTFUL (Retrofit)
            // ==========================================================
            ApiResiduo apiResiduo = new ApiResiduo(tipo, peso, cliente);
            Call<ApiResiduo> call = RetrofitClient.getApi().enviarResiduo(apiResiduo);

            call.enqueue(new Callback<ApiResiduo>() {
                @Override
                public void onResponse(Call<ApiResiduo> call, Response<ApiResiduo> response) {
                    if (response.isSuccessful()) {
                        // Éxito: El servidor "recibió" los datos
                        Log.d("API_ECOLIM", "✅ Datos sincronizados con el servidor. Respuesta: " + response.code());
                        Toast.makeText(RegistroActivity.this, "☁️ Sincronizado con la nube", Toast.LENGTH_LONG).show();
                    } else {
                        // Error del servidor (ej: 404, 500)
                        Log.e("API_ECOLIM", "⚠️ Error del servidor: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ApiResiduo> call, Throwable t) {
                    // Fallo de red (ej: sin internet). Los datos YA están seguros en SQLite.
                    Log.e("API_ECOLIM", "❌ Fallo de red (Modo Offline activado): " + t.getMessage());
                    Toast.makeText(RegistroActivity.this, "⚠️ Sin internet. Datos guardados localmente.", Toast.LENGTH_SHORT).show();
                }
            });
            // ==========================================================

        } else {
            Toast.makeText(this, "✗ Error al guardar en la base de datos", Toast.LENGTH_LONG).show();
        }
    }

    private void limpiarCampos() {
        etCliente.setText("");
        etPeso.setText("");
        etObservaciones.setText("");
        spinnerTipoResiduo.setSelection(0);
    }
}