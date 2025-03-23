const User = require('../models/user');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

exports.cadastro = async (req, res) => {
    try {
        const { nome, email, senha} = req.body;
        const hashedSenha = await bcrypt.hash(senha, 10); // Criptografa a senha
        const user = await User.create({ nome, email, senha: hashedSenha }); // Cria o usuário no banco de dados
        res.status(201).json(user); // Retorna o usuário criado com status 201 (Criado)
    } catch (error) {
        res.status(400).json({ error: error.message }); // Caso ocorra erro, retorna status 400 (Erro de requisição)
    }
};


exports.login = async (req, res) => {
    try {
        const { email, senha } = req.body;
        const user = await User.findOne({ where: { email } }); // Busca o usuário pelo email
        if (!user || !(await bcrypt.compare(senha, user.senha))) { // Verifica se o usuário existe e se a senha confere
            return res.status(401).json({ error: 'Credenciais inválidas' });
        }
        const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, { expiresIn: '1h' });
        res.json({ token });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

exports.getUsers = async (req, res) => {
    const users = await User.findAll();
    res.json(users);
};
