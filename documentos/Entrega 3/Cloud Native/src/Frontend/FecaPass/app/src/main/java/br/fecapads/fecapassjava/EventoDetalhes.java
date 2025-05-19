package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class EventoDetalhes extends AppCompatActivity {
    private ImageView eventoBanner, btnVoltar, btnFavoritar, btnCompartilhar, iconeMapa;
    private TextView txtTitulo, txtData, txtLocal, txtDescricao, txtPreco;
    private Button btnComprar;
    private int eventoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalhes);

        Log.d("EventoDetalhes", "Activity iniciada");

        // Inicializar os componentes
        eventoBanner = findViewById(R.id.imageView3);
        btnVoltar = findViewById(R.id.iconeVoltar);
        btnFavoritar = findViewById(R.id.iconeFavoritar);
        btnCompartilhar = findViewById(R.id.iconeCompartilhar);
        iconeMapa = findViewById(R.id.imageView5);
        txtTitulo = findViewById(R.id.textTitulo);
        txtData = findViewById(R.id.textData);
        txtLocal = findViewById(R.id.textLocal);
        txtDescricao = findViewById(R.id.textDescricao);
        txtPreco = findViewById(R.id.textPreco);
        btnComprar = findViewById(R.id.btnComprar);

        // Receber os dados do evento da Intent
        Intent intent = getIntent();
        eventoId = intent.getIntExtra("id", -1);
        String titulo = intent.getStringExtra("titulo");
        String data = intent.getStringExtra("data");
        String local = intent.getStringExtra("local");
        String descricao = intent.getStringExtra("descricao");
        String imagemUrl = intent.getStringExtra("imagemUrl");
        Double preco = intent.hasExtra("preco") ? intent.getDoubleExtra("preco", 0.0) : null;

        Log.d("EventoDetalhes", "Dados recebidos - ID: " + eventoId + ", Título: " + titulo + ", Preço: " + preco);

        // Preencher os campos com os dados do evento
        txtTitulo.setText(titulo != null ? titulo : "Título não disponível");
        txtData.setText(data != null ? data : "Data não disponível");
        txtLocal.setText(local != null ? local : "Local não disponível");
        txtDescricao.setText(descricao != null ? descricao : "Descrição não disponível");
        txtPreco.setText(preco != null && preco > 0 ? String.format("R$ %.2f", preco) : "Preço não disponível");

        // Carregar a imagem do banner usando Glide
        if (imagemUrl != null && !imagemUrl.isEmpty()) {
            Glide.with(this)
                    .load(imagemUrl)
                    .placeholder(R.drawable.placeholder_img)
                    .into(eventoBanner);
        } else {
            eventoBanner.setImageResource(R.drawable.placeholder_img);
        }

        // Ação do botão "Voltar"
        btnVoltar.setOnClickListener(v -> finish());

        // Ação do botão "Favoritar"
        btnFavoritar.setOnClickListener(v -> Toast.makeText(this, "Evento favoritado!", Toast.LENGTH_SHORT).show());

        // Ação do botão "Compartilhar"
        btnCompartilhar.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Confira o evento " + (titulo != null ? titulo : "desconhecido") + " no FecaPass! " + (data != null ? data : "") + " em " + (local != null ? local : ""));
            startActivity(Intent.createChooser(shareIntent, "Compartilhar evento"));
        });

        // Verificar se o preço e o ID estão disponíveis
        btnComprar.setEnabled(eventoId > 0 && preco != null && preco > 0);
        if (!btnComprar.isEnabled()) {
            Toast.makeText(this, "Preço ou ID do evento não disponível", Toast.LENGTH_LONG).show();
        } else {
            // Ação do botão "Comprar ingresso"
            btnComprar.setOnClickListener(v -> {
                Intent intentCompra = new Intent(EventoDetalhes.this, SelecaoCompra.class);
                intentCompra.putExtra("eventoId", eventoId);
                intentCompra.putExtra("titulo", titulo);
                intentCompra.putExtra("preco", preco);
                intentCompra.putExtra("local", local);
                intentCompra.putExtra("data", data);
                intentCompra.putExtra("descricao", descricao);
                try {
                    startActivity(intentCompra);
                } catch (Exception e) {
                    Log.e("EventoDetalhes", "Erro ao abrir SelecaoCompra: ", e);
                    Toast.makeText(this, "Erro ao abrir a tela de compra", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}