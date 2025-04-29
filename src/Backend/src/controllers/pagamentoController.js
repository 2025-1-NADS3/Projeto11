import pagamentoService from "../services/pagamentoService.js";

const getPagamento = async (req, res) => {
  try {
    const resultado = await pagamentoService.getPagamento(req.headers.paymentid);
    return res.status(200).send({ response: resultado });
  } catch (error) {
    console.log(error);
    return res.status(500).send({ error: "Erro ao buscar pagamento" });
  }
};

const criarPagamento = async (req, res) => {
  try {
    const usuarioId = req.usuario.id;
    const eventoId = Number(req.headers.eventoid);
    const quantidade = Number(req.headers.quantidade);


    if (!usuarioId || isNaN(eventoId)) {
      return res.status(400).send({ error: "Dados inválidos." });
    }

    const pagamento = await pagamentoService.criarPagamento(usuarioId, eventoId, quantidade);
    return res.status(201).send({
      ticketUrl: pagamento.ticketUrl,
      qrCode: pagamento.qrCode,
      qrCodeBase64: pagamento.qrCodeBase64,
    });
  } catch (error) {
    console.log(error);
    return res.status(500).send({ error: "Erro ao criar pagamento" });
  }
};

const notificacao = async (req, res) => {
  try {
    if (req.body.action === "payment.update") {
      const pagamentoId = req.body.data.id;
      const response = await pagamentoService.getPagamento(pagamentoId);

      // Atualizar o status do pagamento no banco de dados
      const pagamento = await PagamentoBD.findOne({ where: { paymentIdMP: pagamentoId } });
      if (pagamento) {
        pagamento.status = response.status;
        await pagamento.save();

        // Verificar o status do pagamento e executar ações adicionais se necessário
        if (response.status === "approved") {
          console.log("Pagamento aprovado:", response);
        } else if (response.status === "rejected") {
          console.log("Pagamento rejeitado:", response);
        } else if (response.status === "pending") {
          console.log("Pagamento pendente:", response);
        }
      }
    }
    res.sendStatus(200); // Resposta para confirmar que a notificação foi recebida
  } catch (err) {
    console.log(err);
    res.sendStatus(500);
  }
};

export default {
  criarPagamento,
  getPagamento,
  notificacao,
};