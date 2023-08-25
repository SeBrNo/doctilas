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
import models.Dashboard;
import models.Kpi;

@ManagedBean(name = "dashboardBean")
@ViewScoped
public class dashboardBean {

    Gson gs = new Gson();
    Gson usegs = new Gson();
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
    CallableStatement stm;
    ResultSet rs;
    Kpi kpi;

    public void lstDash() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstDash");
        ls = new ArrayList();
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_DASHBOARDDOC(?) }");
            stm.setInt(1, Integer.parseInt(bar));
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Dashboard d = new Dashboard();
                d.setCantAlumno(rs.getInt("@alumnos"));
                ls.add(d);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void lstDashboard() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstDashboard");
        ls = new ArrayList();
        try {
            conexiones.conecta();
            stm = conexiones.conn.prepareCall("{ call spPRY_GET_DASHBOARD(?) }");
            stm.setString(1, bar);
            rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                kpi = new Kpi();
                kpi.setIdTipo(rs.getInt("id_tipo_documento"));
                kpi.setTipoDescripcion(rs.getString("tipoDescripcion"));
                kpi.setCuenta(rs.getInt("cuenta"));
                kpi.setTotal(rs.getInt("total"));
                kpi.setVal(rs.getString("val"));
                kpi.setPorcentaje(rs.getString("porcentaje"));
                ls.add(kpi);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }
}
