package com.example.ecotrack2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResiduoAdapter extends RecyclerView.Adapter<ResiduoAdapter.ViewHolder> {

    private List<Residuo> residuos;
    private DatabaseHelper dbHelper;
    private OnItemClickListener listener;

    // Interfaz para comunicar clicks al Activity
    public interface OnItemClickListener {
        void onEditClick(Residuo residuo);
        void onDeleteClick(Residuo residuo, int position);
    }

    public ResiduoAdapter(List<Residuo> residuos, DatabaseHelper dbHelper, OnItemClickListener listener) {
        this.residuos = residuos;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_residuo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Residuo residuo = residuos.get(position);

        holder.tvTipoResiduo.setText("Tipo: " + residuo.getTipoResiduo());
        holder.tvPeso.setText(String.format("%.1f Kg", residuo.getPesoKg()));
        holder.tvCliente.setText("Cliente: " + residuo.getCliente());
        holder.tvFechaHora.setText("📅 " + residuo.getFecha() + " - " + residuo.getHora());
        holder.tvObservaciones.setText("📝 " + residuo.getObservaciones());

        // Click en Editar
        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(residuo);
            }
        });

        // Click en Eliminar con confirmación
        holder.btnEliminar.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar Residuo")
                    .setMessage("¿Estás seguro de eliminar este registro?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        boolean eliminado = dbHelper.deleteResiduo(residuo.getId());
                        if (eliminado) {
                            residuos.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, residuos.size());
                            Toast.makeText(v.getContext(), "Residuo eliminado", Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.onDeleteClick(residuo, position);
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return residuos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipoResiduo, tvPeso, tvCliente, tvFechaHora, tvObservaciones;
        Button btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoResiduo = itemView.findViewById(R.id.tvTipoResiduo);
            tvPeso = itemView.findViewById(R.id.tvPeso);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHora);
            tvObservaciones = itemView.findViewById(R.id.tvObservaciones);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
