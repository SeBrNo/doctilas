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
import models.Curso;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "cursoBean")
@ViewScoped
public class cursoBean {

    Gson gs = new Gson();
    public String arr;
    private JSONArray jsonArray;
    private JSONObject obj;
    CallableStatement stm;
    ResultSet rs;

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
    String error = "Exito", bar = "";
    ArrayList ls = new ArrayList();
    String idC;
    String respon;

    public void listCurso() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("listCurso");
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_CURSO() }");
            rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Curso c = new Curso(rs.getInt("id_curso"), rs.getString("descripcion"));
                ls.add(c);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void insCurso() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insCurso");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_CURSO(?) }");
                    stm.setString(1, obj.getString("desc"));
                    rs = stm.executeQuery();
                    while (rs.next()) {
                        idC = String.valueOf(rs.getInt("rsp"));
                    }
                    if (!"0".equals(idC)) {
                        respon = crearCarpetaCurso(idC);//crear archivo
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

    public void modCurso() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("modCurso");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_CURSO(?,?) }");
                    stm.setInt(1, obj.getInt("idCurso"));
                    stm.setString(2, obj.getString("desc"));
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

    public void delCurso() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("delCurso");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    if (verArc(obj.getInt("idCurso"))) {
                        if (delCarpetaCurso(String.valueOf(obj.getInt("idCurso")))) {
                            stm = conexiones.conn.prepareCall("{ call spPRY_DELETE_CURSO(?) }");
                            stm.setInt(1, obj.getInt("idCurso"));
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

    public String crearCarpetaCurso(String curso) {
        try {
            URL = UPLOAD_DIRECTORY + curso;
            folder = new File(URL);
            if (!folder.exists()) {
                folder.mkdirs();
                return "OK";
            } else {
                return "XX";
            }
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public boolean delCarpetaCurso(String curso) {
        try {
            URL = UPLOAD_DIRECTORY + curso + "/";
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

    public boolean verArc(int idCurso) throws SQLException {
        try {
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_ASIGCOUNT(?) }");
            stm.setInt(1, idCurso);
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
