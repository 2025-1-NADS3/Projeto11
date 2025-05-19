import { Router } from "express";
import pagamentoController from "../controllers/pagamentoController.js";

const router = Router();

// Criar um pagamento
router.post("/pagamento", pagamentoController.criarPagamento);

// Buscar um pagamento
router.get("/pagamento", pagamentoController.getPagamento);

// Receber notificações do Mercado Pago (webhook)
router.post("/notificacao", pagamentoController.notificacao);

export default router;
