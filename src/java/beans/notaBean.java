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
import models.Nota;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "notaBean")
@ViewScoped
public class notaBean {

    Gson gs = new Gson();
    public String arr;

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

    conexion conexiones = new conexion();
    String error = "Exito", bar = "";
    ArrayList ls = new ArrayList();

    public void insNota() throws Exception {
        error = "Exito";
            bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insNota");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_NOTA(?,?,?) }");
                    stm.setString(1, obj.getString("rut"));
                    stm.setFloat(2, Float.parseFloat(obj.getString("nota")));
                    stm.setInt(3, obj.getInt("cuest"));
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

    public void srcNotas() throws Exception {
        error = "Exito";
        String params[] = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("srcNotas").split(",");
        try {
            conexiones.conecta();
            ls = new ArrayList();
            CallableStatement stm = conexiones.conn.prepareCall(" { call spPRY_GET_NOTAS(?,?) } ");
            stm.setString(1, params[0]);//rut
            stm.setInt(2, Integer.parseInt(params[1]));//asignatura
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Nota n = new Nota();
                n.setIdNota(rs.getInt("id_nota"));
                n.setDetalle(rs.getString("detalle"));
                n.setNota(rs.getDouble("nota"));
                ls.add(n);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void updNota() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("updNota");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_NOTA(?,?) }");
                    stm.setInt(1, Integer.parseInt(obj.getString("idNota")));
                    stm.setFloat(2, Float.parseFloat(obj.getString("nota")));
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

    public void delNota() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("delNota");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_DELETE_NOTA(?) }");
                    stm.setInt(1, obj.getInt("idNota"));
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
