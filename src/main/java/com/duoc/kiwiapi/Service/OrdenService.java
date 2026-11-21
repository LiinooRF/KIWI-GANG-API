package com.duoc.kiwiapi.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.duoc.kiwiapi.Model.DetalleOrdenModel;
import com.duoc.kiwiapi.Model.OrdenModel;
import com.duoc.kiwiapi.Model.ProductoModel;
import com.duoc.kiwiapi.Model.UsuarioModel;
import com.duoc.kiwiapi.Repository.OrdenRepository;
import com.duoc.kiwiapi.Repository.ProductoRepository;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    // se usa directo el repositorio para buscar y guardar el stock nuevo
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<OrdenModel> obtenerTodas() {
        return ordenRepository.findAll();
    }

    public OrdenModel obtenerPorId(Long id) {
        return ordenRepository.findById(id).orElse(null);
    }

    // transactional hace que todo se guarde junto o nada
    // si un producto no tiene stock se deshace lo que ya se habia descontado
    @Transactional
    public OrdenModel guardar(OrdenModel orden) {
        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La orden debe tener al menos un producto");
        }

        int total = 0;

        for (DetalleOrdenModel detalle : orden.getDetalles()) {
            if (detalle.getCantidad() == null || detalle.getCantidad() < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0");
            }
            if (detalle.getProducto() == null || detalle.getProducto().getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada detalle debe tener un producto");
            }

            ProductoModel producto = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
            if (producto == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto " + detalle.getProducto().getId() + " no existe");
            }

            // regla de negocio, no se puede vender mas de lo que hay
            if (detalle.getCantidad() > producto.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay stock suficiente de " + producto.getNombre());
            }

            // regla de negocio, se descuenta el stock vendido
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            // si esta en oferta se cobra el precio de oferta, igual que en el carrito del frontend
            //si esta en oferta se cobra el precio de oferta, el TRUE.equals es por si viene null
            int precio = Boolean.TRUE.equals(producto.getEnOferta()) ? producto.getPrecioOferta() : producto.getPrecio();

            detalle.setProducto(producto);
            detalle.setPrecioUnitario(precio);
            detalle.setSubtotal(precio * detalle.getCantidad());
            // el detalle tiene que saber a que orden pertenece para guardar el orden_id
            detalle.setOrden(orden);

            // regla de negocio, el total es la suma de los subtotales
            total = total + detalle.getSubtotal();
        }

        orden.setTotal(total);
        orden.setFecha(LocalDateTime.now());
        // codigo simple con la hora en milisegundos, asi no se repite
        orden.setCodigo("KX-ORD-" + System.currentTimeMillis());
        orden.setUsuario(usuarioActual());

        // por el cascade se guardan tambien los detalles
        return ordenRepository.save(orden);
    }

    // busca al usuario que hizo la compra usando el token
    // el filtro jwt de lino deja el correo del usuario en el contexto de seguridad
    private UsuarioModel usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioModel usuario = usuarioService.obtenerPorCorreo(auth.getName());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El usuario de la sesion no existe");
        }
        return usuario;
    }
}