package modelo;

public class ComboItem {
    private int id;
    private String texto;

    public ComboItem(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public String toString() {
        return texto; 
    }
}