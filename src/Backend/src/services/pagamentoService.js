import { Payment as MP_Payment } from 'mercadopago';
import Usuario from '../models/user.js';
import Evento from '../models/evento.js';
import PagamentoBD from '../models/pagamento.js';
import { v4 as uuidv4 } from 'uuid';

export const criarPagamento = async (usuarioId, eventoId, quantidade) => {
  try {
    // Validar entrada
    if (!usuarioId || isNaN(eventoId) || isNaN(quantidade) || quantidade <= 0) {
      throw new Error('Dados inválidos: usuarioId, eventoId ou quantidade inválidos');
    }

    // Buscar o usuário logado
    const usuario = await Usuario.findByPk(usuarioId);
    if (!usuario) throw new Error('Usuário não encontrado');

    // Buscar o evento
    const evento = await Evento.findByPk(eventoId);
    if (!evento) throw new Error('Evento não encontrado');

    // Validar e converter preço
    let valorUnitario;
    if (typeof evento.preco === 'number') {
      valorUnitario = evento.preco; // Já é um número (ex.: DECIMAL ou FLOAT)
    } else if (typeof evento.preco === 'string') {
      // Caso o preço seja string, limpar e converter
      const precoLimpo = evento.preco.replace(/[^0-9,.]/g, '').replace(',', '.');
      valorUnitario = parseFloat(precoLimpo);
    } else {
      throw new Error('Preço do evento ausente ou em formato inválido');
    }

    // Validar valorUnitario
    if (isNaN(valorUnitario) || valorUnitario <= 0) {
      throw new Error(`Preço do evento inválido: "${evento.preco}" não é um número válido`);
    }

    // Calcular valor total
    const valor = valorUnitario * quantidade;
    if (isNaN(valor) || valor <= 0) {
      throw new Error('Valor total inválido após cálculo');
    }

    const referenciaExterna = uuidv4();

    const mp = new MP_Payment({ accessToken: process.env.ACCESS_TOKEN });

    const result = await mp.create({
      body: {
        transaction_amount: valor, // Enviar como número
        payment_method_id: 'pix',
        payer: {
          email: usuario.email,
        },
        external_reference: referenciaExterna,
      },
    });

    const data = result;

    const pagamento = await PagamentoBD.create({
      usuarioId,
      eventoId,
      valor: valor, // Salvar como número (DECIMAL no banco)
      quantidade: quantidade,
      status: data.status || 'pendente',
      referenciaExterna: referenciaExterna,
      metodoPagamento: 'pix',
      paymentIdMP: data.id,
      qrCode: data.point_of_interaction?.transaction_data?.qr_code,
      qrCodeBase64: data.point_of_interaction?.transaction_data?.qr_code_base64,
      ticketUrl: data.point_of_interaction?.transaction_data?.ticket_url,
    });

    return pagamento;
  } catch (error) {
    console.error('Error in criarPagamento service:', error);
    throw new Error('Erro ao criar pagamento no serviço: ' + error.message);
  }
};

export const getPagamento = async (pagamentoId) => {
  try {
    const result = await new MP_Payment({
      accessToken: process.env.ACCESS_TOKEN,
    }).get({ id: pagamentoId });

    return result;
  } catch (error) {
    console.error('Error in getPagamento service:', error);
    throw new Error('Erro ao consultar pagamento: ' + error.message);
  }
};

export default {
  criarPagamento,
  getPagamento,
};