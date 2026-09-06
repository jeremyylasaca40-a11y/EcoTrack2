package com.example.ecotrack2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class EstadisticasActivity extends AppCompatActivity {

    TextView tvTotalGeneral, tvTotalRegistros;
    TextView tvOrganico, tvReciclable, tvNoAprovechable, tvPeligroso;
    TextView tvResumen;
    Button btnVolver;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        dbHelper = new DatabaseHelper(this);

        tvTotalGeneral = findViewById(R.id.tvTotalGeneral);
        tvTotalRegistros = findViewById(R.id.tvTotalRegistros);
        tvOrganico = findViewById(R.id.tvOrganico);
        tvReciclable = findViewById(R.id.tvReciclable);
        tvNoAprovechable = findViewById(R.id.tvNoAprovechable);
        tvPeligroso = findViewById(R.id.tvPeligroso);
        tvResumen = findViewById(R.id.tvResumen);
        btnVolver = findViewById(R.id.btnVolverEstadisticas);

        cargarEstadisticas();

        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarEstadisticas() {
        List<Residuo> todosLosResiduos = dbHelper.getAllResiduos();

        int totalRegistros = todosLosResiduos.size();
        double totalGeneral = 0;
        double organico = 0, reciclable = 0, noAprovechable = 0, peligroso = 0;

        for (Residuo residuo : todosLosResiduos) {
            totalGeneral += residuo.getPesoKg();

            String tipo = residuo.getTipoResiduo().toLowerCase();

            if (tipo.contains("orgánico")) {
                organico += residuo.getPesoKg();
            } else if (tipo.contains("reciclable")) {
                reciclable += residuo.getPesoKg();
            } else if (tipo.contains("no aprovechable")) {
                noAprovechable += residuo.getPesoKg();
            } else if (tipo.contains("peligroso")) {
                peligroso += residuo.getPesoKg();
            }
        }

        tvTotalGeneral.setText(String.format("%.2f Kg", totalGeneral));
        tvTotalRegistros.setText(totalRegistros + " registros");

        tvOrganico.setText(String.format("%.2f Kg", organico));
        tvReciclable.setText(String.format("%.2f Kg", reciclable));
        tvNoAprovechable.setText(String.format("%.2f Kg", noAprovechable));
        tvPeligroso.setText(String.format("%.2f Kg", peligroso));

        generarResumen(totalGeneral, organico, reciclable, noAprovechable, peligroso, totalRegistros);
    }

    private void generarResumen(double total, double organico, double reciclable,
                                double noAprovechable, double peligroso, int registros) {
        if (registros == 0) {
            tvResumen.setText("Aún no hay registros de residuos.\nComienza registrando tu primera recolección.");
            return;
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("Se han registrado ").append(registros).append(" recolecciones.\n\n");

        double maximo = Math.max(Math.max(organico, reciclable), Math.max(noAprovechable, peligroso));

        if (maximo == organico && organico > 0) {
            resumen.append("✅ El residuo más recolectado es el ORGÁNICO\n");
        } else if (maximo == reciclable && reciclable > 0) {
            resumen.append("✅ El residuo más recolectado es el RECICLABLE\n");
        } else if (maximo == noAprovechable && noAprovechable > 0) {
            resumen.append("✅ El residuo más recolectado es el NO APROVECHABLE\n");
        } else if (maximo == peligroso && peligroso > 0) {
            resumen.append("⚠️ El residuo más recolectado es el PELIGROSO\n");
        }

        if (total > 0) {
            double porcentajeReciclable = (reciclable / total) * 100;
            resumen.append("\n📊 Porcentaje de reciclaje: ")
                    .append(String.format("%.1f", porcentajeReciclable))
                    .append("%");
        }

        tvResumen.setText(resumen.toString());
    }
}