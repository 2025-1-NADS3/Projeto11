import { response } from "express";
import pagamentoService from "../services/pagamentoService.js";
import PagamentoBD from "../models/pagamento.js";

const getPagamento = async (req, res) => {
  try {
    const resultado = await pagamentoService.getPagamento(
      req.headers.paymentid
    );
    return res.status(200).send({ response: resultado });
  } catch (error) {
    console.log(error);
    return res.status(500).send({ error: "Erro ao buscar pagamento" });
  }
  
};

const criarPagamento = async(req, res) =>{
    try{
  const usuarioId = req.usuario.id;
  const valor = Number(req.headers.value);

  if (!usuarioId || isNaN(valor)) {
      return res.status(400).send({ error: "Dados inválidos." });
    }

  const pagamento = await pagamentoService.criarPagamento(usuarioId, valor);
  return res.status(201).send({response: pagamento.ticketUrl});
}
catch(error){
    console.log(err);
    return res.status(500).send({ error: "Erro ao criar pagamento" });
}}

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
    notificacao
  };