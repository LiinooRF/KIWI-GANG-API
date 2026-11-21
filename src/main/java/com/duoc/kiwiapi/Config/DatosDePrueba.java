package com.duoc.kiwiapi.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.duoc.kiwiapi.Model.CategoriaModel;
import com.duoc.kiwiapi.Model.ProductoModel;
import com.duoc.kiwiapi.Repository.CategoriaRepository;
import com.duoc.kiwiapi.Service.ProductoService;

import com.duoc.kiwiapi.Model.UsuarioModel;
import com.duoc.kiwiapi.Service.UsuarioService;

//Carga los usuarios de prueba cuando la base esta vacia.
//Sirve para poder probar el login apenas se levanta la api, porque la base parte vacia.
@Configuration
public class DatosDePrueba {

    @Bean
    CommandLineRunner cargarUsuarios(UsuarioService usuarioService) {
        return args -> {
            if (usuarioService.obtenerTodos().isEmpty()) {
                //Son los mismos que muestra la pagina de login del front.
                crear(usuarioService, "112223334", "Rodrigo", "Fuentes", "rodrigofuentes@duoc.cl", "admin123", "Santiago", "administrador");
                crear(usuarioService, "445556667", "Javiera", "Diaz", "javieradiaz@duoc.cl", "oper123", "Nuñoa", "vendedor");
                crear(usuarioService, "123456789", "Catalina", "Soto", "catalinasoto@gmail.com", "ctr123", "Providencia", "cliente");
                crear(usuarioService, "987654321", "Diego", "Lopez", "diegolopez@gmail.com", "ctr456", "Las Condes", "cliente");
            }
        };
    }

    private void crear(UsuarioService servicio, String run, String nombre, String apellidos,
                       String correo, String contrasena, String comuna, String rol) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setRun(run);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setComuna(comuna);
        usuario.setRol(rol);
        //guardar encripta la clave con bcrypt
        servicio.guardar(usuario);
    }

 // carga las categorias y los 8 servicios del frontend si todavia no hay categorias
    @Bean
    CommandLineRunner cargarProductos(CategoriaRepository categoriaRepository, ProductoService productoService) {
        return args -> {
            if (categoriaRepository.count() == 0) {
                // las categorias van primero porque los productos las necesitan
                CategoriaModel fragil = categoriaRepository.save(new CategoriaModel(null, "Frágil", "Servicios para paquetes que necesitan manejo cuidadoso."));
                CategoriaModel noFragil = categoriaRepository.save(new CategoriaModel(null, "No Frágil", "Servicios para paquetes de manejo estándar."));

                // los mismos datos que productos.js del frontend
                crearProducto(productoService, "KX-ENV-01", "Envío estándar", "Entrega en 3 a 5 días hábiles dentro de la Región Metropolitana.", 3990, 50, 10, "/img/envio-estandar.png", false, 0, noFragil);
                crearProducto(productoService, "KX-ENV-02", "Envío express", "Entrega al día siguiente en las comunas de la Región Metropolitana.", 6990, 30, 10, "/img/envio-estandar.png", true, 5490, noFragil);
                crearProducto(productoService, "KX-FRA-01", "Envío de paquete frágil", "Embalaje reforzado y manejo cuidadoso para vidrios, cerámica y electrónica.", 8990, 20, 5, "/img/envio-estandar.png", false, 0, fragil);
                crearProducto(productoService, "KX-EMB-01", "Caja de embalaje mediana", "Caja de cartón corrugado de 30x30x30 cm para preparar tu envío.", 1990, 120, 25, "/img/caja-mediana.jpg", false, 0, noFragil);
                crearProducto(productoService, "KX-EMB-02", "Kit de embalaje frágil", "Incluye burbuja, relleno y cinta de advertencia para paquetes delicados.", 4490, 8, 15, "/img/kit-fragil.jpg", true, 3490, fragil);
                crearProducto(productoService, "KX-SEG-01", "Seguro de envío", "Cubre el valor declarado del paquete hasta 400.000 pesos.", 2990, 100, 20, "/img/seguro.png", false, 0, noFragil);
                crearProducto(productoService, "KX-ENV-03", "Retiro a domicilio", "Un operario retira el paquete en la dirección que nos indiques.", 2490, 40, 10, "/img/domicilio.png", false, 0, noFragil);
                crearProducto(productoService, "KX-FRA-02", "Envío frágil express", "Entrega al día siguiente con embalaje reforzado para paquetes delicados.", 11990, 4, 8, "/img/envio-estandar.png", true, 9990, fragil);
            }
        };
    }

    private void crearProducto(ProductoService servicio, String codigo, String nombre, String descripcion, int precio, int stock, int stockCritico, String imagen, boolean enOferta, int precioOferta, CategoriaModel categoria) {
        ProductoModel producto = new ProductoModel();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setStockCritico(stockCritico);
        producto.setImagen(imagen);
        producto.setEnOferta(enOferta);
        producto.setPrecioOferta(precioOferta);
        producto.setCategoria(categoria);
        // guardar revisa que el codigo no se repita y que la categoria exista
        servicio.guardar(producto);
    }
}
