import request from "supertest";
import express from "express";
import * as pagamentoService from "../services/pagamentoService.js";
import pagamentoController from "../controllers/pagamentoController.js";

// Mock do authMiddleware simplificado
const mockAuthMiddleware = (req, res, next) => {
  req.usuario = { id: "user-123", email: "teste@exemplo.com" };
  next();
};

// Mocks dos models usados pelo serviço
jest.mock("../models/user.js", () => ({
  findByPk: jest.fn().mockResolvedValue({ id: "user-123", email: "teste@exemplo.com" }),
}));

jest.mock("../models/pagamento.js", () => ({
  create: jest.fn().mockResolvedValue({
    usuarioId: "user-123",
    valor: 150,
    status: "approved",
    referenciaExterna: "fake-id",
    metodoPagamento: "pix",
    paymentIdMP: "fake-payment-id",
    qrCode: "fake-qr-code",
    qrCodeBase64: "fake-base64",
    ticketUrl: "https://fake-pix-url.com/",
  }),
}));

jest.mock("../services/pagamentoService.js", () => ({
  criarPagamento: jest.fn().mockResolvedValue({
    ticketUrl: "https://fake-pix-url.com/",
    qrCodeBase64: "fake-base64",
  }),
  getPagamento: jest.fn(),
}));

const app = express();
app.use(express.json());
app.post("/api/pagamento", mockAuthMiddleware, pagamentoController.criarPagamento);

describe("Pagamento", () => {
  it("deve criar um pagamento com sucesso", async () => {
    const response = await request(app)
      .post("/api/pagamento")
      .set("value", "150");

    expect(response.status).toBe(201);
    expect(response.body.ticketUrl).toContain("https://fake-pix-url.com/");
    expect(response.body.qrCodeBase64).toBe("fake-base64");
  });
});
