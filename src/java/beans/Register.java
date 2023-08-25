package beans;

import models.Pais;
import models.Comuna;
import models.Ciudad;
import com.google.gson.Gson;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.conexion;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "register")
@SessionScoped
public class Register implements Serializable {

    Gson gs = new Gson();
    public String arr;
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
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static final long serialVersionUID = 1094801825228386363L;

    public void createAccount() throws Exception {
        String error = "Error";
        int count = 0;
        String bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("registrarUsuario");
        conexion conexiones = new conexion();
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_Register(?,?,?,?,?,?,?,?,?,?,?,?,?) }");
                    stm.setString(1, obj.getString("calle"));
                    stm.setInt(2, obj.getInt("numU"));
                    stm.setInt(3, obj.getInt("comuna"));
                    stm.setString(4, obj.getString("rut"));
                    stm.setString(5, obj.getString("nombre"));
                    stm.setString(6, obj.getString("apellidoP"));
                    stm.setString(7, obj.getString("apellidoM"));
                    stm.setInt(8, obj.getInt("tipoUser"));
                    stm.setString(9, obj.getString("pass"));
                    stm.setString(10, obj.getString("correo"));
                    stm.setString(11, obj.getString("fechaN"));
                    stm.setString(12, obj.getString("curso"));
                    stm.setString(13, obj.getString("sexo"));
                    stm.execute();
                }
            }
        } catch (SQLIntegrityConstraintViolationException es) {
            count = 1;
            context.execute("swal(\"Oops!\", \"Ya se encuentra un usuario con ese rut.\", \"error\");localStorage.clear();");
        } catch (Exception e) {
            count = 1;
            context.execute("swal(\"Oops!\", \"A ocurrido un error inesperado.\", \"error\");");
            throw e;
        } finally {
            conexiones.disconnect();
        }
        if (count != 1) {
            context.execute("swal(\"De acuerdo!\", \"Se ha creado la cuenta exitosamente.\", \"success\");setTimeout(function (){window.location.href = 'login.xhtml';localStorage.clear();},1000)");
        }
    }
}
