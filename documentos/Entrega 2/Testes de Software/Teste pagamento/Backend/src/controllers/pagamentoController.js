import * as pagamentoService from "../services/pagamentoService.js";

const getPagamento = async (req, res) => {
  try {
    const pagamentoId = req.headers.paymentid;
    const resultado = await pagamentoService.getPagamento(pagamentoId);
    return res.status(200).send({ response: resultado });
  } catch (error) {
    console.log(error);
    return res.status(500).send({ error: "Erro ao buscar pagamento" });
  }
};

const criarPagamento = async (req, res) => {
  try {
    const usuarioId = req.usuario.id;
    const valor = Number(req.headers.value);

    if (!usuarioId || isNaN(valor)) {
      return res.status(400).send({ error: "Dados inválidos." });
    }

    const pagamento = await pagamentoService.criarPagamento(usuarioId, valor);

    return res.status(201).send({ 
      ticketUrl: pagamento.ticketUrl,
      qrCodeBase64: pagamento.qrCodeBase64,
    });
  } catch (error) {
    console.log(error);
    return res.status(500).send({ error: "Erro ao criar pagamento" });
  }
};

export default {
  criarPagamento,
  getPagamento
};
