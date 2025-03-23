const express = require('express');
const userController = require('../controllers/userController'); 
const router = express.Router();

router.post('/cadastro', userController.cadastro); 
router.post('/login', userController.login);
router.get('/users', userController.getUsers);

module.exports = router;
