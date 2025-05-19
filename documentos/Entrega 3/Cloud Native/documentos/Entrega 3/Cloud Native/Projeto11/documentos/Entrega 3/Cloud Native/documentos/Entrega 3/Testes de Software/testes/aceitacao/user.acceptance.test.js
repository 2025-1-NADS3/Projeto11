const request = require('supertest');
const app = require('../../../server');
const db = require('../../config/database');

describe('🔐 Teste de Aceitação - Cadastro de Usuário', () => {
  const usuarioValido = {
    nome: 'Adriano',
    email: 'adriano@teste.com',
    senha: 'Teste123pi'
  };

  beforeAll(async () => {
    await db.sync();
    // Garante que o usuário não existe antes de testar
    await db.models.Usuario.destroy({ where: { email: usuarioValido.email } });
    console.log('🚀 Banco de dados sincronizado e usuário removido para os testes de aceitação!');
  });

  afterAll(async () => {
    await db.close();
    console.log('🔒 Conexão com o banco de dados encerrada após os testes.');
  });

  test('✅ Usuário deve conseguir se cadastrar com sucesso', async () => {
    const res = await request(app)
      .post('/api/cadastro')
      .send(usuarioValido);

    // Log da resposta para debug
    console.log('Resposta do cadastro:', res.statusCode, res.body);

    expect(res.statusCode).toBe(201);
    expect(res.body).toHaveProperty('success', true);
    expect(res.body).toHaveProperty('message', 'Usuário cadastrado com sucesso!');
    expect(res.body).toHaveProperty('user');
    expect(res.body.user).toHaveProperty('id');
    expect(res.body.user).toHaveProperty('nome', usuarioValido.nome);
    expect(res.body.user).toHaveProperty('email', usuarioValido.email);

    console.log('🎉 Cadastro realizado com sucesso para o usuário Adriano!');
  });

  test('❌ Cadastro deve falhar com senha fraca', async () => {
    const res = await request(app)
      .post('/api/cadastro')
      .send({
        nome: 'Adriano',
        email: 'outra@teste.com',
        senha: '123'
      });

    expect(res.statusCode).toBe(400);
    expect(res.body).toHaveProperty('message');
    expect(res.body.message).toMatch(/Senha inválida/);

    console.log('🛡️ O sistema protegeu contra uma senha fraca com sucesso!');
  });
});
