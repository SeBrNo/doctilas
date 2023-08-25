package beans;

import dao.conexion;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import models.Archivo;
import models.Asignatura;
import models.Ciudad;
import models.Comuna;
import models.Curso;
import models.Pais;

@ManagedBean(name = "dropdownBean")
@SessionScoped
public class dropdownBean {

    private Ciudad cid;
    private Comuna com;
    private Asignatura asi;
    private Archivo arc;
    private List<Pais> paises = new ArrayList<>();
    private List<Ciudad> ciudades = new ArrayList<>();
    private List<Comuna> comunas = new ArrayList<>();
    private List<Archivo> archivos = new ArrayList<>();
    private String paisSeleccionado;
    private String ciudadSeleccionada;
    private String comunaSeleccionada;
    CallableStatement stm;
    ResultSet rs;

    conexion conexiones = new conexion();

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public String getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(String ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public void recargarCiudad() throws Exception {
        if (paisSeleccionado != null) {
            ciudades = obtenerCiudades(paisSeleccionado);
        }
    }

    public List<Comuna> getComunas() {
        return comunas;
    }

    public String getComunaSeleccionada() {
        return comunaSeleccionada;
    }

    public String getPaisSeleccionado() {
        return paisSeleccionado;
    }

    public void setPaisSeleccionado(String paisSeleccionado) {
        this.paisSeleccionado = paisSeleccionado;
    }

    public List<Pais> getLstPais() throws Exception {
        comunas.clear();
        ciudades.clear();
        paises.clear();
        paises.add(new Pais(0, "Seleccione un pais"));
        cid = new Ciudad();
        cid.setIdCiudad(0);
        cid.setDescripcion("Seleccione una ciudad");
        cid.setIdPais(0);
        ciudades.add(cid);
        com = new Comuna();
        com.setIdComuna(0);
        com.setDescripcion("Seleccione una comuna");
        com.setIdCiudad(0);
        comunas.add(com);
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_PAIS() }");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                paises.add(new Pais(rs.getInt("id_pais"), rs.getString("descripcion")));
            }
            return paises;
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public List<Ciudad> obtenerCiudades(String pais) throws Exception {
        comunas.clear();
        ciudades.clear();
        cid = new Ciudad();
        cid.setIdCiudad(0);
        cid.setDescripcion("Seleccione una ciudad");
        cid.setIdPais(0);
        ciudades.add(cid);
        com = new Comuna();
        com.setIdComuna(0);
        com.setDescripcion("Seleccione una comuna");
        com.setIdCiudad(0);
        comunas.add(com);
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_CIUDADxPAIS(?) }");
            stm.setInt(1, Integer.parseInt(pais));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                cid = new Ciudad();
                cid.setIdCiudad(rs.getInt("id_ciudad"));
                cid.setDescripcion(rs.getString("descripcion"));
                cid.setIdPais(rs.getInt("id_pais"));
                ciudades.add(cid);
            }
            return ciudades;
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void recargarComuna() throws Exception {
        if (ciudadSeleccionada != null) {
            comunas = obtenerComunas(ciudadSeleccionada);
        } else {
            comunas = new ArrayList<>();
        }
    }

    private List<Comuna> obtenerComunas(String ciudad) throws Exception {
        comunas.clear();
        com = new Comuna();
        com.setIdComuna(0);
        com.setDescripcion("Seleccione una comuna");
        com.setIdCiudad(0);
        comunas.add(com);
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_COMUNAxCIUDAD(?) }");
            stm.setInt(1, Integer.parseInt(ciudad));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                com = new Comuna();
                com.setIdComuna(rs.getInt("id_comuna"));
                com.setDescripcion(rs.getString("descripcion"));
                com.setIdCiudad(rs.getInt("id_ciudad"));
                comunas.add(com);
            }
            return comunas;
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    private List<Curso> cursos = new ArrayList<>();
    private String cursoSeleccionado;

    public List<Curso> getCursos() {
        return cursos;
    }

    public String getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public List<Curso> getLstCurso() throws Exception {
        cursos.clear();
        cursos.add(new Curso(0, "Seleccione un curso"));
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_CURSO() }");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                cursos.add(new Curso(rs.getInt("id_curso"), rs.getString("descripcion")));
            }
            return cursos;
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }
}
