package beans;

import com.google.gson.Gson;
import dao.conexion;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import models.Cuestionario;
import models.Respuesta;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@ManagedBean(name = "cuestionarioBean")
@ViewScoped
public class cuestionarioBean {

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

    public void sendResMsg(String msg) {
        try {
            arr = msg;
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
    private int idCuest;
    String error = "Exito", bar = "";
    ArrayList ls = new ArrayList();

    public void insCuest() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("insCuest");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_CUESTIONARIO(?,?,?) }");
                    stm.setInt(1, obj.getInt("docAsigDocum"));
                    stm.setString(2, obj.getString("descTitulo"));
                    stm.setInt(3, obj.getInt("tipoDocDocum"));
                    ResultSet rs = stm.executeQuery();
                    while (rs.next()) {
                        idCuest = rs.getInt("id_cuestionario");
                    }
                    usegs = new Gson();
                    cuestionarioPregunta(idCuest, usegs.fromJson(obj.getString("preguntas"), int[].class));
                }

                sendResponse(error);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void cuestionarioPregunta(int idPreg, int[] preguntas) throws Exception {
        try {
            for (int i = 0; i < preguntas.length; i++) {
                CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_INSERT_CUESTIONARIOPREGUNTA(?,?) }");
                stm.setInt(1, preguntas[i]);
                stm.setInt(2, idCuest);
                stm.executeQuery();
            }
        } catch (Exception e) {
            throw e;
        }
    }

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
                Cuestionario c = new Cuestionario();
                c.setIdCuestionario(rs.getInt("id_cuestionario"));
                c.setDetalle(rs.getString("detalle"));
                ls.add(c);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void lstCuestDash() throws Exception {
        error = "Exito";
        String a[] = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstCuestDash").split(",");
        ls = new ArrayList();
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_ALUMNODASH2(?,?) }");
            stm.setInt(1, Integer.parseInt(a[0]));
            stm.setString(2, a[1]);
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Cuestionario c = new Cuestionario();
                c.setPendiente(rs.getInt("pendientes"));
                c.setAprobada(rs.getInt("aprobadas"));
                c.setReprobada(rs.getInt("reprobadas"));
                ls.add(c);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void lstCuestxAsig() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstCuestxAsig");
        try {
            ls = new ArrayList();
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_CUESTIONARIOxASIG(?) }");
            stm.setString(1, bar);
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Cuestionario c = new Cuestionario();
                c.setIdCuestionario(rs.getInt("id_cuestionario"));
                c.setDetalle(rs.getString("detalle"));
                ls.add(c);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void lstCuestxId() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstCuestxId");
        ls = new ArrayList();
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_CUESTIONARIOXID(?) }");
            stm.setInt(1, Integer.parseInt(bar));
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Cuestionario c = new Cuestionario();
                c.setIdCuestionario(rs.getInt("id_cuestionario"));
                c.setFecha(rs.getString("fecha"));
                c.setIdAsignatura(rs.getInt("id_asignatura"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setIdTipoDocumento(rs.getInt("id_tipo_documento"));
                c.setDetalle(rs.getString("detalle"));
                ls.add(c);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    String respon;

    public void delCuest() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("delCuest");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    if (verNota(obj.getInt("idCuestionario"))) {
                        CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_PREGUNTAXCUESTIONARIO(?) }");
                        CallableStatement stm2 = conexiones.conn.prepareCall("{ call spPRY_DELETE_RESPUESTA(?) }");
                        stm.setInt(1, obj.getInt("idCuestionario"));
                        ResultSet rs = stm.executeQuery();
                        while (rs.next()) {
                            stm2.setInt(1, rs.getInt("id_pregunta"));
                            stm2.execute();
                        }
                        stm = conexiones.conn.prepareCall("{ call spPRY_DELETE_CUESTIONARIO(?) }");
                        stm.setInt(1, obj.getInt("idCuestionario"));
                        stm.execute();
                        respon = "OK";
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

    private int con;

    public boolean verNota(int idCues) throws Exception {
        try {
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_COUNTNOTA(?) }");
            stm.setInt(1, idCues);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                con = rs.getInt("cuenta");
            }
            return con <= 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public void updCuest() throws Exception {
        error = "Exito";
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("updCuest");
        try {
            conexiones.conecta();
            if (bar != null) {
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_UPDATE_CUESTIONARIO(?,?,?,?) }");
                    stm.setInt(1, obj.getInt("idCuestionario"));
                    stm.setInt(2, obj.getInt("asigCuest"));
                    stm.setInt(3, obj.getInt("tipoDocCuest"));
                    stm.setString(4, obj.getString("descCuest"));
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

    public void lstAlmCuest() throws Exception {
        error = "Exito";
        String a[] = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lstAlmCuest").split(",");
        ls = new ArrayList();
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_GET_ALUMNOCUESTIONARIO(?,?) }");
            stm.setInt(1, Integer.parseInt(a[0]));
            stm.setString(2, a[1]);
            ResultSet rs = stm.executeQuery();
            ls.clear();
            while (rs.next()) {
                Cuestionario c = new Cuestionario();
                c.setIdCuestionario(rs.getInt("id_cuestionario"));
                c.setFecha(rs.getString("fecha"));
                c.setIdAsignatura(rs.getInt("id_asignatura"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setIdTipoDocumento(rs.getInt("id_tipo_documento"));
                c.setTipoDocumento(rs.getString("tipoDocumento"));
                c.setIdNota(rs.getInt("id_nota"));
                c.setRut(rs.getString("rut"));
                c.setNota(rs.getFloat("nota"));
                c.setEst(rs.getInt("est"));
                ls.add(c);
            }
            sendJson(ls);
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

    public void finCuest() throws Exception {
        error = "Exito";
        Respuesta r;
        int cont = 0;
        double not = 0d, es = 0.6d, napr = 4.0d, nmax = 7.0d, nmin = 1.0d, tot = 0d;
        String preg[] = null, resp[] = null;
        DecimalFormat df = new DecimalFormat("#.0");
        bar = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("finCuest");
        try {
            conexiones.conecta();
            if (bar != null) {
                ArrayList<Respuesta> nt = new ArrayList();
                JSONArray jsonArray = new JSONArray(bar);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj;
                    obj = (JSONObject) jsonArray.get(i);
                    preg = obj.getString("preguntas").split(",");
                    CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_VALIDAR_RESPUESTAS(?) }");
                    ResultSet rs;
                    nt.clear();
                    for (int j = 0; j < preg.length; j++) {
                        stm.setInt(1, Integer.parseInt(preg[j]));
                        rs = stm.executeQuery();
                        while (rs.next()) {
                            r = new Respuesta();
                            r.setIdRespuesta(rs.getInt("id_respuesta"));
                            r.setEstado(rs.getInt("estado"));
                            r.setIdPregunta(rs.getInt("id_pregunta"));
                            nt.add(r);
                        }
                    }
                    resp = obj.getString("respuestas").split(",");
                    tot = preg.length;
                }
                for (int i = 0; i < nt.size(); i++) {
                    if (nt.get(i).getIdPregunta() == Integer.parseInt(preg[i])) {
                        if (nt.get(i).getIdRespuesta() == Integer.parseInt(resp[i])) {
                            cont++;
                        }
                    }
                }
                if (cont < es * tot) {
                    not = Double.parseDouble(df.format((napr - nmin) * ((cont) / (es * tot)) + nmin).replace(",", "."));
                } else if (cont >= es * tot) {
                    not = Double.parseDouble(df.format((nmax - napr) * ((cont - es * tot) / (tot * (1 - es))) + napr).replace(",", "."));
                }
                ls.add(not);
                sendJson(ls);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
    }

}
