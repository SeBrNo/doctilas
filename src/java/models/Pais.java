package models;

public class Pais {
    private int idPais;
    private String descripcion;

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Pais(int idPais, String descripcion) {
        this.idPais = idPais;
        this.descripcion = descripcion;
    }
}
