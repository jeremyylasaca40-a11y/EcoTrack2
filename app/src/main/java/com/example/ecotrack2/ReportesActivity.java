package com.example.ecotrack2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ReportesActivity extends AppCompatActivity {

    private static final int EDIT_REQUEST_CODE = 1;

    RecyclerView recyclerView;
    Spinner spinnerFiltroTipo;
    Button btnFiltrar, btnVolver;
    TextView tvTotal;
    DatabaseHelper dbHelper;
    ResiduoAdapter adapter;
    List<Residuo> listaResiduos;

    String[] opcionesFiltro = {
            "Todos",
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
        setContentView(R.layout.activity_reportes);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewResiduos);
        spinnerFiltroTipo = findViewById(R.id.spinnerFiltroTipo);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        btnVolver = findViewById(R.id.btnVolverReportes);
        tvTotal = findViewById(R.id.tvTotal);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> adapterFiltro = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, opcionesFiltro);
        spinnerFiltroTipo.setAdapter(adapterFiltro);

        cargarResiduos("Todos");

        btnFiltrar.setOnClickListener(v -> {
            String tipoSeleccionado = spinnerFiltroTipo.getSelectedItem().toString();
            cargarResiduos(tipoSeleccionado);
        });

        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarResiduos(String tipo) {
        if (tipo.equals("Todos")) {
            listaResiduos = dbHelper.getAllResiduos();
        } else {
            listaResiduos = dbHelper.getResiduosByTipo(tipo);
        }

        adapter = new ResiduoAdapter(listaResiduos, dbHelper, new ResiduoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Residuo residuo) {
                Intent intent = new Intent(ReportesActivity.this, EditarActivity.class);
                intent.putExtra("residuo_id", residuo.getId());
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }

            @Override
            public void onDeleteClick(Residuo residuo, int position) {
                // Recargar después de eliminar
                String tipoSeleccionado = spinnerFiltroTipo.getSelectedItem().toString();
                cargarResiduos(tipoSeleccionado);
            }
        });

        recyclerView.setAdapter(adapter);

        double total = 0;
        for (Residuo r : listaResiduos) {
            total += r.getPesoKg();
        }
        tvTotal.setText(String.format(Locale.getDefault(), "%.2f Kg (%d registros)", total, listaResiduos.size()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Recargar la lista después de editar
            String tipoSeleccionado = spinnerFiltroTipo.getSelectedItem().toString();
            cargarResiduos(tipoSeleccionado);
            Toast.makeText(this, "Lista actualizada", Toast.LENGTH_SHORT).show();
        }
    }
}


