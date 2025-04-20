const express = require('express');
const sequelize = require('./config/database');
const userRoutes = require('./routes/userRoutes');
const cors = require('cors');
require('dotenv').config();

const app = express();
app.use(express.json());
app.use(cors()); // Permite o CORS
app.use('/api', userRoutes);

sequelize.sync({ force: false }).then(() => {
    console.log('Banco de dados sincronizado');
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
    console.log(`Servidor rodando na porta ${PORT}`);
});
