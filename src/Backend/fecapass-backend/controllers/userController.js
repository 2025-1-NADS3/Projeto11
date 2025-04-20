const userService = require("../services/userService");

exports.cadastro = async (req, res) => {
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


exports.login = async (req, res) => {
  try {
    const { email, senha } = req.body;
    const token = await userService.loginUsuario(email, senha);
    res.json({ message: "Login realizado com sucesso!", token });
  } catch (error) {
    res.status(401).json({ error: error.message });
  }
};

exports.getUsers = async (req, res) => {
  try {
    const users = await userService.listarUsuarios();
    res.json(users);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.updateUser = async (req, res) => {
  try {
    const { id } = req.params;
    const { nome, senha } = req.body;
    const user = await userService.updateUser(id, nome, senha);
    res.json({ message: "Usuário atualizado com sucesso!", user });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

exports.deleteUser = async (req, res) => {
  try {
    const { id } = req.params;
    const { senha } = req.body;
    const message = await userService.deleteUser(id, senha);
    res.json(message);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};
