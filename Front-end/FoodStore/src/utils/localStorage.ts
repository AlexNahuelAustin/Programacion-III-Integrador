import type { IUser } from "../types/IUser";

// Guardar usuario en localStorage
export const saveUser = (user: IUser) => {
  const parseUser = JSON.stringify(user);
  localStorage.setItem("userData", parseUser);
};

// Obtener usuario de localStorage
export const getUSer = () => {
  return localStorage.getItem("userData");
};

// Eliminar usuario de localStorage
export const removeUser = () => {
  localStorage.removeItem("userData");
};