package br.com.recomusic.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

import br.com.recomusic.dao.AmigosUsuarioDAO;
import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.PlaylistDAO;
import br.com.recomusic.dao.RequisicaoAmizadeDAO;
import br.com.recomusic.dao.TrocarFotoDAO;
import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.im.MusicaAvaliadaIM;
import br.com.recomusic.im.PlaylistIM;
import br.com.recomusic.om.AmigosUsuario;
import br.com.recomusic.om.Playlist;
import br.com.recomusic.om.RequisicaoAmizade;
import br.com.recomusic.om.TrocarFoto;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;

@ManagedBean(name = "PerfilBean")
@ViewScoped
public class PerfilBean extends UtilidadesTelas implements Serializable {
	private static final long serialVersionUID = 1L;
	private Usuario usuario = null;
	private Integer tabActiveIndex;
	private UsuarioDAO usuarioDAO = new UsuarioDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private AmigosUsuarioDAO amigosUsuarioDAO = new AmigosUsuarioDAO(
			ConectaBanco.getInstance().getEntityManager());
	private RequisicaoAmizadeDAO requisicaoAmizadeDAO = new RequisicaoAmizadeDAO(
			ConectaBanco.getInstance().getEntityManager());
	private AvaliarMusicaDAO avaliarMusicaDAO = new AvaliarMusicaDAO(
			ConectaBanco.getInstance().getEntityManager());
	private PlaylistDAO playlistDAO = new PlaylistDAO(ConectaBanco
			.getInstance().getEntityManager());
	private String tokenPkUsuairo = null;
	private List<MusicaAvaliadaIM> listaMusicaUsuario = null;
	private List<Usuario> listaAmigosUsuario = null;
	private List<Playlist> listaPlaylistsUsuario = null;
	private Boolean amigo = null;
	private Boolean requisitouAmizade = false;
	private boolean disabled = false;
	private List<PlaylistIM> listaPIM = null;
	private TrocarFotoDAO trocarFotoDAO = new TrocarFotoDAO(ConectaBanco
			.getInstance().getEntityManager());
	private TrocarFoto trocarFoto = null;
	
	public PerfilBean() {
		this.FILE_PREFIX = "DOCUMENTO_";
	}

	public void iniciar() {
		try {
			if (UtilidadesTelas.verificarSessao()) {
				trocarFoto = null;
				if (tokenPkUsuairo != null && tokenPkUsuairo.length() > 0) {
					usuario = usuarioDAO.getUsuarioPk(Long
							.valueOf(tokenPkUsuairo));
					if (usuario != null && usuario.getPkUsuario() > 0) {
						// Verifica se é o usuário atual
						if (usuario.getPkUsuario() == getUsuarioGlobal()
								.getPkUsuario()) {
							
							amigo = null;
							disabled = false;
							// Procura as Playlists
							listaPlaylistsUsuario = playlistDAO
									.getPlaylistsUsuario(getUsuarioGlobal());
							if (listaPlaylistsUsuario != null
									&& listaPlaylistsUsuario.size() == 0) {
								listaPlaylistsUsuario = null;
							} else if (listaPlaylistsUsuario != null
									&& listaPlaylistsUsuario.size() > 0) {
								listaPIM = new ArrayList<PlaylistIM>();
								PlaylistIM pIM;
								DateFormat dateFormat = new SimpleDateFormat(
										"dd/MM/yyyy");
								for (Playlist playlist : listaPlaylistsUsuario) {
									pIM = new PlaylistIM();
									pIM.setNomePlaylist(playlist.getNome());
									pIM.setDataLancamento(dateFormat
											.format(playlist.getLancamento()));
									pIM.setQtdFaixas(playlist
											.getNumeroMusicas());
									pIM.setPkPlaylist(Long.toString(playlist
											.getPkPlaylist()));
									listaPIM.add(pIM);
								}
							}

							// Procura os Amigos
							listaMusicaUsuario = avaliarMusicaDAO
									.getAvaliacoesUsuario(getUsuarioGlobal());
							if (listaMusicaUsuario != null
									&& listaMusicaUsuario.size() == 0) {
								listaMusicaUsuario = null;
							}

							// Procura as Musicas Avaliadas
							listaAmigosUsuario = amigosUsuarioDAO
									.getAmigosUsuario(getUsuarioGlobal()
											.getPkUsuario());
							if (listaAmigosUsuario != null
									&& listaAmigosUsuario.size() == 0) {
								listaAmigosUsuario = null;
							}
						} else {
							// Procura se o usuário atual é amigo do usuário do
							// perfil
							Boolean amizade = amigosUsuarioDAO
									.getAmigosUsuario(getUsuarioGlobal()
											.getPkUsuario(), usuario
											.getPkUsuario());

							if (amizade != null && amizade) {
								// Caso o usuário seja amigo desse usuário,
								// procurar seus amigos, playlist e últimas
								// músicas avalidas para mostrar na tela
								amigo = true;
								disabled = false;
								// Procura as Playlists
								listaPlaylistsUsuario = playlistDAO
										.getPlaylistsUsuario(usuario);
								if (listaPlaylistsUsuario != null
										&& listaPlaylistsUsuario.size() == 0) {
									listaPlaylistsUsuario = null;
								} else if (listaPlaylistsUsuario != null
										&& listaPlaylistsUsuario.size() > 0) {
									listaPIM = new ArrayList<PlaylistIM>();
									PlaylistIM pIM;
									DateFormat dateFormat = new SimpleDateFormat(
											"dd/MM/yyyy");
									for (Playlist playlist : listaPlaylistsUsuario) {
										pIM = new PlaylistIM();
										pIM.setNomePlaylist(playlist.getNome());
										pIM.setDataLancamento(dateFormat
												.format(playlist
														.getLancamento()));
										pIM.setQtdFaixas(playlist
												.getNumeroMusicas());
										pIM.setPkPlaylist(Long
												.toString(playlist
														.getPkPlaylist()));
										listaPIM.add(pIM);
									}
								}

								// Procura os Amigos
								listaMusicaUsuario = avaliarMusicaDAO
										.getAvaliacoesUsuario(usuario);
								if (listaMusicaUsuario != null
										&& listaMusicaUsuario.size() == 0) {
									listaMusicaUsuario = null;
								}

								// Procura as Musicas Avaliadas
								listaAmigosUsuario = amigosUsuarioDAO
										.getAmigosUsuario(usuario
												.getPkUsuario());
								if (listaAmigosUsuario != null
										&& listaAmigosUsuario.size() == 0) {
									listaAmigosUsuario = null;
								}
							} else {
								// Procura para ver se o usuário global já fez
								// alguma requisição de amizade para o usuário
								// deste perfil
								amigo = false;
								disabled = true;
								RequisicaoAmizade req = null;
								req = requisicaoAmizadeDAO.procuraRequisicao(
										usuario, getUsuarioGlobal());

								if (req != null
										&& req.getPkRequisicaoAmizade() > 0) {
									requisitouAmizade = true;
								}
							}
						}
					} else {
						redirecionarErro();
					}
				} else {
					// Caso seja o usuário atual
					setUsuario(getUsuarioGlobal());
					amigo = null;
					trocarFoto = trocarFotoDAO
							.getTrocarFotoUsuario(getUsuarioGlobal());
					// Procura as Playlists
					listaPlaylistsUsuario = playlistDAO
							.getPlaylistsUsuario(getUsuarioGlobal());
					if (listaPlaylistsUsuario != null
							&& listaPlaylistsUsuario.size() == 0) {
						listaPlaylistsUsuario = null;
					} else if (listaPlaylistsUsuario != null
							&& listaPlaylistsUsuario.size() > 0) {
						listaPIM = new ArrayList<PlaylistIM>();
						PlaylistIM pIM;
						DateFormat dateFormat = new SimpleDateFormat(
								"dd/MM/yyyy");
						for (Playlist playlist : listaPlaylistsUsuario) {
							pIM = new PlaylistIM();
							pIM.setNomePlaylist(playlist.getNome());
							pIM.setDataLancamento(dateFormat.format(playlist
									.getLancamento()));
							pIM.setQtdFaixas(playlist.getNumeroMusicas());
							pIM.setPkPlaylist(Long.toString(playlist
									.getPkPlaylist()));
							listaPIM.add(pIM);
						}
					}

					// Procura os Amigos
					listaMusicaUsuario = avaliarMusicaDAO
							.getAvaliacoesUsuario(getUsuarioGlobal());
					if (listaMusicaUsuario != null
							&& listaMusicaUsuario.size() == 0) {
						listaMusicaUsuario = null;
					}

					// Procura as Musicas Avaliadas
					listaAmigosUsuario = amigosUsuarioDAO
							.getAmigosUsuario(getUsuarioGlobal().getPkUsuario());
					if (listaAmigosUsuario != null
							&& listaAmigosUsuario.size() == 0) {
						listaAmigosUsuario = null;
					}
				}
			} else {
				encerrarSessao();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void redirecionaPaginaUsuario(String pkUsuario) {
		try {
			if (pkUsuario != null && pkUsuario.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/perfil/index.xhtml?t="
										+ pkUsuario);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void redirecionaPaginaMusica(String idMusica, String nomeMusica,
			String artistaBandaMusica, String album, String idEcho) {
		try {
			if (album != null && album.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ idMusica + "&m=" + nomeMusica + "&a="
										+ artistaBandaMusica + "&i=" + idEcho
										+ "&n=" + album);
			} else {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ idMusica + "&m=" + nomeMusica + "&a="
										+ artistaBandaMusica + "&i=" + idEcho);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void redirecionaPlaylist(String pkPlaylist) {
		try {
			if (pkPlaylist != null && pkPlaylist.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/playlistSelecionada/index.xhtml?t="
										+ pkPlaylist);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void adiconarAmigo() {
		try {
			ConectaBanco.getInstance().beginTransaction();
			RequisicaoAmizade reqAmizade = new RequisicaoAmizade();
			reqAmizade.setLancamento(new Date());
			reqAmizade.setResposta(null);
			reqAmizade.setUsuarioRequisitado(usuario);
			reqAmizade.setUsuarioRequisitante(getUsuarioGlobal());

			requisicaoAmizadeDAO.salvaRequisicaoBD(reqAmizade);

			ConectaBanco.getInstance().commit();

			requisitouAmizade = true;
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void desfazerAmizade() {
		try {
			AmigosUsuario am1 = null;
			AmigosUsuario am2 = null;

			am1 = amigosUsuarioDAO.getOneAmigosUsuario(getUsuarioGlobal()
					.getPkUsuario(), getUsuario().getPkUsuario());
			am2 = amigosUsuarioDAO.getOneAmigosUsuario(getUsuario()
					.getPkUsuario(), getUsuarioGlobal().getPkUsuario());

			ConectaBanco.getInstance().beginTransaction();

			if (am1 != null && am1.getPkAmigosUsuario() > 0) {
				amigosUsuarioDAO.removerAmigosUsuario(am1);
			}

			if (am2 != null && am2.getPkAmigosUsuario() > 0) {
				amigosUsuarioDAO.removerAmigosUsuario(am2);
			}

			ConectaBanco.getInstance().commit();

		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void uploadFoto(FileUploadEvent event) throws FileNotFoundException, IOException{
		try {

			ConectaBanco.getInstance().beginTransaction();
			TrocarFoto trocarFotoAux = new TrocarFoto();
			TrocarFoto trocarFotoRemove = null;
			
			trocarFotoRemove = trocarFotoDAO
					.getTrocarFotoUsuario(getUsuarioGlobal());
			
			if(trocarFotoRemove!=null && trocarFotoRemove.getPkTrocarFoto()>0)
			{
				deleteFoto(trocarFotoRemove.getNome(), FILE_PATH);
				deleteFoto(trocarFotoRemove.getPathFotoImagem(), FILE_PATH);
				trocarFotoDAO.delete(trocarFotoRemove);
			}
			
			String finalname = getFileName(event.getFile().getFileName(), event
					.getFile().getContentType());

			trocarFotoAux.setNome(finalname);
			trocarFotoAux.setPathFoto(FILE_PATH + finalname);

			trocarFotoAux.setTipo(event.getFile().getContentType());
			trocarFotoAux.setTamanho(String.valueOf(event.getFile().getSize()));
			trocarFotoAux.setInputStream(event.getFile().getInputstream());
			trocarFotoAux.setUsuario(getUsuarioGlobal());
			uploadFile(trocarFotoAux);
			trocarFotoDAO.save(trocarFotoAux);
			ConectaBanco.getInstance().commit();
			

		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
			addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR);
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

	public List<MusicaAvaliadaIM> getListaMusicaUsuario() {
		return listaMusicaUsuario;
	}

	public void setListaMusicaUsuario(List<MusicaAvaliadaIM> listaMusicaUsuario) {
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<PlaylistIM> getListaPIM() {
		return listaPIM;
	}

	public void setListaPIM(List<PlaylistIM> listaPIM) {
		this.listaPIM = listaPIM;
	}

	public TrocarFoto getTrocarFoto() {
		return trocarFoto;
	}

	public void setTrocarFoto(TrocarFoto trocarFoto) {
		this.trocarFoto = trocarFoto;
	}
}