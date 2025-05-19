import { DataTypes } from "sequelize";
import sequelize from "../config/database.js";

const Pagamento = sequelize.define("Pagamento", {
    id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
    usuarioId: { type: DataTypes.INTEGER, allowNull: false },
    valor: { type: DataTypes.FLOAT, allowNull: false },
    status: { 
        type: DataTypes.STRING, 
        allowNull: false, 
        defaultValue: "pendente" 
    },
    referenciaExterna: { type: DataTypes.STRING, allowNull: false },
    metodoPagamento: { type: DataTypes.STRING, allowNull: false },

    paymentIdMP: { type: DataTypes.BIGINT }, // ID que o Mercado Pago retorna
    qrCode: { type: DataTypes.TEXT }, // Código QR em texto
    qrCodeBase64: { type: DataTypes.TEXT }, // QR Code em imagem base64
    ticketUrl: { type: DataTypes.TEXT }, // Link para o "boleto" do PIX

}, {
    tableName: "pagamentos",
    timestamps: true
});

export default Pagamento;