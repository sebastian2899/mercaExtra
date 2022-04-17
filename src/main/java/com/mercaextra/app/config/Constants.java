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
    public static final String RESTAR_PRODUCTOS_VENDIDOS = "UPDATE Producto set cantidad = cantidad-:cantidad WHERE id=:id";

    private Constants() {}
}
