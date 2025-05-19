import mp from "../config/mercadopago.js";
import Usuario from "../models/user.js";
import PagamentoBD from "../models/pagamento.js";
import { v4 as uuidv4 } from "uuid";

const criarPagamento = async (usuarioId, valor) => {
  const usuario = await Usuario.findByPk(usuarioId);
  if (!usuario) throw new Error("Usuário não encontrado");

  const pagamentoId = uuidv4();

  const dados = await mp.create({
    body: {
      transaction_amount: valor,
      payment_method_id: "pix",
      payer: {
        email: usuario.email,
      },
      external_reference: pagamentoId,
    },
  });

  const pagamento = await PagamentoBD.create({
    usuarioId,
    valor: valor,
    status: dados.status || "pendente",
    referenciaExterna: pagamentoId,
    metodoPagamento: "pix",
    paymentIdMP: dados.id,
    qrCode: dados.point_of_interaction.transaction_data.qr_code,
    qrCodeBase64: dados.point_of_interaction.transaction_data.qr_code_base64,
    ticketUrl: dados.point_of_interaction.transaction_data.ticket_url,
  });

  return pagamento;
};

const getPagamento = async (pagamentoId) => {
  try {
    const resultado = await mp.get({ id: pagamentoId });

    return resultado;
  } catch (error) {
    console.error(error);
    throw new Error("Erro ao consultar pagamento");
  }
};

export default {
  criarPagamento,
  getPagamento
};
