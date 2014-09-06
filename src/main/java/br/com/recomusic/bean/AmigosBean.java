package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

import br.com.recomusic.dao.AmigosUsuarioDAO;
import br.com.recomusic.dao.RequisicaoAmizadeDAO;
import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.AmigosUsuario;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;


@ManagedBean(name="AmigosBean")
@ViewScoped
public class AmigosBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<String> listaNomesMusica = null;
	private List<Usuario> listaAmigosFacebook = null;
	private List<Usuario> listaAmigos = null;
	private List<Usuario> listaRequisicoes = null;
	private List<Usuario> listaUsuarioProcurados = null;
	private String identificacaoUsuario;
	private String nomeAmigo;
	private Integer tabActiveIndex;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private AmigosUsuarioDAO amigosUsuarioDAO = new AmigosUsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private RequisicaoAmizadeDAO requisicaoAmizadeDAO = new RequisicaoAmizadeDAO( ConectaBanco.getInstance().getEntityManager());
	private boolean procurarAmigos = true;
	private boolean solicitacoesAmizade = true;
	private boolean possuiAmizades = false;
	
	public AmigosBean() {	}

	public void iniciar()
	{
		try
		{
			if(UtilidadesTelas.verificarSessao())
			{
				setUsuarioGlobal(getUsuarioGlobal());
				List<String> listaIdsFacebook = new ArrayList<String>();
				if(UtilidadesTelas.verificarSessaoFacebook()!=null && UtilidadesTelas.verificarSessaoFacebook().length()>0)
				{
					 FacebookClient facebookClient = new DefaultFacebookClient(UtilidadesTelas.verificarSessaoFacebook());
		    		 Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
					 if(myFriends.getData()!=null && myFriends.getData().size()>0)
					 {
						 for (int i = 0; i < myFriends.getData().size(); i++)
						 {
							 listaIdsFacebook.add(myFriends.getData().get(i).getId());
						 }
					 }
				 
					 listaAmigosFacebook = new ArrayList<Usuario>();
					 List<Usuario> listaAmigosFacebookAux = new ArrayList<Usuario>();
					 
					 if(listaIdsFacebook!=null && listaIdsFacebook.size()>0)
					 {
						 for (String id : listaIdsFacebook)
						 {
							 Usuario u = null;
							 u = usuarioDAO.validarID(id);
							 
							 if(u!=null && u.getPkUsuario()>0)
							 {
								 listaAmigosFacebookAux.add(u);
							 }
						 }
					 }
					 
					 if(listaAmigosFacebookAux!=null && listaAmigosFacebookAux.size()>0)
					 {
						 Boolean existe;
						 for (Usuario uAux : listaAmigosFacebookAux)
						 {
							existe = amigosUsuarioDAO.getAmigosUsuario(getUsuarioGlobal().getPkUsuario() , uAux.getPkUsuario());
							if(existe==null || !existe)
							{
								listaAmigosFacebook.add(uAux);
							}
						 }
					 }

					 if(listaAmigosFacebook==null || listaAmigosFacebook.size()==0)		{   listaAmigosFacebook = null; 	}
				}
				
				listaAmigos = amigosUsuarioDAO.getAmigosUsuario(getUsuarioGlobal().getPkUsuario());
				if(listaAmigos!=null && listaAmigos.size()>0)
				{
					possuiAmizades = true;
				}
				else
				{
					listaAmigos = null;
					possuiAmizades = false;
				}
				
				
				listaRequisicoes = requisicaoAmizadeDAO.getRequisicoesAmizadeUsuario(getUsuarioGlobal().getPkUsuario());
				if(listaRequisicoes!=null && listaRequisicoes.size()>0)
				{
					solicitacoesAmizade = false;
				}
				else
				{
					listaRequisicoes = null;
					solicitacoesAmizade = true;
				}
			}
			else
			{
				encerrarSessao();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void redirecionaPaginaUsuario(String pkUsuario)
	{
		try
		{
			if(pkUsuario!=null && pkUsuario.length()>0)
			{
				//FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/playlistSelecionada/index.xhtml?t="+ pkPlaylist);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void responderRequisicaoAmizade(Usuario usuario, String respBoolean)
	{
		try
		{
			ConectaBanco.getInstance().beginTransaction();
			Boolean adicionou = null;
			
			if(respBoolean.equals("true"))
			{
				adicionou = true;
			}
			else if(respBoolean.equals("false"))
			{
				adicionou = false;
			}
			
			requisicaoAmizadeDAO.salvaRespostaRequisicao(getUsuarioGlobal(), usuario, adicionou);
			
			if(adicionou)
			{
				AmigosUsuario am1;
				AmigosUsuario am2;
				
				am1 = new AmigosUsuario();
				am1.setUsuario(getUsuarioGlobal());
				am1.setAmigo(usuario);
				
				am2 = new AmigosUsuario();
				am2.setUsuario(usuario);
				am2.setAmigo(getUsuarioGlobal());
				
				amigosUsuarioDAO.salvarAmigos(am1, am2);
				
				if(listaAmigos==null)
				{
					listaAmigos = new ArrayList<Usuario>();
					possuiAmizades = true;
				}
				else if(listaAmigos!=null && listaAmigos.size()==0)
				{
					possuiAmizades = true;
				}
				
				listaAmigos.add(usuario);
			}

			listaRequisicoes.remove(usuario);
			
			if((listaRequisicoes==null) || (listaRequisicoes!=null && listaRequisicoes.size()==0))
			{
				listaRequisicoes = null;
				solicitacoesAmizade = true;
			}
			
			ConectaBanco.getInstance().commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void procurarAmigosSistema()
	{
		try
		{
			listaUsuarioProcurados = new ArrayList<Usuario>();
			listaUsuarioProcurados = usuarioDAO.procurarUsuarios(nomeAmigo);
			if(listaUsuarioProcurados!=null && listaUsuarioProcurados.size()==0)	{    listaUsuarioProcurados = null;	}
			
			this.procurarAmigos = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void onTabChange(TabChangeEvent event) {
		TabView tabView = (TabView) event.getComponent();
		tabActiveIndex = tabView.getChildren().indexOf(event.getTab());
		
	}
	
	public Integer getTabActiveIndex() {
		return tabActiveIndex;
	}

	public void setTabActiveIndex(Integer tabActiveIndex) {
		this.tabActiveIndex = tabActiveIndex;
	}

	public String getIdentificacaoUsuario() {
		return identificacaoUsuario;
	}

	public void setIdentificacaoUsuario(String identificacaoUsuario) {
		this.identificacaoUsuario = identificacaoUsuario;
	}

	public String getNomeAmigo() {
		return nomeAmigo;
	}

	public void setNomeAmigo(String nomeAmigo) {
		this.nomeAmigo = nomeAmigo;
	}

	public List<String> getListaNomesMusica() {
		return listaNomesMusica;
	}

	public void setListaNomesMusica(List<String> listaNomesMusica) {
		this.listaNomesMusica = listaNomesMusica;
	}

	public boolean isProcurarAmigos() {
		return procurarAmigos;
	}

	public void setProcurarAmigos(boolean procurarAmigos) {
		this.procurarAmigos = procurarAmigos;
	}

	public List<Usuario> getListaAmigosFacebook() {
		return listaAmigosFacebook;
	}

	public void setListaAmigosFacebook(List<Usuario> listaAmigosFacebook) {
		this.listaAmigosFacebook = listaAmigosFacebook;
	}

	public List<Usuario> getListaAmigos() {
		return listaAmigos;
	}

	public void setListaAmigos(List<Usuario> listaAmigos) {
		this.listaAmigos = listaAmigos;
	}

	public List<Usuario> getListaRequisicoes() {
		return listaRequisicoes;
	}

	public void setListaRequisicoes(List<Usuario> listaRequisicoes) {
		this.listaRequisicoes = listaRequisicoes;
	}

	public List<Usuario> getListaUsuarioProcurados() {
		return listaUsuarioProcurados;
	}

	public void setListaUsuarioProcurados(List<Usuario> listaUsuarioProcurados) {
		this.listaUsuarioProcurados = listaUsuarioProcurados;
	}

	public boolean isSolicitacoesAmizade() {
		return solicitacoesAmizade;
	}

	public void setSolicitacoesAmizade(boolean solicitacoesAmizade) {
		this.solicitacoesAmizade = solicitacoesAmizade;
	}

	public boolean isPossuiAmizades() {
		return possuiAmizades;
	}

	public void setPossuiAmizades(boolean possuiAmizades) {
		this.possuiAmizades = possuiAmizades;
	}
}