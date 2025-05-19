const bcrypt = require('bcryptjs');

// Mock do model User
jest.mock('../../models/user.js', () => ({
  findByPk: jest.fn((id) => {
    if (id === 1) {
      return {
        id: 1,
        nome: 'Antigo Nome',
        senha: 'senhaAntigaHash',
        update: jest.fn(function (dados) {
          this.nome = dados.nome;
          this.senha = dados.senha;
          return this;
        }),
      };
    }
    return null;
  }),
}));

const userService = require('../../Services/userServices');

describe('updateUser - atualização simples', () => {
  it('deve atualizar nome e senha de um usuário existente', async () => {
    const id = 1;
    const novoNome = 'Novo Nome';
    const novaSenha = 'NovaSenha123';

    const resultado = await userService.updateUser(id, novoNome, novaSenha);

    expect(resultado).toHaveProperty('nome', novoNome);
    expect(resultado).toHaveProperty('senha');
    expect(typeof resultado.senha).toBe('string');
    expect(resultado.senha).not.toBe('NovaSenha123'); // deve estar criptografada
  });
});
