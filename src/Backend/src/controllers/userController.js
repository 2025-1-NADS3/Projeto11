import userService from "../services/userService.js";

 const cadastro = async (req, res) => {
  try {
    const { nome, email, senha } = req.body;
    const user = await userService.cadastrarUsuario(nome, email, senha);
    
    return res.status(201).json({
      success: true,
      message: "Usuário cadastrado com sucesso!",
      user
    });
  } catch (error) {
    return res.status(400).json({
      success: false,
      message: error.message
    });
  }
};

 const login= async (req, res) => {
  try {
      const { email, senha } = req.body;
      const user = await userService.loginUsuario(email, senha);
      res.json({ message: "Login realizado com sucesso!", token: user, userId: user.id });
  } catch (error) {
      res.status(401).json({ error: error.message });
  }
};

 const getUsers = async (req, res) => {
  try {
    const users = await userService.listarUsuarios();
    res.json(users);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

 const updateUser = async (req, res) => {
  try {
    const { id } = req.params;
    const { nome, senha } = req.body;
    const user = await userService.updateUser(id, nome, senha);
    res.json({ message: "Usuário atualizado com sucesso!", user });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

 const deleteUser = async (req, res) => {
  try {
    const { id } = req.params;
    const { senha } = req.body;
    const message = await userService.deleteUser(id, senha);
    res.json(message);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

export default {
  cadastro,
  login,
  deleteUser,
  updateUser,
  getUsers
};