import { cadastrarUsuario, validarSenha, loginUsuario } from '../Services/userServices.js';
import User from '../models/user.js';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';

jest.mock('../models/user.js', () => ({
  findOne: jest.fn(),
  create: jest.fn(),
  findAll: jest.fn(),
  findByPk: jest.fn(),
}));

jest.mock('jsonwebtoken', () => ({
  sign: jest.fn(() => 'fake-jwt-token'),
}));

describe('Testes de carga e integração para userServices', () => {

  beforeEach(() => {
    jest.clearAllMocks();
  });

  // Teste simples de cadastro
test('🧪 Deve cadastrar usuário com dados válidos (teste de carga leve - 1000 execuções)', async () => {
  const numExecucoes = 1000;

  User.findOne.mockResolvedValue(null);
  User.create.mockImplementation(async (data) => ({ id: Math.floor(Math.random() * 1e9), ...data }));

  const emails = Array.from({ length: numExecucoes }, (_, i) => `userload${i}@email.com`);

  console.log('Iniciando 1.000 cadastros de usuários...');
  const start = Date.now();
  const resultados = await Promise.all(emails.map(email =>
    cadastrarUsuario('Maria Silva', email, 'Senha123')
  ));
  const duration = Date.now() - start;
  console.log(`✅ 1.000 cadastros executados em ${duration} ms`);

  resultados.forEach((result, i) => {
    expect(User.findOne).toHaveBeenCalledWith({ where: { email: emails[i] } });
    expect(User.create).toHaveBeenCalled();
    expect(result).toHaveProperty('id');
    expect(result.nome).toBe('Maria Silva');
    expect(result.email).toBe(emails[i]);
  });
}, 60000);


  // Teste menor para validarSenha - dividido em blocos para feedback rápido
  test('⚙️ Executa validarSenha 10.000 vezes em paralelo (bloco 1)', async () => {
    const numExecucoes = 10_000;

    console.log('Iniciando 10.000 validações de senha...');
    const start = Date.now();
    const promises = Array(numExecucoes).fill(null).map(() => {
      const resultado = validarSenha('Senha123');
      if (!resultado) throw new Error('Falha na validação');
      return true;
    });
    await Promise.all(promises);
    const duration = Date.now() - start;

    console.log(`🧠 ${numExecucoes} validações de senha executadas em ${duration} ms`);
    expect(true).toBe(true);
  }, 30000);

  // Você pode copiar este teste para rodar múltiplos blocos, ex:
  // test('⚙️ Executa validarSenha 10.000 vezes em paralelo (bloco 2)', async () => { ... });

  // Teste de carga para cadastrarUsuario menor (10.000 execuções)
  test('🧪 Executa 10.000 cadastros de usuários em paralelo', async () => {
    const numExecucoes = 10_000;

    User.findOne.mockResolvedValue(null);
    User.create.mockImplementation(async (data) => ({ id: Math.floor(Math.random() * 1e9), ...data }));

    const emails = Array.from({ length: numExecucoes }, (_, i) => `user${i}@exemplo.com`);

    console.log('Iniciando 10.000 cadastros de usuários...');
    const start = Date.now();
    const resultados = await Promise.all(emails.map(email =>
      cadastrarUsuario('Usuário Teste', email, 'Senha123')
    ));
    const duration = Date.now() - start;

    console.log(`📦 ${numExecucoes} cadastros realizados em ${duration} ms`);

    resultados.forEach((user, i) => {
      expect(user).toHaveProperty('id');
      expect(user.email).toBe(emails[i]);
    });
  }, 120000);

  // Teste de carga para loginUsuario menor (10.000 execuções)
  test('🔐 Executa 10.000 logins simultâneos com usuários válidos', async () => {
    const numLogins = 10_000;
    const senha = 'Senha123';
    const senhaHash = await bcrypt.hash(senha, 10);

    User.findOne.mockImplementation(async ({ where }) => ({
      id: Math.floor(Math.random() * 1e9),
      email: where.email,
      senha: senhaHash,
    }));

    const emails = Array.from({ length: numLogins }, (_, i) => `${i}_login@teste.com`);

    console.log('Iniciando 10.000 logins simultâneos...');
    const start = Date.now();
    const resultados = await Promise.all(emails.map(email => loginUsuario(email, senha)));
    const duration = Date.now() - start;

    console.log(`🔐 ${numLogins} logins bem-sucedidos em ${duration} ms`);

    resultados.forEach(res => {
      expect(res).toHaveProperty('token', 'fake-jwt-token');
      expect(res).toHaveProperty('id');
    });
  }, 120000);

});
