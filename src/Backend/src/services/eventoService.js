import Evento from "../models/evento.js";

 const listarEventos = async () => {
    try {
        const eventos = await Evento.findAll();
        return eventos;
    } catch (error) {
        throw new Error("Erro ao buscar eventos: " + error.message);
    }
};

export default {
    listarEventos
}