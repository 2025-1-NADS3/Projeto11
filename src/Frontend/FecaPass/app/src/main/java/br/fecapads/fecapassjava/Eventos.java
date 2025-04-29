package br.fecapads.fecapassjava;

import br.fecapads.fecapassjava.model.Evento;
import br.fecapads.fecapassjava.services.apiService;

import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Eventos extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventoAdapter eventAdapter;
    private List<Evento> eventoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        eventAdapter = new EventoAdapter(eventoList);
        recyclerView.setAdapter(eventAdapter);

        fetchEventos();
    }

    private void fetchEventos() {
        new Thread(() -> {
            try {
                apiService api = new apiService();
                String response = api.getEventos();
                Log.d("Eventos", "Resposta do backend: " + response);
                if (response != null) {
                    List<Evento> novaLista = parseEventos(response);
                    runOnUiThread(() -> {
                        eventAdapter.updateEventos(novaLista);
                        Log.d("Eventos", "Eventos carregados: " + novaLista.size() + " eventos");
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(Eventos.this, "Erro ao carregar eventos", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(Eventos.this, "Erro de conexão", Toast.LENGTH_SHORT).show());
                Log.e("Eventos", "Erro ao buscar eventos: ", e);
            }
        }).start();
    }

    private List<Evento> parseEventos(String json) {
        Gson gson = new Gson();
        try {
            Evento[] eventosArray = gson.fromJson(json, Evento[].class);
            List<Evento> eventosList = new ArrayList<>();
            for (Evento evento : eventosArray) {
                eventosList.add(evento);
            }
            return eventosList;
        } catch (Exception e) {
            Log.e("Eventos", "Erro ao parsear JSON: ", e);
            runOnUiThread(() -> Toast.makeText(Eventos.this, "Erro ao processar dados dos eventos", Toast.LENGTH_SHORT).show());
            return new ArrayList<>();
        }
    }
}