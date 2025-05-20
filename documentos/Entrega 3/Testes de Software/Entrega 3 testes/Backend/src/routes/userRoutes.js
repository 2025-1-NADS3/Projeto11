const express = require('express');
const { cadastro, login, getUsers, updateUser, deleteUser } = require('../controllers/userController');
const router = express.Router();

router.post('/cadastro', cadastro);
router.post('/login', login);
router.get('/users', getUsers);
router.put('/users/:id', updateUser);
router.delete('/users/:id', deleteUser);

module.exports = router;
