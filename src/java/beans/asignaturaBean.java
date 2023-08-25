package beans;

import com.google.gson.Gson;
import dao.conexion;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import models.Asignatura;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "asignaturaBean")
@ViewScoped
public class asignaturaBean {

    Gson gs = new Gson();
    public String arr;
    private JSONArray jsonArray;
    private JSONObject obj;
    CallableStatement stm;
    String respon;

    public void sendJson(ArrayList ls) {
        try {
            arr = gs.toJson(ls);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/json");
            externalContext.setResponseCharacterEncoding("UTF-8");
            externalContext.getResponseOutputWriter().write(arr);
            facesContext.responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(cursoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendResponse(String response) {
        try {
            arr = response;
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/json");
            externalContext.setResponseCharacterEncoding("UTF-8");
            externalContext.getResponseOutputWriter().write(arr);
            facesContext.responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(cursoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendResJson() {
        try {
            arr = "{'status_wms': 'OK'}";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/json");
            externalContext.setResponseCharacterEncoding("UTF-8");
            externalContext.getResponseOutputWriter().write(arr);
            facesContext.responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(cursoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendResMsg(String msg) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("text/plain");
            externalContext.setResponseCharacterEncoding("UTF-8");
            externalContext.getResponseOutputWriter().write(msg);
            facesContext.responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(cursoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    conexion conexiones = new conexion();
    String error = "Exito", bar = "", pidAsig = "";
    ArrayList ls = new ArrayList();

    public void lstAsignaturaxId() throws SQLException {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstAsignaturaxId");
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_ASIGNATURAXID(?) }");
            stm.setString(1, bar);
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Asignatura a = new Asignatura();
                a.setIdAsignatura(rs.getInt("id_asignatura"));
                a.setDescripcion(rs.getString("descripcion"));
                ls.add(a);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void lstAsig() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstAsig");
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_ASIGNATURA() }");
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Asignatura a = new Asignatura();
                a.setIdAsignatura(rs.getInt("id_asignatura"));
                a.setDescripcion(rs.getString("descripcion"));
                ls.add(a);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void lstAsigFull() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstAsigFull");
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_ASIGNATURA_FULL() }");
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Asignatura a = new Asignatura();
                a.setIdAsignatura(rs.getInt("id_asignatura"));
                a.setDescripcion(rs.getString("descripcion"));
                a.setIdCurso(rs.getInt("id_curso"));
                a.setCursoDescripcion(rs.getString("curso_descripcion"));
                ls.add(a);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void insAsig() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insAsig");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_ASIGNATURA(?,?) }");
                    stm.setString(1, obj.getString("desc"));
                    stm.setInt(2, obj.getInt("idCurso"));
                    ResultSet rs = stm.executeQuery();
                    while (rs.next()) {
                        pidAsig = String.valueOf(rs.getInt("rsp"));
                    }
                    if (!"0".equals(pidAsig)) {
                        respon = crearCarpetaAsig(String.valueOf(obj.getInt("idCurso")), pidAsig);//crear archivo
                    } else {
                        respon = "XX";
                    }
                }
                sendResMsg(respon);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void modAsig() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("modAsig");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_ASIGNATURA(?,?,?) }");
                    stm.setInt(1, obj.getInt("idAsig"));
                    stm.setString(2, obj.getString("desc"));
                    stm.setInt(3, obj.getInt("idCurso"));
                    stm.executeQuery();
                }
                sendResponse(error);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void delAsig() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("delAsig");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    if (verArc(obj.getInt("idAsig"))) {
                        if (delCarpetaAsig(String.valueOf(obj.getInt("idCurso")), String.valueOf(obj.getInt("idAsig")))) {
                            stm = conexiones.conn.prepareCall("{ call spPRY_DELETE_ASIGNATURA(?) }");
                            stm.setInt(1, obj.getInt("idAsig"));
                            stm.executeQuery();
                            respon = "OK";
                        }
                    } else {
                        respon = "XX";
                    }
                }
                sendResMsg(respon);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    private static final String UPLOAD_DIRECTORY = "C:/doctilasArchive/";
    private String URL = "";
    private File folder;

    public String crearCarpetaAsig(String curso, String asig) {
        try {
            URL = UPLOAD_DIRECTORY + curso + "/" + asig;
            folder = new File(URL);
            if (!folder.exists()) {
                folder.mkdirs();
                return "OK";
            } else {
                return "XX";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean delCarpetaAsig(String curso, String asig) {
        try {
            URL = UPLOAD_DIRECTORY + curso + "/" + asig;
            folder = new File(URL);
            if (folder.exists()) {
                folder.delete();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private int cont;

    public boolean verArc(int idAsig) throws SQLException {
        try {
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_ARCHIVOCOUNT(?) }");
            stm.setInt(1, idAsig);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                cont = rs.getInt("cuenta");
            }
            return cont == 0;
        } catch (Exception e) {
            throw e;
        }
    }
}
