// src/config/mercadopago.js
import { Payment as MP_Pagamento } from "mercadopago";

const mp = new MP_Pagamento({ accessToken: process.env.ACCESS_TOKEN });

export default mp;
