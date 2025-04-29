import express from "express";
import userController from "../controllers/userController.js";


const router = express.Router();

router.post('/cadastro', userController.cadastro);
router.post('/login', userController.login);
router.get('/users', userController.getUsers);
router.put('/users/:id', userController.updateUser);
router.delete('/users/:id', userController.deleteUser);

export default router;
