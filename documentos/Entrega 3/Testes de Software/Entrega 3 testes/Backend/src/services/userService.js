const User = require("../models/user");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const crypto = require("crypto");
const nodemailer = require("nodemailer");

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

const updateUser = async (id, nome, senha) => {
  const user = await User.findByPk(id);
  if (!user) {
    throw new Error("Usuário não encontrado.");
  }

  if (nome && !validarNome(nome)) {
    throw new Error("Nome inválido.");
  }
  if (senha && !validarSenha(senha)) {
    throw new Error("Senha inválida.");
  }

  const dadosParaAtualizar = {};
  if (nome) dadosParaAtualizar.nome = nome;
  if (senha) dadosParaAtualizar.senha = await bcrypt.hash(senha, 10);

  await user.update(dadosParaAtualizar);
  return user;
};

const deleteUser = async (id, senha) => {
  const user = await User.findByPk(id);
  if (!user) {
    throw new Error("Usuário não encontrado.");
  }

  if (!(await bcrypt.compare(senha, user.senha))) {
    throw new Error("Senha incorreta. Não foi possível excluir a conta.");
  }

  await user.destroy();
  return { message: "Conta deletada com sucesso." };
};

const enviarCodigoRecuperacao = async (email) => {
  const user = await User.findOne({ where: { email } });
  if (!user) {
    throw new Error("Usuário não encontrado.");
  }

  const codigo = crypto.randomInt(100000, 999999).toString();
  user.codigoRecuperacao = codigo;
  await user.save();

  const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: process.env.EMAIL_USER,
      pass: process.env.EMAIL_PASS
    }
  });

  await transporter.sendMail({
    from: process.env.EMAIL_USER,
    to: email,
    subject: 'Código de Recuperação de Senha',
    text: `Seu código de recuperação é: ${codigo}`
  });

  return { message: "Código de recuperação enviado para o email." };
};

const redefinirSenha = async (email, codigo, novaSenha) => {
  const user = await User.findOne({ where: { email, codigoRecuperacao: codigo } });
  if (!user) {
    throw new Error("Código inválido ou expirado.");
  }

  if (!validarSenha(novaSenha)) {
    throw new Error("Senha inválida.");
  }

  user.senha = await bcrypt.hash(novaSenha, 10);
  user.codigoRecuperacao = null;
  await user.save();

  return { message: "Senha redefinida com sucesso." };
};

module.exports = { 
  cadastrarUsuario, 
  loginUsuario, 
  listarUsuarios, 
  updateUser, 
  deleteUser, 
  enviarCodigoRecuperacao, 
  redefinirSenha 
};
