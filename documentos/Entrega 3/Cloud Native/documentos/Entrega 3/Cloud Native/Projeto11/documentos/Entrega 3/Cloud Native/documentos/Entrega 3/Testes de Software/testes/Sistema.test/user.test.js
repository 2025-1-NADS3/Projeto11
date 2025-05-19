const request = require('supertest');
const bcrypt = require('bcrypt');
const app = require('../../../server');
const Usuario = require('../../models/user'); 
process.env.JWT_SECRET = 'segredo-de-teste';

describe('Rotas de Usuário - Teste E2E', () => {
  const testUser = {
    nome: 'usuarioTeste',
    email: 'teste@teste.com',
    senha: 'Senha123',
  };

  describe('Cadastro', () => {
    beforeAll(async () => {
      await Usuario.destroy({ where: { email: testUser.email } });
    });

    it('Deve cadastrar um usuário com sucesso', async () => {
      const res = await request(app)
        .post('/api/cadastro')
        .send(testUser);

      expect(res.statusCode).toBe(201);
      expect(res.body).toHaveProperty('success', true);
      expect(res.body).toHaveProperty('message', 'Usuário cadastrado com sucesso!');
      expect(res.body).toHaveProperty('user');
      expect(res.body.user).toHaveProperty('id');
      expect(res.body.user).toHaveProperty('nome', testUser.nome);
    });
  });

describe('Login', () => {
  beforeAll(async () => {
    // Apaga qualquer usuário com o email de teste, para evitar conflito
    await Usuario.destroy({ where: { email: testUser.email } });

    // Cria o hash da senha para armazenar no banco
    const senhaHash = await bcrypt.hash(testUser.senha, 10);

    // Cria o usuário com a senha já hashada
    await Usuario.create({
      nome: testUser.nome,
      email: testUser.email,
      senha: senhaHash,
    });
  });

  it('Deve logar o usuário com sucesso', async () => {
    const res = await request(app)
      .post('/api/login')
      .send({
        email: testUser.email,
        senha: testUser.senha,
      });

    expect(res.statusCode).toBe(200);
    expect(res.body).toHaveProperty('message', 'Login realizado com sucesso!');
    expect(res.body).toHaveProperty('token');
  });
});
});
