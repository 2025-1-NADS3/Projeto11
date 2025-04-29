import User from "./user.js";
import Evento from "./evento.js";
import Pagamento from "./pagamento.js";

User.hasMany(Pagamento, { foreignKey: "usuarioId" });
Pagamento.belongsTo(User, { foreignKey: "usuarioId" });
Evento.hasMany(Pagamento, { foreignKey: "eventoId" });
Pagamento.belongsTo(Evento, { foreignKey: "eventoId" });

export const relacionamento = { User, Evento, Pagamento };