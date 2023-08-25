package models;

public class CuestionarioPregunta {
    private int idCuestionarioPregtuna;
    private int idCuestionario;
    private int idPregunta;
    private String descripcion;

    public int getIdCuestionarioPregtuna() {
        return idCuestionarioPregtuna;
    }

    public void setIdCuestionarioPregtuna(int idCuestionarioPregtuna) {
        this.idCuestionarioPregtuna = idCuestionarioPregtuna;
    }

    public int getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(int idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
