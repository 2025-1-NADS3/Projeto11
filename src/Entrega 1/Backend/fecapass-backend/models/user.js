const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const User = sequelize.define('Usuario', { 
    id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
    nome: { type: DataTypes.STRING, allowNull: false },
    email: { type: DataTypes.STRING, allowNull: false, unique: true },
    senha: { type: DataTypes.STRING, allowNull: false },
    codigoRecuperacao: { type: DataTypes.STRING, allowNull: true }
}, {
    tableName: 'usuarios', // Define o nome da tabela no banco
    timestamps: true // Adiciona os campos createdAt e updatedAt automaticamente
});

module.exports = User;
