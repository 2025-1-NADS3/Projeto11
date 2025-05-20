const userService = require('../../Services/userServices');

// Mock do model User
jest.mock('../../models/user.js', () => ({
  findOne: jest.fn(({ where }) => {
    // Simula que o e-mail já está em uso
    if (where.email === 'jaexiste@email.com') {
      return {
        id: 1,
        nome: 'Adriano',
        email: 'jaexiste@email.com',
        senha: 'senhaQualquerHash',
      };
    }
    return null;
  }),
  create: jest.fn((dados) => ({
    id: 2,
    ...dados,
  })),
}));

describe('cadastrarUsuario - email em uso', () => {
  it('deve lançar erro se o e-mail já estiver em uso', async () => {
    const nome = 'Adriano';
    const email = 'jaexiste@email.com';
    const senha = 'Testepi123';

await expect(userService.cadastrarUsuario(nome, email, senha))
  .rejects
  .toThrow('Este e-mail já está em uso. Tente outro.');
  });
});
