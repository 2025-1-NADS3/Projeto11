import { DataTypes } from "sequelize";
import sequelize from "../config/database.js";

const Evento = sequelize.define("Evento", {
    id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
    titulo: { type: DataTypes.STRING, allowNull: false },
    categoria: { type: DataTypes.STRING, allowNull: false },
    preco: { type: DataTypes.DECIMAL(10, 2), allowNull: false },
    data: { type: DataTypes.DATEONLY, allowNull: false },
    horario: { type: DataTypes.TIME, allowNull: false },
    localizacao: { type: DataTypes.STRING, allowNull: false },
    descricao: { type: DataTypes.TEXT, allowNull: false },
    imagemUrl: { type: DataTypes.STRING } // opcional
}, {
    tableName: "eventos",
    timestamps: true 
});

export default Evento;