const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const User = sequelize.define("Usuario", {
  id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
  nome: { type: DataTypes.STRING, allowNull: false },
  email: { type: DataTypes.STRING, allowNull: false, unique: true },
  senha: { type: DataTypes.STRING, allowNull: false }
}, {
  tableName: "usuarios",
  timestamps: true
});

module.exports = User;
