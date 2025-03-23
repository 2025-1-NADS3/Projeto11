const userService = require('../services/userService');

exports.cadastro = async (req, res) => {
    try {
        const { nome, email, senha } = req.body;
        const user = await userService.cadastrarUsuario(nome, email, senha);
        res.status(201).json({ message: "Usuário cadastrado com sucesso!", user });
    } catch (error) {
        res.status(400).json({ error: error.message });
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
