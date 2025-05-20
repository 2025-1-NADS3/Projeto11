require('dotenv').config();

const express = require('express');
const cors = require('cors');

const sequelize = require('./src/config/database');
const userRoutes = require('./src/routes/userRoutes');

const app = express();

app.use(express.json());
app.use(cors());

app.use('/api', userRoutes);

sequelize.authenticate()
  .then(() => {
    console.log('✅ Conexão com o banco de dados estabelecida com sucesso.');
    return sequelize.sync({ force: false });
  })
  .then(() => {
    console.log('📦 Banco de dados sincronizado');
  })
  .catch((err) => {
    console.error('❌ Erro ao conectar com o banco de dados:', err);
  });

if (process.env.NODE_ENV !== 'test') {
  const PORT = process.env.PORT || 8080;
  app.listen(PORT, () => {
    console.log(`🚀 Servidor rodando na porta ${PORT}`);
  });
}

module.exports = app;
