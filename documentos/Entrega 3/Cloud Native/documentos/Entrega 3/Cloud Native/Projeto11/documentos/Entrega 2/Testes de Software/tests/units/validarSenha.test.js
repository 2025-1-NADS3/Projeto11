const { validarSenha } = require('../../Services/userServices');

describe('validação de senha com lançamento de erro', () => {
  // Função auxiliar só para teste, que usa a validarSenha original
  const verificarSenhaOuErro = (senha) => {
    if (!validarSenha(senha)) {
      throw new Error('Senha inválida');
    }
    return true;
  };

  it('deve lançar erro se a senha for inválida', () => {
    expect(() => verificarSenhaOuErro('senhaFraca')).toThrow('Senha inválida');
  });

  it('deve retornar true se a senha for válida', () => {
    expect(verificarSenhaOuErro('Senha123')).toBe(true);
  });
});
