import request from 'supertest';
import app from '../../app.js';
import * as userService from '../../Services/userServices.js';

jest.mock('../../Services/userServices.js'); // Mocka todas as funções do service

describe('Integração: Login com sucesso', () => {
  it('deve fazer login e retornar um token JWT', async () => {
    // Define o valor que o mock deve retornar
    userService.loginUsuario.mockResolvedValue({ token: 'token.jwt.falso' });

    // Faz a requisição
    const response = await request(app)
      .post('/login')
      .send({ email: 'teste@email.com', senha: '123456' });

    // Verifica se a função mockada foi chamada com os argumentos corretos
    expect(userService.loginUsuario).toHaveBeenCalledWith('teste@email.com', '123456');

    // Verifica a resposta
    expect(response.status).toBe(200);
    expect(response.body).toHaveProperty('token');
    expect(response.body.token).toBe('token.jwt.falso');
  });
});
