package beans;

import dao.conexion;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.Archivo;
import models.Asignatura;
import models.Curso;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "blackboardBean")
@ViewScoped
public class blackboardBean {

    private boolean elementosCargados = false;
    private Asignatura asi;
    private Archivo arc;
    private List<Asignatura> asignaturas2 = new ArrayList<>(), asignaturas = new ArrayList<>();
    private List<Curso> cursosS, cursos = new ArrayList<>();
    private List<Archivo> archivos = new ArrayList<>();
    private String asignaturaSeleccionada, asigSel, cursoSeleccionado, cursoSel, archivoSeleccionado, mimeType, fileName, idCurso;
    //CAMBIAR
    private static final String UPLOAD_DIRECTORY = "C:\\doctilasArchive\\";
    ResultSet rs;
    CallableStatement stm;
    conexion conexiones = new conexion();
    private InputStream inputStream;
    private Path filePath;
    private FacesMessage errorMessage, msg;
    private File archivo;
    private String ruta;
    private int idC;

    public String getAsigSel() {
        return asigSel;
    }

    public void setAsigSel(String asigSel) {
        this.asigSel = asigSel;
    }

    public String getAsignaturaSeleccionada() {
        return asignaturaSeleccionada;
    }

    public void setAsignaturaSeleccionada(String asignaturaSeleccionada) {
        this.asignaturaSeleccionada = asignaturaSeleccionada;
    }

    public String getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public void setCursoSeleccionado(String cursoSeleccionado) {
        this.cursoSeleccionado = cursoSeleccionado;
    }

    public String getArchivoSeleccionado() {
        return archivoSeleccionado;
    }

    public void setArchivoSeleccionado(String archivoSeleccionado) {
        this.archivoSeleccionado = archivoSeleccionado;
    }

    public String getCursoSel() {
        return cursoSel;
    }

    public void setCursoSel(String cursoSel) {
        this.cursoSel = cursoSel;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public List<Asignatura> getAsignaturas2() {
        return asignaturas2;
    }

    public List<Archivo> getArchivos() {
        return archivos;
    }

    public void recargarAsignatura() throws Exception {
        if (cursoSeleccionado != null) {
            asignaturas = obtenerAsignaturas(cursoSeleccionado);
        }
    }

    public List<Curso> getCursos() throws Exception {
        if (!elementosCargados) {
            asignaturas.clear();
            cursos.clear();
            asi = new Asignatura();
            asi.setIdAsignatura(0);
            asi.setDescripcion("Seleccione una asignatura");
            asignaturas.add(asi);
            asignaturas2.clear();
            cursos.clear();
            asi = new Asignatura();
            asi.setIdAsignatura(0);
            asi.setDescripcion("Seleccione una asignatura");
            asignaturas2.add(asi);
            cursos.add(new Curso(0, "Seleccine un curso"));
            try {
                conexiones.conecta();
                stm = conexiones.conn.prepareCall("{ call spPRY_GET_CURSO() }");
                rs = stm.executeQuery();
                while (rs.next()) {
                    cursos.add(new Curso(rs.getInt("id_curso"), rs.getString("descripcion")));
                }
                cursosS = cursos;
            } catch (Exception e) {
                throw e;
            } finally {
                conexiones.disconnect();
            }
            elementosCargados = true;
        }
        return cursosS;
    }

    public List<Asignatura> obtenerAsignaturas(String curso) throws Exception {
        asignaturas.clear();
        asi = new Asignatura();
        asi.setIdAsignatura(0);
        asi.setDescripcion("Seleccione una asignatura");
        asignaturas.add(asi);
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_ASIGNATURAXID(?) }");
            stm.setInt(1, Integer.parseInt(curso));
            rs = stm.executeQuery();
            while (rs.next()) {
                asi = new Asignatura();
                asi.setIdAsignatura(rs.getInt("id_asignatura"));
                asi.setDescripcion(rs.getString("descripcion"));
                asignaturas.add(asi);
            }
            return asignaturas;
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void recargarTabla() throws Exception {
        if (asignaturaSeleccionada != null) {
            archivos = obtenerArchivos(asignaturaSeleccionada);
        } else {
            archivos = new ArrayList<>();
        }
    }

    public void recargarAsignatura2() throws Exception {
        if (cursoSel != null) {
            asignaturas2 = obtenerAsignatura2(cursoSel);
        } else {
            asignaturas2 = new ArrayList<>();
        }
    }

    private List<Asignatura> obtenerAsignatura2(String curso) throws Exception {
        asignaturas2.clear();
        asi = new Asignatura();
        asi.setIdAsignatura(0);
        asi.setDescripcion("Seleccione una asignatura");
        asignaturas2.add(asi);
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_ASIGNATURAXID(?) }");
            stm.setInt(1, Integer.parseInt(curso));
            rs = stm.executeQuery();
            while (rs.next()) {
                asi = new Asignatura();
                asi.setIdAsignatura(rs.getInt("id_asignatura"));
                asi.setDescripcion(rs.getString("descripcion"));
                asignaturas2.add(asi);
            }
            return asignaturas2;
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public List<Archivo> obtenerArchivos(String asignatura) throws Exception {
        archivos.clear();
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall(" { call spPRY_GET_ARCHIVO(?) } ");
            stm.setInt(1, Integer.parseInt(asignatura));
            rs = stm.executeQuery();
            while (rs.next()) {
                arc = new Archivo();
                arc.setIdArchivo(rs.getInt("id_archivo"));
                arc.setEnlace(rs.getString("enlace"));
                arc.setNombre(rs.getString("nombre"));
                archivos.add(arc);
            }
            return archivos;
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public List<Asignatura> getLstAsignaturas() throws Exception {
        asignaturas.clear();
        asi = new Asignatura();
        asi.setIdAsignatura(0);
        asi.setDescripcion("Seleccione una asignatura");
        asignaturas.add(asi);
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_ASIGNATURA_FULL() }");
            rs = stm.executeQuery();
            while (rs.next()) {
                asi = new Asignatura();
                asi.setIdAsignatura(rs.getInt("id_asignatura"));
                asi.setDescripcion(rs.getString("descripcion"));
                asignaturas.add(asi);
            }
            return asignaturas;
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public String searchCurso(int asigSel) throws Exception {
        try {
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_CURSOXASIG(?) }");
            stm.setInt(1, asigSel);
            rs = stm.executeQuery();
            while (rs.next()) {
                idC = rs.getInt("id_curso");
            }
            return String.valueOf(idC);
        } catch (Exception e) {
            throw e;
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws Exception {
        UploadedFile uploadedFile = event.getFile();
        if (uploadedFile != null) {
            try {
                conexiones.conecta();
                fileName = uploadedFile.getFileName();
                inputStream = uploadedFile.getInputstream();
                idCurso = searchCurso(Integer.parseInt(asigSel));
                ruta = UPLOAD_DIRECTORY + idCurso + "\\" + asigSel + "\\" + fileName;
                filePath = new File(UPLOAD_DIRECTORY + idCurso + "\\" + asigSel + "\\", fileName).toPath();
                archivo = new File(ruta);
                if (!archivo.exists()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_ARCHIVO(?,?,?) }");
                    stm.setString(1, ruta);
                    stm.setString(2, fileName);
                    stm.setInt(3, Integer.parseInt(asigSel));
                    stm.executeQuery();
                    errorMessage = new FacesMessage("El archivo se ha guardado correctamente!");
                    FacesContext.getCurrentInstance().addMessage(null, errorMessage);
                    recargarTabla();
                } else {
                    errorMessage = new FacesMessage("Ya existe un archivo con ese nombre en esa asignatura!");
                    FacesContext.getCurrentInstance().addMessage(null, errorMessage);
                }
            } catch (Exception e) {
                errorMessage = new FacesMessage("Ha ocurrido un error al guardar el archivo");
                FacesContext.getCurrentInstance().addMessage(null, errorMessage);
                throw e;
            } finally {
                conexiones.disconnect();
            }
        }
    }

    private StreamedContent downloadFileContent;

    public StreamedContent downloadFileContent(String enlace) throws Exception {
        archivo = new File(enlace);
        InputStream stream;
        try {
            stream = new FileInputStream(archivo);
        } catch (Exception e) {
            throw e;
        }
        mimeType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(archivo.getAbsolutePath());
        downloadFileContent = new DefaultStreamedContent(stream, mimeType, archivo.getName());

        return downloadFileContent;
    }

    public void deleteFile(String dir) throws Exception {
        try {
            conexiones.conecta();
            archivo = new File(dir);
            if (archivo.exists()) {
                archivo.delete();
                stm = conexiones.conn.prepareCall("{ call spPRY_DELETE_ARCHIVO(?) }");
                stm.setString(1, dir);
                stm.executeQuery();
                recargarTabla();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Archivo eliminado", "El archivo se ha eliminado correctamente.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Precaución", "El archivo indicado no existe para la ruta guardada.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ha ocurrido un error al eliminar el archivo");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }
}
