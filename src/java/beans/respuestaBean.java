package beans;

import com.google.gson.Gson;
import dao.conexion;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import models.Respuesta;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "respuestaBean")
@ViewScoped
public class respuestaBean {

    Gson gs = new Gson();
    Gson usegs = new Gson();
    public String arr;
    private int bis;

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

    public void sendResJson(int ids) {
        try {
            arr = "{\"status_wms\": \"OK\",\"idPreg\":" + ids + "}";
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

    conexion conexiones = new conexion();
    String error = "Exito", bar = "";
    ArrayList ls = new ArrayList();

    public void lstCuest() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstCuest");
        ls = new ArrayList();
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_CUESTIONARIO() }");
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {

            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void updResp() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("updResp");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_RESPUESTA(?,?,?) }");
                    stm.setInt(1, obj.getInt("idRespuesta"));
                    stm.setString(2, obj.getString("respDesc"));
                    bis = obj.getBoolean("respCheck") ? 1 : 0;
                    stm.setInt(3, bis);
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

    public void lstRespxId() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstRespxId");
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_RESPUESTAXID(?) }");
            stm.setInt(1, Integer.parseInt(bar));
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Respuesta r = new Respuesta();
                r.setIdRespuesta(rs.getInt("id_respuesta"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setEstado(rs.getInt("estado"));
                r.setIdPregunta(rs.getInt("id_pregunta"));
                ls.add(r);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void insResp() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insResp");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_RESPUESTA(?,?,?) }");
                    stm.setString(1, obj.getString("respDesc"));
                    bis = obj.getBoolean("respCheck") ? 1 : 0;
                    stm.setInt(2, bis);
                    stm.setInt(3, obj.getInt("idPregunta"));
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
}
