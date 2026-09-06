package com.example.ecotrack2;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarActivity extends AppCompatActivity {

    EditText etClienteEditar, etPesoEditar, etObservacionesEditar;
    Spinner spinnerTipoResiduoEditar;
    Button btnGuardarCambios, btnCancelar, btnVolverEditar;
    DatabaseHelper dbHelper;
    int residuoId;

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
        setContentView(R.layout.activity_editar);

        dbHelper = new DatabaseHelper(this);

        // Inicializar vistas
        etClienteEditar = findViewById(R.id.etClienteEditar);
        etPesoEditar = findViewById(R.id.etPesoEditar);
        etObservacionesEditar = findViewById(R.id.etObservacionesEditar);
        spinnerTipoResiduoEditar = findViewById(R.id.spinnerTipoResiduoEditar);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnVolverEditar = findViewById(R.id.btnVolverEditar);

        // Configurar Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, tiposResiduo);
        spinnerTipoResiduoEditar.setAdapter(adapter);

        // Obtener ID del residuo a editar
        residuoId = getIntent().getIntExtra("residuo_id", -1);

        if (residuoId != -1) {
            cargarDatosResiduo();
        } else {
            Toast.makeText(this, "Error: No se encontró el residuo", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Listeners
        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
        btnCancelar.setOnClickListener(v -> finish());
        btnVolverEditar.setOnClickListener(v -> finish());
    }

    private void cargarDatosResiduo() {
        Residuo residuo = dbHelper.getResiduoById(residuoId);
        if (residuo != null) {
            etClienteEditar.setText(residuo.getCliente());
            etPesoEditar.setText(String.valueOf(residuo.getPesoKg()));
            etObservacionesEditar.setText(residuo.getObservaciones());

            // Seleccionar el tipo en el spinner
            String tipo = residuo.getTipoResiduo();
            for (int i = 0; i < tiposResiduo.length; i++) {
                if (tiposResiduo[i].equals(tipo)) {
                    spinnerTipoResiduoEditar.setSelection(i);
                    break;
                }
            }
        }
    }

    private void guardarCambios() {
        String cliente = etClienteEditar.getText().toString().trim();
        String pesoStr = etPesoEditar.getText().toString().trim();
        String observaciones = etObservacionesEditar.getText().toString().trim();
        String tipo = spinnerTipoResiduoEditar.getSelectedItem().toString();

        // Validaciones
        if (cliente.isEmpty()) {
            etClienteEditar.setError("Ingrese el cliente");
            return;
        }
        if (pesoStr.isEmpty()) {
            etPesoEditar.setError("Ingrese el peso");
            return;
        }

        double peso = Double.parseDouble(pesoStr);

        // Crear objeto con los nuevos datos (manteniendo fecha y hora originales)
        Residuo residuoOriginal = dbHelper.getResiduoById(residuoId);
        Residuo residuoActualizado = new Residuo(
                tipo, peso, residuoOriginal.getFecha(),
                residuoOriginal.getHora(), cliente, observaciones
        );
        residuoActualizado.setId(residuoId);

        // Actualizar en la base de datos
        boolean resultado = dbHelper.updateResiduo(residuoActualizado);

        if (resultado) {
            Toast.makeText(this, "✓ Cambios guardados correctamente", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK); // Indicar que se actualizó
            finish();
        } else {
            Toast.makeText(this, "✗ Error al guardar los cambios", Toast.LENGTH_LONG).show();
        }
    }
}