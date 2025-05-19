import express from "express";
import pagamentoController from "../controllers/pagamentoController.js"
import authMiddleware from "../middlewares/authMiddleware.js";

const router = express.Router();

router.get("/getPagamento", pagamentoController.getPagamento);
router.put("/criarPagamento", authMiddleware, pagamentoController.criarPagamento);
router.post("/notificacao", pagamentoController.notificacao);

export default router;
