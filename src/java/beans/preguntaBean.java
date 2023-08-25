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
import models.CuestionarioPregunta;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "preguntaBean")
@ViewScoped
public class preguntaBean {

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

    public void lstPregxId() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstPregxId");
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_PREGUNTAXID(?) }");
            stm.setInt(1, Integer.parseInt(bar));
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                CuestionarioPregunta cp = new CuestionarioPregunta();
                cp.setIdCuestionarioPregtuna(rs.getInt("id_cuestionario_pregunta"));
                cp.setIdCuestionario(rs.getInt("id_cuestionario"));
                cp.setIdPregunta(rs.getInt("id_pregunta"));
                cp.setDescripcion(rs.getString("descripcion"));
                ls.add(cp);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    int idPreg = 0;

    public void insPreg() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insPreg");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_PREGUNTA(?) }");
                    stm.setString(1, obj.getString("descPreg"));
                    ResultSet rs = stm.executeQuery();
                    while (rs.next()) {
                        idPreg = rs.getInt("id_pregunta");
                    }
                }
                sendResJson(idPreg);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void updPreg() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("updPreg");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_PREGUNTA(?,?) }");
                    stm.setInt(1, obj.getInt("idPregunta"));
                    stm.setString(2, obj.getString("descPreg"));
                    stm.execute();
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
