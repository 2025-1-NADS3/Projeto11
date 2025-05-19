package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Pix extends AppCompatActivity {
    private Switch switchTermos;
    private Button btnComprar;
    private TextView txtValorTotal, txtQuantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pix);

        // Inicializar componentes
        txtValorTotal = findViewById(R.id.valorTotal);
        txtQuantidade = findViewById(R.id.txtQuantidade); // Novo TextView
        switchTermos = findViewById(R.id.switchTermos);
        btnComprar = findViewById(R.id.btnComprar);
        Button btnLerTermos = findViewById(R.id.btnLerTermos);
        ImageView btnVoltar = findViewById(R.id.btnVoltar);

        Intent intent = getIntent();
        int eventoId = intent.getIntExtra("eventoId", -1);
        String titulo = intent.getStringExtra("titulo");
        double valorTotal = intent.getDoubleExtra("valorTotal", 0.0);
        int quantidade = intent.getIntExtra("quantidade", 0);
        String local = intent.getStringExtra("local");
        String data = intent.getStringExtra("data");
        String descricao = intent.getStringExtra("descricao");

        if (eventoId <= 0 || quantidade <= 0 || valorTotal <= 0) {
            Toast.makeText(this, "Evento, quantidade ou valor inválido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Exibir valor total e quantidade
        txtValorTotal.setText(String.format("R$ %.2f", valorTotal));
        txtQuantidade.setText(quantidade + " ingresso(s)");

        // Ação do botão Ler Termos
        btnLerTermos.setOnClickListener(v -> Toast.makeText(this, "Exibir termos de uso", Toast.LENGTH_SHORT).show());

        // Ação do botão Comprar
        btnComprar.setOnClickListener(v -> {
            if (!switchTermos.isChecked()) {
                Toast.makeText(this, "Você deve aceitar os termos de uso", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intentPedido = new Intent(Pix.this, Pedido.class);
            intentPedido.putExtra("eventoId", eventoId);
            intentPedido.putExtra("titulo", titulo);
            intentPedido.putExtra("valorTotal", valorTotal);
            intentPedido.putExtra("quantidade", quantidade);
            intentPedido.putExtra("local", local);
            intentPedido.putExtra("data", data);
            intentPedido.putExtra("descricao", descricao);
            startActivity(intentPedido);
        });

        btnVoltar.setOnClickListener(v -> finish());
    }
}