import express from "express";
import cors from "cors";
import dotenv from "dotenv";
import sequelize from "./src/config/database.js";
import userRoutes from "./src/routes/userRoutes.js";
import pagamentoRoutes from "./src/routes/pagamentoRoutes.js";

import "./src/config/mercadopago.js"; // Importa a configuração do Mercado Pago


dotenv.config();
const app = express();
app.use(express.json());
app.use(cors()); 

app.use("/api", userRoutes);
app.use("/api", pagamentoRoutes)

sequelize.sync({ force: false }).then(() => {
  console.log("Banco de dados sincronizado");
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Servidor rodando na porta ${PORT}`);
});
