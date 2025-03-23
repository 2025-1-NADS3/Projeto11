const express = require('express');
const userController = require('../controllers/userController'); // Corrija a importação
const router = express.Router();

router.post('/cadastro', userController.cadastro); // Alterei 'register' para 'cadastro'
router.post('/login', userController.login);
router.get('/users', userController.getUsers);

module.exports = router;
