jest.mock('../../models/user.js', () => ({
  findOne: jest.fn(({ where }) => null), // simula usuário não encontrado
}));

const userService = require('../../Services/userServices');

describe('loginUsuario (email inválido)', () => {
  it('deve lançar erro ao tentar login com e-mail inexistente', async () => {
    const email = 'naoexiste@email.com';
    const senha = 'qualquerSenha';

    await expect(userService.loginUsuario(email, senha)).rejects.toThrow('Credenciais inválidas');
  });
});
