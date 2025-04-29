
import eventoService from "../services/eventoService.js";

const listarEventos = async (req, res) => {
    try {
        const resultado  = await eventoService.listarEventos();
        return res.json(resultado);
    } catch(error){
        console.log(error);
        return res.status(500).json({ error: "Erro ao buscar eventos: " + error.message });
}
}

export default {
    listarEventos
}