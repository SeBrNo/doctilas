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
import models.Comuna;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "comunaBean")
@SessionScoped
public class comunaBean {

    Gson gs = new Gson();
    Comuna c;
    public String arr;
    private JSONArray jsonArray;
    private JSONObject obj;
    CallableStatement stm;
    conexion conexiones = new conexion();

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
            Logger.getLogger(ciudadBean.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ciudadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String error = "Exito", bar = "";
    ArrayList ls = new ArrayList();

    public void listComuna() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("listComuna");
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_COMUNA() }");
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                c = new Comuna();
                c.setIdComuna(rs.getInt("id_comuna"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setIdCiudad(rs.getInt("id_ciudad"));
                c.setCiudadDescripcion(rs.getString("ciudadDescripcion"));
                ls.add(c);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void insComuna() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insComuna");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_COMUNA(?,?) }");
                    stm.setString(1, obj.getString("desc"));
                    stm.setInt(2, obj.getInt("idCiud"));
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

    public void modComuna() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("modComuna");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_COMUNA(?,?,?) }");
                    stm.setInt(1, obj.getInt("idCom"));
                    stm.setString(2, obj.getString("desc"));
                    stm.setInt(3, obj.getInt("idCiud"));
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

    public void delComuna() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("delComuna");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_DELETE_COMUNA(?) }");
                    stm.setInt(1, obj.getInt("idCom"));
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
