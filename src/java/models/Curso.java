package models;

public class Curso {

    private int idCurso;
    private String descripcion;

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Curso(int idCurso, String descripcion) {
        this.idCurso = idCurso;
        this.descripcion = descripcion;
    }

}
