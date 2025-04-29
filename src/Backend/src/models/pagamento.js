import { DataTypes } from "sequelize";
import sequelize from "../config/database.js";

const Pagamento = sequelize.define("Pagamento", {
    id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
    usuarioId: { type: DataTypes.INTEGER, allowNull: false },
    eventoId: { type: DataTypes.INTEGER, allowNull: false }, // Chave estrangeira para Evento
    valor: { type: DataTypes.FLOAT, allowNull: false },
    quantidade: {
        type: DataTypes.INTEGER,
        allowNull: false,
        defaultValue: 1, // Adicionado
      },
    status: { 
        type: DataTypes.STRING, 
        allowNull: false, 
        defaultValue: "pendente" 
    },
    referenciaExterna: { type: DataTypes.STRING, allowNull: false },
    metodoPagamento: { type: DataTypes.STRING, allowNull: false },
    paymentIdMP: { type: DataTypes.STRING }, // Alterado para STRING
    qrCode: { type: DataTypes.TEXT }, // Código QR em texto
    qrCodeBase64: { type: DataTypes.TEXT }, // QR Code em imagem base64
    ticketUrl: { type: DataTypes.TEXT }, // Link para o "boleto" do PIX
}, {
    tableName: "pagamentos",
    timestamps: true
});

export default Pagamento;