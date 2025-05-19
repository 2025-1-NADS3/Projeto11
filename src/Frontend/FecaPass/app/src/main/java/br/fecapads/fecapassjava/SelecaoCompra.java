package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SelecaoCompra extends AppCompatActivity {
    private TextView txtLote, txtPreco, txtCartTotal, txtQuantidadeLabel;
    private ImageButton btnMenos, btnMais;
    private Button btnComprar, btnAplicarCupom;
    private EditText etCupom;
    private double precoIngresso;
    private int quantidade = 0;
    private String titulo, local, data, descricao;
    private int eventoId;
    private boolean cupomAplicado = false; // Flag para verificar se o cupom está aplicado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_compra);

        // Inicializar componentes
        txtLote = findViewById(R.id.lote);
        txtPreco = findViewById(R.id.preco);
        txtQuantidadeLabel = findViewById(R.id.quantidadeLabel);
        txtCartTotal = findViewById(R.id.cartTotal);
        btnMenos = findViewById(R.id.btnMenos);
        btnMais = findViewById(R.id.btnMais);
        btnComprar = findViewById(R.id.btnComprar);
        btnAplicarCupom = findViewById(R.id.btnAplicarCupom);
        etCupom = findViewById(R.id.etCupom);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Receber dados da Intent
        Intent intent = getIntent();
        eventoId = intent.getIntExtra("eventoId", -1);
        titulo = intent.getStringExtra("titulo");
        precoIngresso = intent.getDoubleExtra("preco", 0.0);
        local = intent.getStringExtra("local");
        data = intent.getStringExtra("data");
        descricao = intent.getStringExtra("descricao");

        // Validar eventoId e preço
        if (eventoId <= 0 || precoIngresso <= 0) {
            Toast.makeText(this, "Evento ou preço inválido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Exibir dados iniciais
        txtPreco.setText(String.format("R$ %.2f", precoIngresso));
        txtCartTotal.setText(String.format("R$ %.2f", 0.0));
        txtQuantidadeLabel.setText("0 ingresso(s)");

        // Ação do botão Aplicar Cupom
        btnAplicarCupom.setOnClickListener(v -> {
            String cupom = etCupom.getText().toString().trim().toUpperCase();
            if (cupom.equals("PRIMEIRACOMPRA")) {
                precoIngresso = 1.00; // Ajusta o preço do ingresso para R$ 1,00
                cupomAplicado = true;
                Toast.makeText(this, "Cupom aplicado! Preço por ingresso: R$ 1,00", Toast.LENGTH_SHORT).show();
            } else {
                precoIngresso = intent.getDoubleExtra("preco", 0.0); // Restaura o preço original
                cupomAplicado = false;
                Toast.makeText(this, "Cupom inválido", Toast.LENGTH_SHORT).show();
            }
            // Atualiza os valores exibidos
            txtPreco.setText(String.format("R$ %.2f", precoIngresso));
            txtCartTotal.setText(String.format("R$ %.2f", precoIngresso * quantidade));
        });

        // Ações dos botões
        btnMenos.setOnClickListener(v -> {
            if (quantidade > 0) {
                quantidade--;
                txtQuantidadeLabel.setText(quantidade + " ingresso(s)");
                txtCartTotal.setText(String.format("R$ %.2f", precoIngresso * quantidade));
            }
        });

        btnMais.setOnClickListener(v -> {
            quantidade++;
            txtQuantidadeLabel.setText(quantidade + " ingresso(s)");
            txtCartTotal.setText(String.format("R$ %.2f", precoIngresso * quantidade));
        });

        btnComprar.setOnClickListener(v -> {
            if (quantidade == 0) {
                Toast.makeText(this, "Selecione pelo menos um ingresso", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intentFormaPagamento = new Intent(SelecaoCompra.this, FormaDePagamento.class);
            intentFormaPagamento.putExtra("eventoId", eventoId);
            intentFormaPagamento.putExtra("titulo", titulo);
            intentFormaPagamento.putExtra("valorTotal", precoIngresso * quantidade);
            intentFormaPagamento.putExtra("quantidade", quantidade);
            intentFormaPagamento.putExtra("local", local);
            intentFormaPagamento.putExtra("data", data);
            intentFormaPagamento.putExtra("descricao", descricao);
            intentFormaPagamento.putExtra("cupomAplicado", cupomAplicado); // Passa o status do cupom
            startActivity(intentFormaPagamento);
        });

        btnBack.setOnClickListener(v -> finish());
    }
}