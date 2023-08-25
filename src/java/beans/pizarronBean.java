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
import javax.servlet.http.HttpSession;
import models.Archivo;
import models.Asignatura;
import models.Curso;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "pizarronBean")
@ViewScoped
public class pizarronBean {

    private boolean elementosCargados = false;
    ResultSet rs;
    CallableStatement stm;
    conexion conexiones = new conexion();
    private int idCur;
    private String asignaturaSeleccionada, mimeType;
    List<Asignatura> asignaturasS, asignaturas = new ArrayList<>();
    private List<Archivo> archivos = new ArrayList<>();
    private Asignatura asi;
    private Archivo arc;
    private FacesMessage errorMessage, msg;
    private File archivo;
    HttpSession sesion;

    public String getAsignaturaSeleccionada() {
        return asignaturaSeleccionada;
    }

    public void setAsignaturaSeleccionada(String asignaturaSeleccionada) {
        this.asignaturaSeleccionada = asignaturaSeleccionada;
    }

    public List<Archivo> getArchivos() {
        return archivos;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public List<Asignatura> getAsignaturas() throws Exception {
        if (!elementosCargados) {
            asignaturas.clear();
            asi = new Asignatura();
            asi.setIdAsignatura(0);
            asi.setDescripcion("Seleccione una asignatura");
            asignaturas.add(asi);
            try {
                sesion = SessionUtils.getSession();
                String idCur = (String) sesion.getAttribute("curs");
                conexiones.conecta();
                stm = conexiones.conn.prepareCall("{ call spPRY_GET_ASIGNATURAXID(?) }");
                stm.setInt(1, Integer.parseInt(idCur));
                rs = stm.executeQuery();
                while (rs.next()) {
                    asi = new Asignatura();
                    asi.setIdAsignatura(rs.getInt("id_asignatura"));
                    asi.setDescripcion(rs.getString("descripcion"));
                    asignaturas.add(asi);
                }
                asignaturasS = asignaturas;
            } catch (Exception e) {
                throw e;
            } finally {
                conexiones.disconnect();
                elementosCargados = true;
            }
        }
        return asignaturasS;
    }

    public void recargarTabla() throws Exception {
        if (asignaturaSeleccionada != null) {
            archivos = obtenerArchivos(asignaturaSeleccionada);
        } else {
            archivos = new ArrayList<>();
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
