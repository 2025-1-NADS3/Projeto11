<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
const cadastrarUsuario = async (nome, email, senha) => {
    if (!validarNome(nome)) {
        throw new Error("Nome inválido. Deve conter pelo menos 2 caracteres e apenas letras e espaços.");
    }
    if (!validarEmail(email)) {
        throw new Error("Email inválido. Use o formato usuario@dominio.com.");
    }
    const userExistente = await User.findOne({ where: { email } });
    if (userExistente) {
        throw new Error("Este e-mail já está em uso. Tente outro.");
    }
    if (!validarSenha(senha)) {
        throw new Error("Senha inválida. Deve ter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma minúscula e um número, sem espaços.");
    }

    const hashedSenha = await bcrypt.hash(senha, 10);
    return await User.create({ nome, email, senha: hashedSenha });
};

const listarUsuarios = async () => {
    return await User.findAll();
};

const updateUser = async (id, nome, senha) => {
    try {
        const user = await User.findByPk(id);
        if (!user) {
            throw new Error("Usuário não encontrado.");
        }
        
        if (nome && !validarNome(nome)) {
            throw new Error("Nome inválido.");
        }
        if (senha && !validarSenha(senha)) {
            throw new Error("Senha inválida.");
        }
        
        const hashedSenha = senha ? await bcrypt.hash(senha, 10) : user.senha;

        await user.update({ nome, senha: hashedSenha });
        return user;
    } catch (error) {
        throw new Error(error.message);
    }
};

const deleteUser = async (id, senha) => {
    try {
        const user = await User.findByPk(id);
        if (!user) {
            throw new Error("Usuário não encontrado.");
        }

        if (!(await bcrypt.compare(senha, user.senha))) {
            throw new Error("Senha incorreta. Não foi possível excluir a conta.");
        }

        await user.destroy();
        return { message: "Conta deletada com sucesso." };
    } catch (error) {
        throw new Error(error.message);
=======
const cadastrarUsuario = async (nome, email, senha) => {
    if (!validarNome(nome)) {
        throw new Error("Nome inválido. Deve conter pelo menos 2 caracteres e apenas letras e espaços.");
    }
    if (!validarEmail(email)) {
        throw new Error("Email inválido. Use o formato usuario@dominio.com.");
    }
    const userExistente = await User.findOne({ where: { email } });
    if (userExistente) {
        throw new Error("Este e-mail já está em uso. Tente outro.");
    }
    if (!validarSenha(senha)) {
        throw new Error("Senha inválida. Deve ter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma minúscula e um número, sem espaços.");
    }

    const hashedSenha = await bcrypt.hash(senha, 10);
    return await User.create({ nome, email, senha: hashedSenha });
};

const listarUsuarios = async () => {
    return await User.findAll();
};

const updateUser = async (id, nome, senha) => {
    try {
        const user = await User.findByPk(id);
        if (!user) {
            throw new Error("Usuário não encontrado.");
        }
        
        if (nome && !validarNome(nome)) {
            throw new Error("Nome inválido.");
        }
        if (senha && !validarSenha(senha)) {
            throw new Error("Senha inválida.");
        }
        
        const hashedSenha = senha ? await bcrypt.hash(senha, 10) : user.senha;

        await user.update({ nome, senha: hashedSenha });
        return user;
    } catch (error) {
        throw new Error(error.message);
    }
};

const deleteUser = async (id, senha) => {
    try {
        const user = await User.findByPk(id);
        if (!user) {
            throw new Error("Usuário não encontrado.");
        }

        if (!(await bcrypt.compare(senha, user.senha))) {
            throw new Error("Senha incorreta. Não foi possível excluir a conta.");
        }

        await user.destroy();
        return { message: "Conta deletada com sucesso." };
    } catch (error) {
        throw new Error(error.message);
>>>>>>> 24f8c0a2e1978559817d6f11d521faa44aec8845
=======
const cadastrarUsuario = async (nome, email, senha) => {
    if (!validarNome(nome)) {
        throw new Error("Nome inválido. Deve conter pelo menos 2 caracteres e apenas letras e espaços.");
    }
    if (!validarEmail(email)) {
        throw new Error("Email inválido. Use o formato usuario@dominio.com.");
    }
    const userExistente = await User.findOne({ where: { email } });
    if (userExistente) {
        throw new Error("Este e-mail já está em uso. Tente outro.");
    }
    if (!validarSenha(senha)) {
        throw new Error("Senha inválida. Deve ter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma minúscula e um número, sem espaços.");
    }

    const hashedSenha = await bcrypt.hash(senha, 10);
    return await User.create({ nome, email, senha: hashedSenha });
};

const listarUsuarios = async () => {
    return await User.findAll();
};

const updateUser = async (id, nome, senha) => {
    try {
        const user = await User.findByPk(id);
        if (!user) {
            throw new Error("Usuário não encontrado.");
        }
        
        if (nome && !validarNome(nome)) {
            throw new Error("Nome inválido.");
        }
        if (senha && !validarSenha(senha)) {
            throw new Error("Senha inválida.");
        }
        
        const hashedSenha = senha ? await bcrypt.hash(senha, 10) : user.senha;

        await user.update({ nome, senha: hashedSenha });
        return user;
    } catch (error) {
        throw new Error(error.message);
    }
};

const deleteUser = async (id, senha) => {
    try {
        const user = await User.findByPk(id);
        if (!user) {
            throw new Error("Usuário não encontrado.");
        }

        if (!(await bcrypt.compare(senha, user.senha))) {
            throw new Error("Senha incorreta. Não foi possível excluir a conta.");
        }

        await user.destroy();
        return { message: "Conta deletada com sucesso." };
    } catch (error) {
        throw new Error(error.message);
>>>>>>> 24f8c0a2e1978559817d6f11d521faa44aec8845
=======
const cadastrarUsuario = async (nome, email, senha) => {
    if (!validarNome(nome)) {
        throw new Error("Nome inválido. Deve conter pelo menos 2 caracteres e apenas letras e espaços.");
    }
    if (!validarEmail(email)) {
        throw new Error("Email inválido. Use o formato usuario@dominio.com.");
    }
    const userExistente = await User.findOne({ where: { email } });
    if (userExistente) {
        throw new Error("Este e-mail já está em uso. Tente outro.");
    }
    if (!validarSenha(senha)) {
        throw new Error("Senha inválida. Deve ter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma minúscula e um número, sem espaços.");
    }

    const hashedSenha = await bcrypt.hash(senha, 10);
    return await User.create({ nome, email, senha: hashedSenha });
};

const listarUsuarios = async () => {
    return await User.findAll();
};

const updateUser = async (id, nome, senha) => {
    try {
        const user = await User.findByPk(id);
        if (!user) {
            throw new Error("Usuário não encontrado.");
        }
        
        if (nome && !validarNome(nome)) {
            throw new Error("Nome inválido.");
        }
        if (senha && !validarSenha(senha)) {
            throw new Error("Senha inválida.");
        }
        
        const hashedSenha = senha ? await bcrypt.hash(senha, 10) : user.senha;

        await user.update({ nome, senha: hashedSenha });
        return user;
    } catch (error) {
        throw new Error(error.message);
    }
};

const deleteUser = async (id, senha) => {
    try {
        const user = await User.findByPk(id);
        if (!user) {
            throw new Error("Usuário não encontrado.");
        }

        if (!(await bcrypt.compare(senha, user.senha))) {
            throw new Error("Senha incorreta. Não foi possível excluir a conta.");
        }

        await user.destroy();
        return { message: "Conta deletada com sucesso." };
    } catch (error) {
        throw new Error(error.message);
>>>>>>> adb3d22f11d0707235ca6947edc7e8e370eea507
    }