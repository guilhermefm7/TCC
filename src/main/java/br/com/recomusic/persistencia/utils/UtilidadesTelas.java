package br.com.recomusic.persistencia.utils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.recomusic.bean.UsuarioBean;
import br.com.recomusic.om.Usuario;

@ManagedBean(name="GlobalBean")
@SessionScoped
public class UtilidadesTelas
{
	private static Usuario usuarioGlobal;
	
    public UtilidadesTelas() {   }  
    
    /**
	 * Verifica a sessão corrente
	 */
	public static boolean verificarSessao()
	{
		try
		{
			if(((UsuarioBean) getBean("UsuarioBean")).getUsuario()!=null)
			{ return true; }
			else
			{
				encerrarSessao();
				return false; 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}	
	}
    
	/**
	 * Responsavel por retornar um Bean
	 */
	public static Object getBean(String ref) {    
	    FacesContext facesContext = FacesContext.getCurrentInstance();   
	    
	    ELContext elContext = facesContext.getELContext();    
	    ExpressionFactory factory = facesContext.getApplication().getExpressionFactory();    
	    return factory.createValueExpression(elContext, "#{" + ref + "}",Object.class).getValue(elContext);    
	} 
	
	/**
	 * Responsavel por enerrar a sessao
	 */
	public static void encerrarSessao()
	{
		try
		{
			FacesContext fc = FacesContext.getCurrentInstance();  
		    HttpSession session = (HttpSession)fc.getExternalContext().getSession(false);  
		    session.invalidate();  

			FacesContext.getCurrentInstance().getExternalContext().redirect("/RecoMusic/login.xhtml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Usuario getUsuarioGlobal() {
		return usuarioGlobal;
	}

	public static void setUsuarioGlobal(Usuario usuarioGlobal) {
		UtilidadesTelas.usuarioGlobal = usuarioGlobal;
	}
}
