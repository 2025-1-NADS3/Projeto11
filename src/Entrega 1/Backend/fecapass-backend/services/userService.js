const User = require('../models/user');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

// Validações
const validarNome = (nome) => {
    return typeof nome === 'string' && 
           nome.trim().length >= 2 &&
           /^[A-Za-zÀ-ÿ\s]+$/.test(nome);
};

const validarEmail = (email) => {
    return typeof email === 'string' &&
           /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

const validarSenha = (senha) => {
    return typeof senha === 'string' &&
           senha.length >= 8 &&
           /[A-Z]/.test(senha) &&
           /[a-z]/.test(senha) &&
           /\d/.test(senha) &&
           !/\s/.test(senha);
};


const cadastrarUsuario = async (nome, email, senha) => {
    if (!validarNome(nome)) {
        throw new Error("Nome inválido. Deve conter pelo menos 2 caracteres e apenas letras e espaços.");
    }
    if (!validarEmail(email)) {
        throw new Error("Email inválido. Use o formato usuario@dominio.com.");
    }
    const userExistente = await User.findOne({ where: { email } });
    if (userExistente) {
        throw new Error("Este e-mail já está em uso. Tente outro.");
    }
    if (!validarSenha(senha)) {
        throw new Error("Senha inválida. Deve ter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma minúscula e um número, sem espaços.");
    }

    const hashedSenha = await bcrypt.hash(senha, 10);
    return await User.create({ nome, email, senha: hashedSenha });
};

const loginUsuario = async (email, senha) => {
    if (!validarEmail(email)) {
        throw new Error("Email inválido.");
    }
    if (!senha) {
        throw new Error("Senha não pode estar vazia.");
    }

    const user = await User.findOne({ where: { email } });
    if (!user || !(await bcrypt.compare(senha, user.senha))) {
        throw new Error("Credenciais inválidas");
    }

    return jwt.sign({ id: user.id }, process.env.JWT_SECRET, { expiresIn: '1h' });
};

const listarUsuarios = async () => {
    return await User.findAll();
};

module.exports = { cadastrarUsuario, loginUsuario, listarUsuarios };
