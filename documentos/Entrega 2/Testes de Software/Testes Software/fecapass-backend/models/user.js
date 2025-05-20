const User = {
  findOne: async () => null,
  findByPk: async () => null,
  findAll: async () => [],
  create: async ({ nome, email, senha }) => ({ id: 1, nome, email, senha }),
};

export default User;
