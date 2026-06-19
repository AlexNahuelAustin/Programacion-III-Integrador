import type { IProducto } from "../../../types/IProducto";
import type { ICategoria } from "../../../types/ICategoria";
import { checkAuhtUser, logout } from "../../../utils/auth";
import productosData from "../../../data/productos.json";
import categoriasData from "../../../data/categorias.json";

// Proteger ruta - solo USUARIO
checkAuhtUser(
  "/src/pages/auth/login/login.html",
  "/src/pages/client/home/home.html",
  "USUARIO",
);

const productos: IProducto[] = productosData;
const categorias: ICategoria[] = categoriasData;

// Logout
document.getElementById("logoutButton")?.addEventListener("click", logout);

// renderizar categorias
const cargarCategorias = () => {
  const ul = document.querySelector<HTMLLIElement>("ul");

  const li = document.createElement("li");
  li.innerHTML = `<a href="# class="list-categorias>todos los productos</a>`;

  li.addEventListener("click", (e) => {
    e.preventDefault();
    cargarProductos(productos);
  });
  ul?.appendChild(li);

  categorias.forEach((categoria) => {
    const li = document.createElement("li");
    li.innerHTML = `<a href="#" class="list-categoria">${categoria.nombre}</a>`;
    li.addEventListener("click", (e) => {
      e.preventDefault();
      const filtrados = productos.filter(
        (p) => p.categoria.id === categoria.id,
      );
      cargarProductos(filtrados);
    });
    ul?.appendChild(li);
  });
};

// Renderizar productos
const cargarProductos = (lista: IProducto[]) => {
  const contenedor = document.querySelector<HTMLDivElement>(
    "#contenedor-pruductos",
  );
  if (!contenedor) return;
  contenedor.innerHTML = "";

  lista.forEach((producto) => {
    const card = document.createElement("article");
    card.className = "producto-card";
    card.innerHTML = `<img class="comidas" src="${producto.imagen} alt="${producto.nombre}">
    <h3>${producto.nombre}</h3>
    <p>${producto.descripcion}</p>
    <p>precio: <strong>$${producto.precio.toLocaleString("es-AR")}</strong></p>
     <span class="${producto.disponible ? "producto--disponible" : "producto--nodisponible"}">
    ${producto.disponible ? "producto--disponible" : "producto--nodisponible"}
    </span>
    <button class""btn">+ Agregar</button>
     `;
    contenedor.appendChild(card);
card.querySelector<HTMLBRElement>("button")?.addEventListener("click",()=>){
    //agregarAlCarrito(producto);    
}
  });
};

//Buscador
document.querySelector<HTMLInputElement>(#buscador)?.addEventListener("input",e =>{
    const texto = ( e.target as HTMLInputElement).value.toLocaleLowerCase();
    const filtrado = productos.filter(producto => producto.nombre.toLowerCase().includes(texto))
    cargarProductos(filtrado);
})

// Carrito (terminar), despues de ingles y luego seguir
const agregarAlCarrito = (producto: IProducto)=>{
const carrito = JSON.parse(localStorage.getItem("carrito")||"[]");
const existe = carrito.find((item:IProducto)=> item.id === producto.id);
}

