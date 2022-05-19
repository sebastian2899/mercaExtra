package com.mercaextra.app.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "es";

    //PRODUCTO
    public static final String PRODUCTO_BASE = "SELECT p FROM Producto p WHERE p.id IS NOT NULL";
    public static final String PRODUCTO_NOMBRE = " AND UPPER(p.nombre) LIKE :nombre";
    public static final String PRODUCTO_DESCRIPCION = " AND UPPER(p.descripcion) LIKE :descripcion";
    public static final String PRODUCTOS_CATEGORIA = "SELECT p FROM Producto p WHERE p.categoria = :categoria";
    public static final String PRODUCTOS_ORDENADOS_PRECIO = "SELECT p FROM Producto p ORDER BY p.precio DESC";
    public static final String PRODUCTOS_ORDENADOS_PRECIO_CATEGORIA =
        "SELECT p FROM Producto p WHERE p.categoria =:categoria" + " ORDER BY p.precio DESC";
    public static final String PRODUCTOS_ORDENADOS_ALFABETICAMENTE = "SELECT p FROM Producto p ORDER BY p.nombre ASC";
    public static final String PRODUCTOS_ORDENAOS_ALFABETICAMENTE_CATEGORIA =
        "SELECT p FROM Producto p WHERE p.categoria =:categoria" + " ORDER BY p.nombre ASC";

    public static final String RESTAR_PRODUCTOS_VENDIDOS = "UPDATE Producto SET cantidad=cantidad-:cantidad WHERE id=:id";

    //FACTURA
    public static final String TRAER_FACTURA_POR_USUARIO = "SELECT f FROM Factura f WHERE f.userName =:userName AND f.id=:id";
    public static final String TRAER_FACTURAS_POR_USUARIO = "SELECT f FROM Factura f WHERE f.userName =:userName";

    //CONSULTAR LOGIN
    public static final String TRAER_PEDIDOS_POR_USUARIO =
        "SELECT p.fechaPedido,p.direccion,p.infoDomicilio,p.id FROM Pedido p" + " WHERE p.userName = :userName";

    //PEDIDO
    public static final String CAMBIAR_ESTADO_PEDIDO_EXPIRADO = "UPDATE Pedido SET estado='Expirado' WHERE id=:id";

    // DOMICILIARY
    public static final String BASE_DOMICILIARY = "SELECT d FROM Domiciliario d WHERE d.id IS NOT NULL";
    public static final String DOMICILIARY_PER_NAME = " AND UPPER(d.nombre) LIKE :nombre";
    public static final String DOMICILIARY_PER_TYPE_SALARY = " AND d.salario = :tipoSalario";
    public static final String DOMICILIARY_PER_SHEDULED = " AND d.horario LIKE :horario";
    public static final String DOMICILIARY_PER_STATE = " AND d.estado = :estado";

    // COMPRA
    public static final String INCREASE_AMOUNT_PRODUCTS = "UPDATE FROM Producto SET cantidad = cantidad+:cantidad WHERE id=:id";
    public static final String BASE_COMPRA = "SELECT c FROM Compra c WHERE c.id IS NOT NULL";
    public static final String COMPRA_TIPO_FACTURA = " AND c.tipoFactura = :tipoFactura";
    public static final String COMPRA_ESTADO = " AND c.estado = :estado";
    public static final String COMPRA_PROVEEDOR = " AND c.idProveedor =:idProveedor";
    public static final String COMPRA_NUMERO_FACTURA = " AND c.numeroFactura =:numeroFactura";
    public static final String COMPRA_FECHA = " AND DATE_FORMAT(c.fechaCreacion, '%Y-%m-%d') =:fechaCreacion";

    private Constants() {}
}
