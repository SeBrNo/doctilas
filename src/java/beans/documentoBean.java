package beans;

import com.google.gson.Gson;
import models.Documento;
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
import models.TipoDocumento;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "documentoBean")
@ViewScoped
public class documentoBean {

    Gson gs = new Gson();
    public String arr;
    CallableStatement stm;
    private JSONArray jsonArray;
    private JSONObject obj;
    conexion conexiones = new conexion();
    ArrayList ls = new ArrayList();
    Documento doc;
    ResultSet rs;
    TipoDocumento tipoDoc;
    String error = "Exito", bar = "";
    
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
    
    public void lstTipoDoc() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstTipoDoc");
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_TIPO_DOCUMENTO() }");
            rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                tipoDoc = new TipoDocumento();
                tipoDoc.setIdTipoDocumento(rs.getInt("id_tipo_documento"));
                tipoDoc.setDescripcion(rs.getString("descripcion"));
                ls.add(tipoDoc);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }
    
    public void insTipoDoc() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insTipoDoc");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_TIPO_DOCUMENTO(?) }");
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

    public void modTipoDoc() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("modTipoDoc");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_TIPO_DOCUMENTO(?,?) }");
                    stm.setInt(1, obj.getInt("idTipo"));
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

    public void delTipoDoc() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("delTipoDoc");
        try {
            conexiones.conecta();
            if (bar != null) {
                jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = (JSONObject) jsonArray.get(i);
                    stm = conexiones.conn.prepareCall("{ call spPRY_DELETE_TIPO_DOCUMENTO(?) }");
                    stm.setInt(1, obj.getInt("idTipo"));
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
