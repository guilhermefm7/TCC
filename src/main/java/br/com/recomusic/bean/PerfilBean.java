package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

import br.com.recomusic.dao.AmigosUsuarioDAO;
import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.PlaylistDAO;
import br.com.recomusic.dao.RequisicaoAmizadeDAO;
import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.AmigosUsuario;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.Playlist;
import br.com.recomusic.om.RequisicaoAmizade;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="PerfilBean")
@ViewScoped
public class PerfilBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Usuario usuario = null;
	private Integer tabActiveIndex;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private AmigosUsuarioDAO amigosUsuarioDAO = new AmigosUsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private RequisicaoAmizadeDAO requisicaoAmizadeDAO = new RequisicaoAmizadeDAO( ConectaBanco.getInstance().getEntityManager());
	private AvaliarMusicaDAO avaliarMusicaDAO = new AvaliarMusicaDAO( ConectaBanco.getInstance().getEntityManager());
	private PlaylistDAO playlistDAO = new PlaylistDAO( ConectaBanco.getInstance().getEntityManager());
	private String tokenPkUsuairo = null;
	private List<Musica> listaMusicaUsuario = null;
	private List<Usuario> listaAmigosUsuario = null;
	private List<Playlist> listaPlaylistsUsuario = null;
	private Boolean amigo = null;
	private Boolean requisitouAmizade = false;
	
	public PerfilBean() {	}

	public void iniciar()
	{
		try
		{
			if(tokenPkUsuairo!=null && tokenPkUsuairo.length()>0)
			{
				if(UtilidadesTelas.verificarSessao())
				{
					setUsuarioGlobal(getUsuarioGlobal());
					
					usuario = usuarioDAO.getUsuarioPk(Long.valueOf(tokenPkUsuairo));
					if(usuario!=null && usuario.getPkUsuario()>0)
					{
						//Verifica se é o usuário atual
						if(usuario.getPkUsuario()==getUsuarioGlobal().getPkUsuario())
						{
							amigo = null;
							FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/perfil/index.xhtml");
							
							//Procura as Playlists
							listaPlaylistsUsuario = playlistDAO.getPlaylistsUsuario(getUsuarioGlobal());
							if(listaPlaylistsUsuario!=null && listaPlaylistsUsuario.size()==0){listaPlaylistsUsuario=null;}
							
							//Procura os Amigos
							listaMusicaUsuario = avaliarMusicaDAO.getAvaliacoesUsuarioPorData(getUsuarioGlobal());
							if(listaMusicaUsuario!=null && listaMusicaUsuario.size()==0){listaMusicaUsuario=null;}
							
							//Procura as Musicas Avaliadas
							listaAmigosUsuario = amigosUsuarioDAO.getAmigosUsuario(getUsuarioGlobal().getPkUsuario());
							if(listaPlaylistsUsuario!=null && listaPlaylistsUsuario.size()==0){listaPlaylistsUsuario=null;}
						}
						else
						{
							//Procura se o usuário atual é amigo do usuário do perfil
							Boolean amizade = amigosUsuarioDAO.getAmigosUsuario(getUsuarioGlobal().getPkUsuario(), usuario.getPkUsuario());
							
							if(amizade!=null && amizade)
							{
								//Caso o usuário seja amigo desse usuário, procurar seus amigos, playlist e últimas músicas avalidas para mostrar na tela
								amigo = true;
								
								//Procura as Playlists
								listaPlaylistsUsuario = playlistDAO.getPlaylistsUsuario(usuario);
								if(listaPlaylistsUsuario!=null && listaPlaylistsUsuario.size()==0){listaPlaylistsUsuario=null;}
								
								//Procura os Amigos
								listaMusicaUsuario = avaliarMusicaDAO.getAvaliacoesUsuarioPorData(usuario);
								if(listaMusicaUsuario!=null && listaMusicaUsuario.size()==0){listaMusicaUsuario=null;}
								
								//Procura as Musicas Avaliadas
								listaAmigosUsuario = amigosUsuarioDAO.getAmigosUsuario(usuario.getPkUsuario());
								if(listaPlaylistsUsuario!=null && listaPlaylistsUsuario.size()==0){listaPlaylistsUsuario=null;}
							}
							else
							{
								//Procura para ver se o usuário global já fez alguma requisição de amizade para o usuário deste perfil
								amigo = false;
								RequisicaoAmizade req = null;
								req = requisicaoAmizadeDAO.procuraRequisicao(usuario, getUsuarioGlobal());
								
								if(req!=null && req.getPkRequisicaoAmizade()>0)
								{
									requisitouAmizade = true;
								}
							}
						}
					}
					else
					{
						redirecionarErro();
					}
				}
				else
				{
					encerrarSessao();
				}
			}
			else
			{
				//Caso seja o usuário atual
				setUsuario(getUsuarioGlobal());
				amigo = null;
				
				//Procura as Playlists
				listaPlaylistsUsuario = playlistDAO.getPlaylistsUsuario(getUsuarioGlobal());
				if(listaPlaylistsUsuario!=null && listaPlaylistsUsuario.size()==0){listaPlaylistsUsuario=null;}
				
				//Procura os Amigos
				listaMusicaUsuario = avaliarMusicaDAO.getAvaliacoesUsuarioPorData(getUsuarioGlobal());
				if(listaMusicaUsuario!=null && listaMusicaUsuario.size()==0){listaMusicaUsuario=null;}
				
				//Procura as Musicas Avaliadas
				listaAmigosUsuario = amigosUsuarioDAO.getAmigosUsuario(getUsuarioGlobal().getPkUsuario());
				if(listaPlaylistsUsuario!=null && listaPlaylistsUsuario.size()==0){listaPlaylistsUsuario=null;}
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
	
	public void adiconarAmigo()
	{
		try
		{
			ConectaBanco.getInstance().beginTransaction();
			RequisicaoAmizade reqAmizade = new RequisicaoAmizade();
			reqAmizade.setLancamento(new Date());
			reqAmizade.setResposta(null);
			reqAmizade.setUsuarioRequisitado(usuario);
			reqAmizade.setUsuarioRequisitante(getUsuarioGlobal());
			
			requisicaoAmizadeDAO.salvaRequisicaoBD(reqAmizade);
			
			ConectaBanco.getInstance().commit();
			
			requisitouAmizade = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void desfazerAmizade()
	{
		try
		{
			AmigosUsuario am1 = null;
			AmigosUsuario am2 = null;
			
			am1 = amigosUsuarioDAO.getOneAmigosUsuario(getUsuarioGlobal().getPkUsuario(), getUsuario().getPkUsuario());
			am2 = amigosUsuarioDAO.getOneAmigosUsuario(getUsuario().getPkUsuario(), getUsuarioGlobal().getPkUsuario());
			
			ConectaBanco.getInstance().beginTransaction();
			
			if(am1!=null && am1.getPkAmigosUsuario()>0)
			{
				amigosUsuarioDAO.removerAmigosUsuario(am1);
			}
			
			if(am2!=null && am2.getPkAmigosUsuario()>0 )
			{
				amigosUsuarioDAO.removerAmigosUsuario(am2);
			}
			
			ConectaBanco.getInstance().commit();
			
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTokenPkUsuairo() {
		return tokenPkUsuairo;
	}

	public void setTokenPkUsuairo(String tokenPkUsuairo) {
		this.tokenPkUsuairo = tokenPkUsuairo;
	}

	public List<Musica> getListaMusicaUsuario() {
		return listaMusicaUsuario;
	}

	public void setListaMusicaUsuario(List<Musica> listaMusicaUsuario) {
		this.listaMusicaUsuario = listaMusicaUsuario;
	}

	public List<Usuario> getListaAmigosUsuario() {
		return listaAmigosUsuario;
	}

	public void setListaAmigosUsuario(List<Usuario> listaAmigosUsuario) {
		this.listaAmigosUsuario = listaAmigosUsuario;
	}

	public List<Playlist> getListaPlaylistsUsuario() {
		return listaPlaylistsUsuario;
	}

	public void setListaPlaylistsUsuario(List<Playlist> listaPlaylistsUsuario) {
		this.listaPlaylistsUsuario = listaPlaylistsUsuario;
	}

	public Boolean getAmigo() {
		return amigo;
	}

	public void setAmigo(Boolean amigo) {
		this.amigo = amigo;
	}

	public Boolean getRequisitouAmizade() {
		return requisitouAmizade;
	}

	public void setRequisitouAmizade(Boolean requisitouAmizade) {
		this.requisitouAmizade = requisitouAmizade;
	}
}