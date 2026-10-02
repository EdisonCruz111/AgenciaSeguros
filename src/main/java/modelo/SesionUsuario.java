/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author deino
 */

public class SesionUsuario {
    private static Usuario usuarioLogueado;

    public static void setUsuario(Usuario u) {
        usuarioLogueado = u;
    }

    public static Usuario getUsuario() {
        return usuarioLogueado;
    }

    public static void cerrarSesion() {
        usuarioLogueado = null;
    }

    public static boolean haySesionActiva() {
        return usuarioLogueado != null;
    }
}
