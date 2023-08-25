package beans;

import com.google.gson.Gson;
import dao.conexion;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import models.Pais;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "paisBean")
@SessionScoped
public class paisBean {

    Gson gs = new Gson();
    public String arr;
    private JSONArray jsonArray;
    private JSONObject obj;
    CallableStatement stm;
    conexion conexiones = new conexion();
    String error = "Exito", bar = "";
    ArrayList ls = new ArrayList();

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
            Logger.getLogger(paisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            Logger.getLogger(paisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listPais() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("listPais");
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_PAIS() }");
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Pais p = new Pais(rs.getInt("id_pais"), rs.getString("descripcion"));
                ls.add(p);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void insPais() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insPais");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_PAIS(?) }");
                    stm.setString(1, obj.getString("desc"));
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

    public void modPais() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("modPais");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_PAIS(?,?) }");
                    stm.setInt(1, obj.getInt("idPais"));
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

    public void delPais() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("delPais");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_DELETE_PAIS(?) }");
                    stm.setInt(1, obj.getInt("idPais"));
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
