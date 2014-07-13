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
import br.com.recomusic.im.BandaGeneroIM;
import br.com.recomusic.om.Banda;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;

import com.echonest.api.v4.EchoNestAPI;

@ManagedBean(name="GlobalBean")
@SessionScoped
public class UtilidadesTelas
{
	private static Usuario usuarioGlobal;
	private  boolean curtiuMusica = false;
	private  boolean naoCurtiuMusica = false;
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
	 * Responsável por procurar se a lista de bandas curtidas pelo usuário existem e se elas já estão cadastradas nas informações de perfil do usuário
	 */
	
    /**
     * @author Guilherme
     * Responsável por procurar se a lista de bandas curtidas pelo usuário existem e se elas já estão cadastradas nas informações de perfil do usuário
     * @param listaMusicas - lista de BANDAS, existe - se o usuário já existe no sistema
     * @throws Exception
     * return List<BandaGeneroIM>
     */
	public List<BandaGeneroIM> pesquisaBanda(List<String> listaMusicas, boolean existe) throws Exception
	{
		Banda banda = null;
		//List<Banda> listaBandas =  new ArrayList<Banda>();
		List<BandaGeneroIM> listaBGIM = new ArrayList<BandaGeneroIM>();
		BandaGeneroIM bgIM;
		
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
					bgIM = new BandaGeneroIM();
					bgIM.setListaGeneros(PesquisaMusica.requisitarGeneroBanda(bandaAux.getIdBanda()));
					bgIM.setBanda(bandaAux);
					listaBGIM.add(bgIM);
					//listaBandas.add(bandaAux);
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
					bgIM = new BandaGeneroIM();
					bgIM.setListaGeneros(PesquisaMusica.requisitarGeneroBanda(banda.getIdBanda()));
					bgIM.setBanda(banda);
					listaBGIM.add(bgIM);
					//listaBandas.add(banda);
				}
			}
		}
		
		return listaBGIM;
	}
	
	public Musica pesquisaMusica(String idMusica) throws Exception
	{
		Musica musica = new Musica();
		musica = PesquisaMusica.procuraMusica(en, idMusica);
		return musica;
	}
	
	public void redirecionarErro() throws Exception
	{
		FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/index.xhtml");
	}

	public static Usuario getUsuarioGlobal() {
		return usuarioGlobal;
	}

	public static void setUsuarioGlobal(Usuario usuarioGlobal) {
		UtilidadesTelas.usuarioGlobal = usuarioGlobal;
	}

	public  boolean isCurtiuMusica() {
		return curtiuMusica;
	}

	public  void setCurtiuMusica(boolean curtiuMusica) {
		this.curtiuMusica = curtiuMusica;
	}

	public  boolean isNaoCurtiuMusica() {
		return naoCurtiuMusica;
	}

	public  void setNaoCurtiuMusica(boolean naoCurtiuMusica) {
		this.naoCurtiuMusica = naoCurtiuMusica;
	}
	
	
}
