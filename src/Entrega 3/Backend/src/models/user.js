import { DataTypes } from "sequelize";
import sequelize from "../config/database.js";
import Pagamento from "./pagamento.js"; // Importa a model de pagamento


const User = sequelize.define("Usuario", { 
    id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
    nome: { type: DataTypes.STRING, allowNull: false },
    email: { type: DataTypes.STRING, allowNull: false, unique: true },
    senha: { type: DataTypes.STRING, allowNull: false }
}, {
    tableName: "usuarios",
    timestamps: true
});

// 🔹 Define a relação: Um usuário pode ter vários pagamentos
User.hasMany(Pagamento, { foreignKey: "usuarioId", as: "pagamentos" });
Pagamento.belongsTo(User, { foreignKey: "usuarioId", as: "usuario" });
export default User;