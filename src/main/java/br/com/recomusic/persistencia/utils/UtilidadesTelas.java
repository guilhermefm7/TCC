package br.com.recomusic.persistencia.utils;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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
	private boolean curtiuMusica = false;
	private boolean naoCurtiuMusica = false;
	private static boolean ckMusica; 
	private static boolean ckBanda; 
	private boolean ckMusicaAux; 
	private boolean ckBandaAux; 
	
    protected Boolean enableMessage = Boolean.TRUE;
    protected String tokenFacebook;
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
			if(((UsuarioBean) getBean("UsuarioBean")).getUsuario()!=null && ((UsuarioBean) getBean("UsuarioBean")).getUsuario().getPkUsuario()>0)
			{ return true; }
			else { return false; }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}	
	}
	
	/**
	 * Verifica se foi uma conexão Facebook
	 */
	public static String verificarSessaoFacebook()
	{
		try
		{
			if(((UsuarioBean) getBean("UsuarioBean")).getGuardaToken()!=null && ((UsuarioBean) getBean("UsuarioBean")).getGuardaToken().length()>0)
			{ return ((UsuarioBean) getBean("UsuarioBean")).getGuardaToken(); }
			else { return null; }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
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
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/index.xhtml");
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
	
	/*
	 * Pesquisa uma música na API através do seu id passado como parâmetro
	 * String idMusica
	 * return Música
	 */
	public Musica pesquisaMusica(String idMusica) throws Exception
	{
		Musica musica = new Musica();
		musica = PesquisaMusica.procuraMusica(en, idMusica);
		return musica;
	}
	
	/*
	 * Pesquisa na API (através do JSON) todos os gêneros de uma determinada banda
	 * String idBanda
	 * return List<String> listaGeneros
	 */
	public static List<String> requisitarAPIGeneroBanda(String idBanda) throws Exception
	{
		return PesquisaMusica.requisitarGeneroBanda(idBanda);
	}
	
	public void redirecionarErro() throws Exception
	{
		FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/resources/ajax/page_500.html");
	}
	
    public void addMessage(String summary, Severity severityInfo) {
        if (enableMessage) {
            FacesMessage message = new FacesMessage(severityInfo, summary, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
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
	
    public Boolean getEnableMessage() {
        return enableMessage;
    }

    public void setEnableMessage(Boolean enableMessage) {
        this.enableMessage = enableMessage;
    }

	public String getTokenFacebook() {
		return tokenFacebook;
	}

	public void setTokenFacebook(String tokenFacebook) {
		this.tokenFacebook = tokenFacebook;
	}
	
	public void changeCheckBoxMusica() {
		this.ckMusica = this.ckMusicaAux;
	}
	
	public void changeCheckBoxBanda() {
		this.ckBanda = this.ckBandaAux;
	}

	public static boolean isCkMusica() {
		return ckMusica;
	}

	public static void setCkMusica(boolean ckMusica) {
		ckMusica = ckMusica;
	}

	public static boolean isCkBanda() {
		return ckBanda;
	}

	public static void setCkBanda(boolean ckBanda) {
		ckBanda = ckBanda;
	}

	public boolean isCkMusicaAux() {
		return ckMusicaAux;
	}

	public void setCkMusicaAux(boolean ckMusicaAux) {
		this.ckMusicaAux = ckMusicaAux;
	}

	public boolean isCkBandaAux() {
		return ckBandaAux;
	}

	public void setCkBandaAux(boolean ckBandaAux) {
		this.ckBandaAux = ckBandaAux;
	}
}
