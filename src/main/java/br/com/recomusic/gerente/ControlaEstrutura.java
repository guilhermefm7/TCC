package br.com.recomusic.gerente;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.recomusic.bean.UsuarioBean;
 
 
/**
 * @author Guilherme
 *
 * 
 * 
 * Esta classe que controla a estrutura das telas
 * 
 */
public class ControlaEstrutura
{
	/**
	 * Responsavel por verificar a sessao
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
	 * Método responsável por encerrar a sessão e redirecionar a página inicial
	 */
	public static void encerrarSessao()
	{
		try
		{
			FacesContext fc = FacesContext.getCurrentInstance();  
		    HttpSession session = (HttpSession)fc.getExternalContext().getSession(false);  
		    session.invalidate();  

			FacesContext.getCurrentInstance().getExternalContext().redirect("/RecoMusic");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Responsável por retornar um Bean
	 */
	public static Object getBean(String ref) {    
	    FacesContext facesContext = FacesContext.getCurrentInstance();   
	    
	    ELContext elContext = facesContext.getELContext();    
	    ExpressionFactory factory = facesContext.getApplication().getExpressionFactory();    
	    return factory.createValueExpression(elContext, "#{" + ref + "}",Object.class).getValue(elContext);    
	} 
}