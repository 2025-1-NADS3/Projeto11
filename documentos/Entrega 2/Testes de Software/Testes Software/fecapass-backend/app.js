import express from 'express';
import bodyParser from 'body-parser';
import * as userController from './controllers/userController.js';

const app = express();

app.use(bodyParser.json());

app.post('/cadastro', userController.cadastro);
app.post('/login', userController.login);
app.get('/users', userController.getUsers);
app.put('/update/:id', userController.updateUser);
app.delete('/delete/:id', userController.deleteUser);

export default app;  
