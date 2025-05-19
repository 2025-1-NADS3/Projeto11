package br.fecapads.fecapassjava;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import br.fecapads.fecapassjava.model.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventViewHolder> {
    private List<Evento> eventoList;

    public EventoAdapter(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }
    public void updateEventos(List<Evento> novaLista) {
        this.eventoList.clear();
        this.eventoList.addAll(novaLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_evento, parent, false);
        Log.d("Eventos", "ViewHolder criado");
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Evento evento = eventoList.get(position);
        holder.titulo.setText(evento.getTitulo());
        holder.data.setText(evento.getData());
        holder.descreve.setText(evento.getDescricao());

        Glide.with(holder.itemView.getContext())
                .load(evento.getImagemUrl())
                .placeholder(R.drawable.placeholder_img)
                .into(holder.imagemEvento);

        holder.itemView.setOnClickListener(v -> {
            Log.d("EventoAdapter", "Evento clicado: " + evento.getTitulo());
            Intent intent = new Intent(holder.itemView.getContext(), EventoDetalhes.class);
            intent.putExtra("id", evento.getId());
            intent.putExtra("titulo", evento.getTitulo());
            intent.putExtra("data", evento.getData());
            intent.putExtra("local", evento.getLocalizacao());
            intent.putExtra("descricao", evento.getDescricao());
            intent.putExtra("imagemUrl", evento.getImagemUrl());
            intent.putExtra("preco", evento.getPrecoAsDouble());
            try {
                holder.itemView.getContext().startActivity(intent);
            } catch (Exception e) {
                Log.e("EventoAdapter", "Erro ao abrir EventoDetalhes: ", e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, data, descreve;
        ImageView imagemEvento;

        public EventViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo);
            data = itemView.findViewById(R.id.data);
            descreve = itemView.findViewById(R.id.descreve);
            imagemEvento = itemView.findViewById(R.id.imagemEvento);
        }
    }
}