const authMiddleware = (req, res, next) => {
  const authHeader = req.headers.authorization;

  console.log('Authorization Header:', authHeader); // Log para depuração

  if (!authHeader || !authHeader.startsWith("Bearer ")) {
      return res.status(401).json({ error: "Token não fornecido" });
  }

  const token = authHeader.split(" ")[1];

  try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      console.log('Token decodificado:', decoded); // Log para depuração
      req.usuario = decoded;
      next();
  } catch (erro) {
      console.log('Erro ao verificar token:', erro.message); // Log para depuração
      return res.status(403).json({ erro: "Token inválido" });
  }
};

export default {
  authMiddleware
}