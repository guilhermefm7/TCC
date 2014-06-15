package br.com.recomusic.persistencia.utils;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.recomusic.bean.UsuarioBean;
import br.com.recomusic.dao.InformacaoMusicalCadastroBandaDAO;
import br.com.recomusic.om.Banda;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;

import com.echonest.api.v4.EchoNestAPI;

@ManagedBean(name="GlobalBean")
@SessionScoped
public class UtilidadesTelas
{
	private static Usuario usuarioGlobal;
	EchoNestAPI en = new EchoNestAPI("9QB1EM63CLM2RR5V3");
	private InformacaoMusicalCadastroBandaDAO informacaoMusicalCadastroBandaDAO = new InformacaoMusicalCadastroBandaDAO( ConectaBanco.getInstance().getEntityManager());
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
	
	/**
	 * Responsavel por enerrar a sessao
	 */
	public List<Banda> pesquisaBanda(List<String> listaMusicas, boolean existe) throws Exception
	{
		Banda banda = null;
		List<Banda> listaBandas =  new ArrayList<Banda>();
		
		if(existe)
		{
			List<Banda> listaBandasAux =  new ArrayList<Banda>();
			List<Banda> listaBandasAuxUsuario =  new ArrayList<Banda>();
			for (String string : listaMusicas)
			{
				banda = PesquisaMusica.procuraArtista(en, string);
				if(banda!=null)
				{
					listaBandasAux.add(banda);
				}
			}
			
			listaBandasAuxUsuario = informacaoMusicalCadastroBandaDAO.procurarBandasUsuario(getUsuarioGlobal());
			
			boolean existeBanda = false;
			for (Banda bandaAux : listaBandasAux)
			{
				for (Banda bandaCadastrada : listaBandasAuxUsuario)
				{
					if(bandaAux.getIdBanda().equals(bandaCadastrada.getIdBanda()))
					{
						existeBanda = true;
					}
				}
				
				if(!existeBanda)
				{
					listaBandas.add(bandaAux);
				}
			}
		}
		else
		{
			for (String string : listaMusicas)
			{
				banda = PesquisaMusica.procuraArtista(en, string);
				if(banda!=null)
				{
					listaBandas.add(banda);
				}
			}
		}
		
		
		return listaBandas;
	}

	public static Usuario getUsuarioGlobal() {
		return usuarioGlobal;
	}

	public static void setUsuarioGlobal(Usuario usuarioGlobal) {
		UtilidadesTelas.usuarioGlobal = usuarioGlobal;
	}
}
