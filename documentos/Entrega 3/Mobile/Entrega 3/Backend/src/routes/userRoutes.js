import express from "express";
import { cadastro, login, getUsers, updateUser, deleteUser } from "../controllers/userController.js";

const router = express.Router();

router.post('/cadastro', cadastro);
router.post('/login', login);
router.get('/users', getUsers);
router.put('/users/:id', updateUser);
router.delete('/users/:id', deleteUser);

export default router;
