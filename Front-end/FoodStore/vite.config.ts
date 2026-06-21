import { defineConfig } from "vite";
import { resolve } from "path";

export default defineConfig({
  build: {
    rollupOptions: {
      input: {
        index: resolve(__dirname, "index.html"),
        login: resolve(__dirname, "src/pages/auth/login/login.html"),
        registro: resolve(__dirname, "src/pages/auth/registro/registro.html"),
        storeHome: resolve(__dirname, "src/pages/store/home/home.html"),
        storeCart: resolve(__dirname, "src/pages/store/cart/cart.html"),
        storeProductDetail: resolve(__dirname, "src/pages/store/productDetail/productDetail.html"),
        clientOrders: resolve(__dirname, "src/pages/client/orders/orders.html"),
        adminCategories: resolve(__dirname, "src/pages/admin/home/categories/categories.html"),
        adminProductsList: resolve(__dirname, "src/pages/admin/home/products/products.html"),
        adminOrders: resolve(__dirname, "src/pages/admin/home/orders/orders.html"),
        adminHome: resolve(__dirname, "src/pages/admin/home/adminHome/adminHome.html"),
      },
    },
  },
  base: "./",
});