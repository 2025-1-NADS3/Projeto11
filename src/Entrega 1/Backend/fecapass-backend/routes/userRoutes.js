const express = require('express');
const userController = require('../controllers/userController'); 
const router = express.Router();
const jwt = require('jsonwebtoken'); // Esse pode ser removido se você não for mais usar

router.post('/cadastro', userController.cadastro); 
router.post('/login', userController.login);

router.put('/users/:id', userController.updateUser);
router.delete('/users/:id', userController.deleteUser);

router.get('/users', userController.getUsers);

module.exports = router;

