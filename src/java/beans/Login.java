/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.conexion;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class Login implements Serializable {

    private static final long serialVersionUID = 1094801825228386363L;

    private String pwd;
    private String msg;
    private String user;
    String rol;
    String cur;
    private int rolIn;
    private String desteny;
    private String username;
    private String iduser;
    String var;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    /**
     * @return the rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * @return the iduser
     */
    public String getIduser() {
        return iduser;
    }

    /**
     * @param iduser the iduser to set
     */
    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    /**
     * @return the desteny
     */
    public String getDesteny() {
        return desteny;
    }

    /**
     * @param desteny the desteny to set
     */
    public void setDesteny(String desteny) {
        this.desteny = desteny;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    // --------------------
    public void validateUsernamePassword() throws Exception {
        String val[] = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("users")
                .split(",");
        RequestContext context = RequestContext.getCurrentInstance();
        // boolean valid = validate(user, pwd);
        boolean valid = validate(val[0], val[1]);

        setUser(val[0]);
        if (valid) {
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", user);
            session.setAttribute("username", user);
            session.setAttribute("curs", cur);
            context.execute(
                    "window.location.href = '" + desteny + "';localStorage.setItem('nameUser','" + username + "')");
        } else {
            context.execute("swal(\"Oops!\", \"Los datos del usuario no son válidos!\", \"error\");setTimeout(function (){window.location.href = 'login.xhtml';},2000)");
        }
    }

    public boolean validate(String user, String password) throws Exception {
        username = "";
        desteny = "";
        iduser = "";
        rol = "";
        var = "";
        cur = "";
        conexion conexiones = new conexion();
        try {
            conexiones.conecta();
            CallableStatement stm = conexiones.conn.prepareCall("{ call spPRY_Login(?,?) }");
            stm.setString(1, user);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                rol = rs.getString("id_tipo_usuario");
                rolIn = Integer.parseInt(rol);
                switch (rolIn) {
                    case 1:
                        desteny = "indexAdmin.html";
                        var = "_menuAD.html";
                        break;
                    case 2:
                        desteny = "index.html";
                        var = "_menu.html";
                        break;
                    case 3:
                        desteny = "indexAlumno.html";
                        var = "_menuAl.html";
                        break;
                }
                cur = rs.getString("curs");
                username = rs.getString("NombreCompleto") + "," + rs.getString("rut") + ","
                        + rs.getString("TipoUsuario") + "," + rs.getString("curs") + "," + var;
                iduser = rs.getString("rut");
                setUser(rs.getString("rut"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            conexiones.disconnect();
        }
        return true;
    }

    // logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("localStorage.clear();window.location.href = 'login.xhtml';");
        return "login";
    }

    // register event, invalidate session
    public String registra() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("localStorage.clear();window.location.href = 'register.xhtml';");
        return "register";
    }

}
