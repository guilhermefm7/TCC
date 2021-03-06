package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.BandaDAO;
import br.com.recomusic.dao.BandaGeneroDAO;
import br.com.recomusic.dao.GeneroDAO;
import br.com.recomusic.dao.InformacaoMusicalCadastroBandaDAO;
import br.com.recomusic.dao.InformacaoMusicalCadastroGeneroDAO;
import br.com.recomusic.dao.InformacaoMusicalCadastroMusicaDAO;
import br.com.recomusic.dao.MediaUsuarioGeneroDAO;
import br.com.recomusic.dao.MusicaDAO;
import br.com.recomusic.dao.PlaylistDAO;
import br.com.recomusic.dao.PlaylistMusicaDAO;
import br.com.recomusic.dao.PossivelAvaliacaoMusicaDAO;
import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.AvaliarMusica;
import br.com.recomusic.om.Banda;
import br.com.recomusic.om.BandaGenero;
import br.com.recomusic.om.Genero;
import br.com.recomusic.om.InformacaoMusicalCadastroBanda;
import br.com.recomusic.om.InformacaoMusicalCadastroGenero;
import br.com.recomusic.om.InformacaoMusicalCadastroMusica;
import br.com.recomusic.om.MediaUsuarioGenero;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.Playlist;
import br.com.recomusic.om.PlaylistMusica;
import br.com.recomusic.om.PossivelAvaliacaoMusica;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Constantes;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;

@ManagedBean(name = "MusicaBean")
@ViewScoped
public class MusicaBean extends UtilidadesTelas implements Serializable {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private MusicaDAO musicaDAO = new MusicaDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private PlaylistDAO playlistDAO = new PlaylistDAO(ConectaBanco
			.getInstance().getEntityManager());
	private PlaylistMusicaDAO playlistMusicaDAO = new PlaylistMusicaDAO(
			ConectaBanco.getInstance().getEntityManager());
	private BandaDAO bandaDAO = new BandaDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private GeneroDAO generoDAO = new GeneroDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private PossivelAvaliacaoMusicaDAO possivelAvaliacaoMusicaDAO = new PossivelAvaliacaoMusicaDAO(
			ConectaBanco.getInstance().getEntityManager());
	private MediaUsuarioGeneroDAO mediaUsuarioGeneroDAO = new MediaUsuarioGeneroDAO(
			ConectaBanco.getInstance().getEntityManager());
	private BandaGeneroDAO bandaGeneroDAO = new BandaGeneroDAO(ConectaBanco
			.getInstance().getEntityManager());
	private InformacaoMusicalCadastroMusicaDAO informacaoMusicalCadastroMusicaDAO = new InformacaoMusicalCadastroMusicaDAO(
			ConectaBanco.getInstance().getEntityManager());
	private InformacaoMusicalCadastroBandaDAO informacaoMusicalCadastroBandaDAO = new InformacaoMusicalCadastroBandaDAO(
			ConectaBanco.getInstance().getEntityManager());
	private InformacaoMusicalCadastroGeneroDAO informacaoMusicalCadastroGeneroDAO = new InformacaoMusicalCadastroGeneroDAO(
			ConectaBanco.getInstance().getEntityManager());
	private AvaliarMusicaDAO avaliarMusicaDAO = new AvaliarMusicaDAO(
			ConectaBanco.getInstance().getEntityManager());
	private Usuario usuario = null;
	private String nomeCompletoMusica = null;
	private String nomeMusica = null;
	private String nomeArtista = null;
	private String valorUrlMusica = null;
	private String nomeAlbum = null;
	private boolean curtiuMusica = false;
	private String valorIdMusica;
	private String valorIdMusicaEcho;
	private int notaMusica = 0;
	private AvaliarMusica avaliarMusicaPrincipal = null;
	private String tokenRecebido = null;
	private String valorIdMusicaEchoAux = null;
	private List<Playlist> listaPlaylists = null;
	private List<Musica> musicasRecomendadas = null;

	public MusicaBean() {
	}

	/**
	 * Responsavel por iniciar a pagina desktop.xhtml, carregando tdo oq ela
	 * precisa, verificando sessao, etc.
	 */
	public void iniciar() {
		try {
			if ((valorIdMusica != null && valorIdMusica.length() > 0)
					&& (nomeMusica != null && nomeMusica.length() > 0)
					&& (valorIdMusicaEcho != null && valorIdMusicaEcho.length() > 0)) {

				boolean flag = false;
				if (valorIdMusicaEcho == null) {
					flag = true;
				}

				this.valorIdMusicaEchoAux = valorIdMusicaEcho;

				this.notaMusica = 0;
				if (UtilidadesTelas.verificarSessao()) {
					setUsuario(getUsuarioGlobal());

					if (nomeArtista != null && nomeArtista.length() > 0) {
						nomeCompletoMusica = nomeMusica + " - " + nomeArtista;
					} else {
						nomeCompletoMusica = nomeMusica;
					}

					// Chama a fun��o para atualizar a quantidade de vezes que o
					// usu�rio procurou esta m�sica, caso seja maior ou igual a
					// 5...Ser� atribuido uma nota 3 por parte do usu�rio a esta
					// m�sica
					atualizarQuantidadeVezesProcurada();
					if (!flag) {
						carregarListaMusicasRecomendadas();
					}
					// Procura as Playlists para que o usu�rio, caso queira,
					// possa adicionar a m�sica
					listaPlaylists = playlistDAO
							.getPlaylistsUsuario(getUsuarioGlobal());

					// Seta a Playlist como Null para n�o dar erro no JSF
					if (listaPlaylists == null || listaPlaylists.size() == 0) {
						listaPlaylists = null;
					}
					AvaliarMusica am = null;
					am = avaliarMusicaDAO.pesquisaUsuarioAvaliouMusica(
							valorIdMusica, getUsuarioGlobal());

					if (am != null && am.getPkAvaliarMusica() > 0) {
						if (am.getResposta() != null && am.getResposta()) {
							this.notaMusica = am.getNota();
							this.curtiuMusica = true;
						} else {
							this.curtiuMusica = false;
						}
					} else {
						this.curtiuMusica = false;
					}
				} else {
					encerrarSessao();
				}
			} else {
				redirecionarErro();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void avaliarMusicaPositivamente() { try {
	 * if(getUsuarioGlobal()!=null) { Musica musica = null; musica =
	 * musicaDAO.procuraMusicaByID(valorIdMusica); if(musica==null) { musica =
	 * pesquisaMusica(valorIdMusica); if(musica!=null) {
	 * ConectaBanco.getInstance().beginTransaction(); musicaDAO.save(musica);
	 * ConectaBanco.getInstance().commit(); } }
	 * 
	 * if(musica!=null) { ConectaBanco.getInstance().beginTransaction();
	 * AvaliarMusica am = avaliarMusicaDAO.getAvaliacaoUsuario(musica,
	 * getUsuarioGlobal(), 1); ConectaBanco.getInstance().commit();
	 * avaliarMusicaPrincipal = am; //curtiuMusica = !curtiuMusica;
	 * 
	 * 
	 * if(am.getResposta()==null) { setCurtiuMusica(false); } else {
	 * if(am.getResposta()==true) { setCurtiuMusica(true); } }
	 * System.out.println("Curtiu " + isCurtiuMusica() + "N�o Curtiu " +
	 * isNaoCurtiuMusica()); } } } catch(Exception e) {
	 * ConectaBanco.getInstance().rollBack(); e.printStackTrace(); } }
	 */

	/**
	 * Avalia uma m�sica (Curti ou Descurti uma m�sica)
	 */
	public void avaliarMusica(String nota) {
		try {
			Musica m = null;
			Banda banda = null;
			Genero genero = null;
			BandaGenero bg = null;
			InformacaoMusicalCadastroMusica imcm = null;
			InformacaoMusicalCadastroBanda imcb = null;
			InformacaoMusicalCadastroGenero imcg = null;
			AvaliarMusica am = null;

			ConectaBanco.getInstance().beginTransaction();

			// Caso a m�sica ainda n�o tenha sido curtida
			if (!this.curtiuMusica) {
				m = musicaDAO.procuraMusicaByID(valorIdMusica);

				if (m != null && m.getPkMusica() > 0) {
					// Caso a m�sica j� exista
					// Pesquisa se a banda existe
					banda = bandaDAO.pesquisarBandaExiste(m.getBanda()
							.getIdBanda());

					if (banda != null && banda.getPkBanda() > 0) {
						// Caso exista, atualiza as informa��es do cadastro de
						// banda caso existam ou cria nova caso seja necess�rio
						imcb = null;
						imcb = informacaoMusicalCadastroBandaDAO.pesquisarIMCB(
								getUsuarioGlobal(), banda);

						if (imcb != null
								&& imcb.getPkInformacaoMusicalCadastroBanda() > 0) {
							if (imcb.getStatus() == Constantes.STATUS_BANDA_FOI_CURTIDA) {
								imcb.setStatus(Constantes.TIPO_STATUS_ATIVO);
								imcb.setQuantidade(imcb.getQuantidade() + 1);
								informacaoMusicalCadastroBandaDAO
										.salvarBandasCadastro(imcb);
							} else if (imcb.getStatus() == Constantes.TIPO_STATUS_ATIVO) {
								imcb.setQuantidade(imcb.getQuantidade() + 1);
								informacaoMusicalCadastroBandaDAO
										.salvarBandasCadastro(imcb);
							}
						} else {
							imcb = new InformacaoMusicalCadastroBanda();
							imcb.setBanda(banda);
							imcb.setUsuario(getUsuarioGlobal());
							imcb.setQuantidade(1);
							imcb.setStatus(Constantes.TIPO_STATUS_ATIVO);
							informacaoMusicalCadastroBandaDAO
									.salvarBandasCadastro(imcb);
						}

						// Como a banda j� existem, ent�o a bandaGen�ro e os
						// g�neros j� existem
						// Ent�o s� precisar� de atualizar as informa��es do
						// cadastro de g�nero
						MediaUsuarioGenero mUG;
						List<String> listasGenerosBanda = requisitarAPIGeneroBanda(banda
								.getIdBanda());
						for (String nomeGenero : listasGenerosBanda) {
							genero = null;
							genero = generoDAO.pesquisarGenero(nomeGenero);

							if (genero == null) {
								genero = new Genero();
								genero.setNomeGenero(nomeGenero);
								genero = generoDAO
										.salvaListaGeneros(nomeGenero);
							}

							// Cria/Atualiza o MediaUsuarioGenero
							mUG = null;
							mUG = mediaUsuarioGeneroDAO.pesquisarExiste(
									getUsuarioGlobal(), genero);

							if (mUG == null) {
								mUG = new MediaUsuarioGenero();
								mUG.setGenero(genero);
								mUG.setUsuario(getUsuarioGlobal());
								mUG.setQuantidadeMusicas(1D);
								mUG.setMedia(m.getBPMMUsica());
								mUG.setMediaAvaliacoes(Double.valueOf(nota));
							} else {
								mUG.setQuantidadeMusicas(mUG
										.getQuantidadeMusicas() + 1);
								mUG.setMedia(((mUG.getMedia() * (mUG
										.getQuantidadeMusicas() - 1)) + m
										.getBPMMUsica())
										/ mUG.getQuantidadeMusicas());
								mUG.setMediaAvaliacoes(((mUG
										.getMediaAvaliacoes() * (mUG
										.getQuantidadeMusicas() - 1)) + Double
										.valueOf(nota))
										/ mUG.getQuantidadeMusicas());
							}

							mediaUsuarioGeneroDAO.salvaMediaUsuarioGenero(mUG);

							imcg = null;
							imcg = informacaoMusicalCadastroGeneroDAO
									.pesquisarIMCG(getUsuarioGlobal(), genero);
							if (imcg == null) {
								imcg = new InformacaoMusicalCadastroGenero();
								imcg.setUsuario(getUsuarioGlobal());
								imcg.setGenero(genero);
								imcg.setQuantidade(1);
								imcg.setStatus(Constantes.TIPO_STATUS_ATIVO);
								informacaoMusicalCadastroGeneroDAO
										.salvarIMCG(imcg);
							} else {
								if (imcg.getStatus() == Constantes.STATUS_GENERO_FOI_CURTIDO) {
									imcg.setQuantidade(imcg.getQuantidade() + 1);
									imcg.setStatus(Constantes.TIPO_STATUS_ATIVO);
									informacaoMusicalCadastroGeneroDAO
											.salvarIMCG(imcg);
								} else if (imcg.getStatus() == Constantes.TIPO_STATUS_ATIVO) {
									imcg.setQuantidade(imcg.getQuantidade() + 1);
									informacaoMusicalCadastroGeneroDAO
											.salvarIMCG(imcg);
								}
							}
						}

						// Fim do if quando vai curtir uma m�sica que n�o
						// existem mas a banda j� existe
					} else {
						// Como a banda n�o existia ent�o cria a banda, cria os
						// g�neros que ainda n�o existem, as informa��es de
						// cadastro e os BandaGeneros
						banda = new Banda();
						banda = m.getBanda();
						bandaDAO.salvarBanda(banda);

						imcb = new InformacaoMusicalCadastroBanda();
						imcb.setBanda(banda);
						imcb.setUsuario(getUsuarioGlobal());
						imcb.setQuantidade(1);
						imcb.setStatus(Constantes.TIPO_STATUS_ATIVO);
						informacaoMusicalCadastroBandaDAO
								.salvarBandasCadastro(imcb);

						// Banda e Musica n�o existem, ent�o elas ter�o de ser
						// salvas e ter� que salvar os generos e criar todas as
						// informa��es de cadastro necess�ria
						MediaUsuarioGenero mUG;
						List<String> listasGenerosBanda = requisitarAPIGeneroBanda(banda
								.getIdBanda());
						for (String nomeGenero : listasGenerosBanda) {
							genero = null;
							genero = generoDAO.pesquisarGenero(nomeGenero);

							if (genero == null) {
								genero = new Genero();
								genero.setNomeGenero(nomeGenero);
								genero = generoDAO
										.salvaListaGeneros(nomeGenero);
							}

							// Cria/Atualiza o MediaUsuarioGenero
							mUG = null;
							mUG = mediaUsuarioGeneroDAO.pesquisarExiste(
									getUsuarioGlobal(), genero);

							if (mUG == null) {
								mUG = new MediaUsuarioGenero();
								mUG.setGenero(genero);
								mUG.setUsuario(getUsuarioGlobal());
								mUG.setQuantidadeMusicas(1D);
								mUG.setMedia(m.getBPMMUsica());
								mUG.setMediaAvaliacoes(Double.valueOf(nota));

							} else {
								mUG.setQuantidadeMusicas(mUG
										.getQuantidadeMusicas() + 1);
								mUG.setMedia(((mUG.getMedia() * (mUG
										.getQuantidadeMusicas() - 1)) + m
										.getBPMMUsica())
										/ mUG.getQuantidadeMusicas());
								mUG.setMediaAvaliacoes(((mUG
										.getMediaAvaliacoes() * (mUG
										.getQuantidadeMusicas() - 1)) + Double
										.valueOf(nota))
										/ mUG.getQuantidadeMusicas());
							}

							mediaUsuarioGeneroDAO.salvaMediaUsuarioGenero(mUG);

							bg = null;
							bg = bandaGeneroDAO.pesquisarBandaGenero(banda,
									genero);

							if (bg == null) {
								bg = new BandaGenero();
								bg.setBanda(banda);
								bg.setGenero(genero);
								bandaGeneroDAO.salvarBandaGenero(bg);
							}

							imcg = null;
							imcg = informacaoMusicalCadastroGeneroDAO
									.pesquisarIMCG(getUsuarioGlobal(), genero);
							if (imcg == null) {
								imcg = new InformacaoMusicalCadastroGenero();
								imcg.setUsuario(getUsuarioGlobal());
								imcg.setGenero(genero);
								imcg.setStatus(Constantes.TIPO_STATUS_ATIVO);
								imcg.setQuantidade(1);
								informacaoMusicalCadastroGeneroDAO
										.salvarIMCG(imcg);
							} else {
								if (imcg.getStatus() == Constantes.STATUS_GENERO_FOI_CURTIDO) {
									imcg.setStatus(Constantes.TIPO_STATUS_ATIVO);
									imcg.setQuantidade(imcg.getQuantidade() + 1);
									informacaoMusicalCadastroGeneroDAO
											.salvarIMCG(imcg);
								}
							}
						}

						// Fim do if que a banda n�o existia
					}

					imcm = null;
					imcm = informacaoMusicalCadastroMusicaDAO.pesquisarIMCM(
							getUsuarioGlobal(), m);

					if (imcm == null) {
						imcm = new InformacaoMusicalCadastroMusica();
						imcm.setMusica(m);
						imcm.setUsuario(getUsuarioGlobal());
						imcm.setStatus(Constantes.TIPO_STATUS_ATIVO);
						informacaoMusicalCadastroMusicaDAO
								.salvarMusicaCadastro(imcm);
					} else {
						if (imcm.getStatus() == Constantes.STATUS_MUSICA_FOI_CURTIDA) {
							imcm.setStatus(Constantes.TIPO_STATUS_ATIVO);
							informacaoMusicalCadastroMusicaDAO
									.salvarMusicaCadastro(imcm);
						}
					}

					am = null;
					am = avaliarMusicaDAO
							.pesquisaUsuarioAvaliouMusicaPelaMusica(m,
									getUsuarioGlobal());

					if (am == null) {
						m.setQuantidadeAvaliacoes(m.getQuantidadeAvaliacoes() + 1);
						m.setMediaAvaliacoes(((m.getMediaAvaliacoes() * (m
								.getQuantidadeAvaliacoes() - 1)) + (Integer
								.valueOf(nota)))
								/ m.getQuantidadeAvaliacoes());
						musicaDAO.salvarMusica(m);

						am = new AvaliarMusica();
						am.setMusica(m);
						am.setUsuario(getUsuarioGlobal());
						am.setStatus(Constantes.TIPO_STATUS_ATIVO);
						am.setResposta(true);
						am.setLancamento(new Date());
						am.setNota(Integer.valueOf(nota));
						avaliarMusicaDAO.salvarAvaliacao(am);
					} else {
						if (am.getResposta() != true) {
							m.setMediaAvaliacoes((m.getMediaAvaliacoes()
									- am.getNota() + Integer.valueOf(nota))
									/ m.getQuantidadeAvaliacoes());
							musicaDAO.salvarMusica(m);

							am.setResposta(true);
							am.setNota(Integer.valueOf(nota));
							avaliarMusicaDAO.salvarAvaliacao(am);
						}
					}

					// Fim do if quando vai curtir uma m�sica e essa m�sica j�
					// exista no BD
				} else {
					// Caso a m�sica n�o exista
					m = pesquisaMusica(valorIdMusicaEchoAux);

					// Pesquisa se a banda existe
					banda = bandaDAO.pesquisarBandaExiste(m.getBanda()
							.getIdBanda());

					if (banda != null && banda.getPkBanda() > 0) {
						// Caso exista, atualiza as informa��es do cadastro de
						// banda caso existam ou cria nova caso seja necess�rio
						imcb = null;
						imcb = informacaoMusicalCadastroBandaDAO.pesquisarIMCB(
								getUsuarioGlobal(), banda);

						if (imcb != null
								&& imcb.getPkInformacaoMusicalCadastroBanda() > 0) {
							if (imcb.getStatus() == Constantes.STATUS_BANDA_FOI_CURTIDA) {
								imcb.setStatus(Constantes.TIPO_STATUS_ATIVO);
								imcb.setQuantidade(imcb.getQuantidade() + 1);
								informacaoMusicalCadastroBandaDAO
										.salvarBandasCadastro(imcb);
							} else if (imcb.getStatus() == Constantes.TIPO_STATUS_ATIVO) {
								imcb.setQuantidade(imcb.getQuantidade() + 1);
								informacaoMusicalCadastroBandaDAO
										.salvarBandasCadastro(imcb);
							}
						} else {
							imcb = new InformacaoMusicalCadastroBanda();
							imcb.setBanda(banda);
							imcb.setUsuario(getUsuarioGlobal());
							imcb.setStatus(Constantes.TIPO_STATUS_ATIVO);
							imcb.setQuantidade(1);
							informacaoMusicalCadastroBandaDAO
									.salvarBandasCadastro(imcb);
						}

						// Como a banda j� existem, ent�o a bandaGen�ro e os
						// g�neros j� existem
						// Ent�o s� precisar� de atualizar as informa��es do
						// cadastro de g�nero
						MediaUsuarioGenero mUG;
						List<String> listasGenerosBanda = requisitarAPIGeneroBanda(banda
								.getIdBanda());
						for (String nomeGenero : listasGenerosBanda) {
							genero = null;
							genero = generoDAO.pesquisarGenero(nomeGenero);

							if (genero == null) {
								genero = new Genero();
								genero.setNomeGenero(nomeGenero);
								genero = generoDAO
										.salvaListaGeneros(nomeGenero);
							}

							// Cria/Atualiza o MediaUsuarioGenero
							mUG = null;
							mUG = mediaUsuarioGeneroDAO.pesquisarExiste(
									getUsuarioGlobal(), genero);

							if (mUG == null) {
								mUG = new MediaUsuarioGenero();
								mUG.setGenero(genero);
								mUG.setUsuario(getUsuarioGlobal());
								mUG.setQuantidadeMusicas(1D);
								mUG.setMedia(m.getBPMMUsica());
								mUG.setMediaAvaliacoes(Double.valueOf(nota));
							} else {
								mUG.setQuantidadeMusicas(mUG
										.getQuantidadeMusicas() + 1);
								mUG.setMedia(((mUG.getMedia() * (mUG
										.getQuantidadeMusicas() - 1)) + m
										.getBPMMUsica())
										/ mUG.getQuantidadeMusicas());
								mUG.setMediaAvaliacoes(((mUG
										.getMediaAvaliacoes() * (mUG
										.getQuantidadeMusicas() - 1)) + Double
										.valueOf(nota))
										/ mUG.getQuantidadeMusicas());
							}

							mediaUsuarioGeneroDAO.salvaMediaUsuarioGenero(mUG);

							imcg = null;
							imcg = informacaoMusicalCadastroGeneroDAO
									.pesquisarIMCG(getUsuarioGlobal(), genero);
							if (imcg == null) {
								imcg = new InformacaoMusicalCadastroGenero();
								imcg.setUsuario(getUsuarioGlobal());
								imcg.setGenero(genero);
								imcg.setStatus(Constantes.TIPO_STATUS_ATIVO);
								imcg.setQuantidade(1);
								informacaoMusicalCadastroGeneroDAO
										.salvarIMCG(imcg);
							} else {
								if (imcg.getStatus() == Constantes.STATUS_GENERO_FOI_CURTIDO) {
									imcg.setQuantidade(imcg.getQuantidade() + 1);
									imcg.setStatus(Constantes.TIPO_STATUS_ATIVO);
									informacaoMusicalCadastroGeneroDAO
											.salvarIMCG(imcg);
								} else if (imcg.getStatus() == Constantes.TIPO_STATUS_ATIVO) {
									imcg.setQuantidade(imcg.getQuantidade() + 1);
									informacaoMusicalCadastroGeneroDAO
											.salvarIMCG(imcg);
								}
							}
						}

						// Fim do if quando vai curtir uma m�sica que n�o
						// existem mas a banda j� existe
					} else {
						// Como a banda n�o existia ent�o cria a banda, cria os
						// g�neros que ainda n�o existem, as informa��es de
						// cadastro e os BandaGeneros
						banda = new Banda();
						banda = m.getBanda();
						bandaDAO.salvarBanda(banda);

						imcb = new InformacaoMusicalCadastroBanda();
						imcb.setBanda(banda);
						imcb.setQuantidade(1);
						imcb.setUsuario(getUsuarioGlobal());
						imcb.setStatus(Constantes.TIPO_STATUS_ATIVO);
						informacaoMusicalCadastroBandaDAO
								.salvarBandasCadastro(imcb);

						// Banda e Musica n�o existem, ent�o elas ter�o de ser
						// salvas e ter� que salvar os generos e criar todas as
						// informa��es de cadastro necess�rias
						MediaUsuarioGenero mUG;
						List<String> listasGenerosBanda = requisitarAPIGeneroBanda(banda
								.getIdBanda());
						for (String nomeGenero : listasGenerosBanda) {
							genero = null;
							genero = generoDAO.pesquisarGenero(nomeGenero);

							if (genero == null) {
								genero = new Genero();
								genero.setNomeGenero(nomeGenero);
								genero = generoDAO
										.salvaListaGeneros(nomeGenero);
							}

							// Cria/Atualiza o MediaUsuarioGenero
							mUG = null;
							mUG = mediaUsuarioGeneroDAO.pesquisarExiste(
									getUsuarioGlobal(), genero);

							if (mUG == null) {
								mUG = new MediaUsuarioGenero();
								mUG.setGenero(genero);
								mUG.setUsuario(getUsuarioGlobal());
								mUG.setQuantidadeMusicas(1D);
								mUG.setMedia(m.getBPMMUsica());
								mUG.setMediaAvaliacoes(Double.valueOf(nota));
							} else {
								mUG.setQuantidadeMusicas(mUG
										.getQuantidadeMusicas() + 1);
								mUG.setMedia(((mUG.getMedia() * (mUG
										.getQuantidadeMusicas() - 1)) + m
										.getBPMMUsica())
										/ mUG.getQuantidadeMusicas());
								mUG.setMediaAvaliacoes(((mUG
										.getMediaAvaliacoes() * (mUG
										.getQuantidadeMusicas() - 1)) + Double
										.valueOf(nota))
										/ mUG.getQuantidadeMusicas());
							}

							mediaUsuarioGeneroDAO.salvaMediaUsuarioGenero(mUG);

							bg = null;
							bg = bandaGeneroDAO.pesquisarBandaGenero(banda,
									genero);

							if (bg == null) {
								bg = new BandaGenero();
								bg.setBanda(banda);
								bg.setGenero(genero);
								bandaGeneroDAO.salvarBandaGenero(bg);
							}

							imcg = null;
							imcg = informacaoMusicalCadastroGeneroDAO
									.pesquisarIMCG(getUsuarioGlobal(), genero);
							if (imcg == null) {
								imcg = new InformacaoMusicalCadastroGenero();
								imcg.setUsuario(getUsuarioGlobal());
								imcg.setGenero(genero);
								imcg.setQuantidade(1);
								imcg.setStatus(Constantes.TIPO_STATUS_ATIVO);
								informacaoMusicalCadastroGeneroDAO
										.salvarIMCG(imcg);
							} else {
								if (imcg.getStatus() == Constantes.STATUS_GENERO_FOI_CURTIDO) {
									imcg.setStatus(Constantes.TIPO_STATUS_ATIVO);
									imcg.setQuantidade(imcg.getQuantidade() + 1);
									informacaoMusicalCadastroGeneroDAO
											.salvarIMCG(imcg);
								}
							}
						}

						// Fim do if que a banda n�o existia
					}

					// Cria a musica setando a banda, as informa��es de cadastro
					// de m�sica e a AvaliarMusica
					m.setBanda(banda);
					m.setAlbum(this.nomeAlbum);
					m.setUrlImagem(this.valorUrlMusica);
					m.setIdDeezer(this.valorIdMusica);
					m.setQuantidadeAvaliacoes(m.getQuantidadeAvaliacoes() + 1);
					m.setMediaAvaliacoes(((m.getMediaAvaliacoes() * m
							.getQuantidadeAvaliacoes()) + Integer.valueOf(nota))
							/ m.getQuantidadeAvaliacoes());
					musicaDAO.salvarMusica(m);

					imcm = new InformacaoMusicalCadastroMusica();
					imcm.setMusica(m);
					imcm.setUsuario(getUsuarioGlobal());
					imcm.setStatus(Constantes.TIPO_STATUS_ATIVO);
					informacaoMusicalCadastroMusicaDAO
							.salvarMusicaCadastro(imcm);

					am = new AvaliarMusica();
					am.setMusica(m);
					am.setLancamento(new Date());
					am.setUsuario(getUsuarioGlobal());
					am.setStatus(Constantes.TIPO_STATUS_ATIVO);
					am.setResposta(true);
					am.setNota(Integer.valueOf(nota));
					avaliarMusicaDAO.salvarAvaliacao(am);

					// Fim do if que a m�sica n�o existia
				}

				// Fim do if que a m�sica ainda n�o tinha sido curtida
			} else {
				// Caso a m�sica tenha sido curtida
				m = musicaDAO.procuraMusicaByID(valorIdMusica);

				// Sup�e-se que a m�sica e a banda j� existam
				if (m != null && m.getPkMusica() > 0) {

					/*
					 * if(banda!=null && banda.getPkBanda()>0) { //atualiza as
					 * informa��es do cadastro de banda e g�nero que j� devem
					 * existir imcb = null; imcb =
					 * informacaoMusicalCadastroBandaDAO
					 * .pesquisarIMCB(getUsuarioGlobal(), banda);
					 * 
					 * if(imcb!=null &&
					 * imcb.getPkInformacaoMusicalCadastroBanda()>0) {
					 * if(imcb.getStatus()==Constantes.TIPO_STATUS_ATIVO) {
					 * if(imcb.getQuantidade()==1) {
					 * imcb.setStatus(Constantes.STATUS_BANDA_FOI_CURTIDA);
					 * imcb.setQuantidade(imcb.getQuantidade()-1); } else
					 * if(imcb.getQuantidade()>1) {
					 * imcb.setQuantidade(imcb.getQuantidade()-1); }
					 * 
					 * informacaoMusicalCadastroBandaDAO.salvarBandasCadastro(imcb
					 * ); } }
					 * 
					 * //Como a banda j� existem, ent�o a bandaGen�ro e os
					 * g�neros j� existem //Ent�o s� precisar� de atualizar as
					 * informa��es do cadastro de g�nero List<String>
					 * listasGenerosBanda =
					 * requisitarAPIGeneroBanda(banda.getIdBanda()); for (String
					 * nomeGenero : listasGenerosBanda) { genero = null; genero
					 * = generoDAO.pesquisarGenero(nomeGenero);
					 * 
					 * if(genero==null) { genero = new Genero();
					 * genero.setNomeGenero(nomeGenero); genero =
					 * generoDAO.salvaListaGeneros(nomeGenero); }
					 * 
					 * imcg = null; imcg =
					 * informacaoMusicalCadastroGeneroDAO.pesquisarIMCG
					 * (getUsuarioGlobal(), genero);
					 * 
					 * if(imcg!=null &&
					 * imcg.getPkInformacaoMusicalCadastroGenero()>0) {
					 * if(imcg.getStatus()==Constantes.TIPO_STATUS_ATIVO) {
					 * if(imcg.getQuantidade()==1) {
					 * imcg.setStatus(Constantes.STATUS_GENERO_FOI_CURTIDO);
					 * imcg.setQuantidade(imcg.getQuantidade()-1); } else
					 * if(imcg.getQuantidade()>1) {
					 * imcg.setQuantidade(imcg.getQuantidade()-1); }
					 * 
					 * informacaoMusicalCadastroGeneroDAO.salvarIMCG(imcg); } }
					 * }
					 * 
					 * //Fim quando procura pela banda que j� existe }
					 */

					/*
					 * imcm = null; imcm =
					 * informacaoMusicalCadastroMusicaDAO.pesquisarIMCM
					 * (getUsuarioGlobal(), m);
					 * 
					 * if(imcm!=null &&
					 * imcm.getPkInformacaoMusicalCadastroMusica()>0) {
					 * if(imcm.getStatus()==Constantes.TIPO_STATUS_ATIVO) {
					 * imcm.setStatus(Constantes.STATUS_MUSICA_FOI_CURTIDA);
					 * informacaoMusicalCadastroMusicaDAO
					 * .salvarMusicaCadastro(imcm); } }
					 */

					am = null;
					am = avaliarMusicaDAO
							.pesquisaUsuarioAvaliouMusicaPelaMusica(m,
									getUsuarioGlobal());

					if (am != null && am.getPkAvaliarMusica() > 0) {
						banda = bandaDAO.pesquisarBandaExiste(m.getBanda()
								.getIdBanda());

						if (banda != null && banda.getPkBanda() > 0) {
							// Como a banda j� existem, ent�o a bandaGen�ro e os
							// g�neros j� existem
							// Ent�o s� precisar� de atualizar as informa��es do
							// cadastro de g�nero
							MediaUsuarioGenero mUG;
							List<String> listasGenerosBanda = requisitarAPIGeneroBanda(banda
									.getIdBanda());
							for (String nomeGenero : listasGenerosBanda) {
								genero = null;
								genero = generoDAO.pesquisarGenero(nomeGenero);

								if (genero == null) {
									genero = new Genero();
									genero.setNomeGenero(nomeGenero);
									genero = generoDAO
											.salvaListaGeneros(nomeGenero);
								}

								// Cria/Atualiza o MediaUsuarioGenero
								mUG = null;
								mUG = mediaUsuarioGeneroDAO.pesquisarExiste(
										getUsuarioGlobal(), genero);

								if (mUG == null) {
									mUG = new MediaUsuarioGenero();
									mUG.setGenero(genero);
									mUG.setUsuario(getUsuarioGlobal());
									mUG.setQuantidadeMusicas(1D);
									mUG.setMedia(m.getBPMMUsica());
									mUG.setMediaAvaliacoes(Double.valueOf(nota));
								} else {
									mUG.setMediaAvaliacoes(((mUG
											.getMediaAvaliacoes() * (mUG
											.getQuantidadeMusicas()))
											- am.getNota() + Double
												.valueOf(nota))
											/ mUG.getQuantidadeMusicas());
								}

								mediaUsuarioGeneroDAO
										.salvaMediaUsuarioGenero(mUG);
							}

						}
						/*
						 * if(am.getResposta()!=false) { am.setResposta(false);
						 * avaliarMusicaDAO.salvarAvaliacao(am); }
						 */

						m.setMediaAvaliacoes(((m.getMediaAvaliacoes() * m
								.getQuantidadeAvaliacoes()) - am.getNota() + Integer
									.valueOf(nota))
								/ m.getQuantidadeAvaliacoes());
						musicaDAO.salvarMusica(m);

						am.setNota(Integer.valueOf(nota));
						avaliarMusicaDAO.salvarAvaliacao(am);
					}

					// Fim quando procura pela m�sica que j� existe
				}

				// Fim do if quando vai descurtir uma m�sica
			}

			ConectaBanco.getInstance().commit();
			this.curtiuMusica = Boolean.TRUE;

			if (nomeAlbum != null && nomeAlbum.length() > 0
					&& valorUrlMusica != null && valorUrlMusica.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ valorIdMusica + "&m=" + nomeMusica
										+ "&a=" + nomeArtista + "&i="
										+ valorIdMusicaEchoAux + "&n="
										+ nomeAlbum + "&u=" + valorUrlMusica);
			} else if (nomeAlbum != null && nomeAlbum.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ valorIdMusica + "&m=" + nomeMusica
										+ "&a=" + nomeArtista + "&i="
										+ valorIdMusicaEchoAux + "&n="
										+ nomeAlbum);
			} else if (valorUrlMusica != null && valorUrlMusica.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ valorIdMusica + "&m=" + nomeMusica
										+ "&a=" + nomeArtista + "&i="
										+ valorIdMusicaEchoAux + "&u="
										+ valorUrlMusica);
			} else {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ valorIdMusica + "&m=" + nomeMusica
										+ "&a=" + nomeArtista + "&i="
										+ valorIdMusicaEchoAux);
			}

		} catch (Exception e) {
			ConectaBanco.getInstance().rollBack();
			e.printStackTrace();
		}
	}

	/*
	 * public void avaliarMusicaNegativamente() { try {
	 * if(getUsuarioGlobal()!=null) { Musica musica = null; musica =
	 * musicaDAO.procuraMusicaByID(valorIdMusica); if(musica==null) { musica =
	 * pesquisaMusica(valorIdMusica); if(musica!=null) {
	 * ConectaBanco.getInstance().beginTransaction(); musicaDAO.save(musica);
	 * ConectaBanco.getInstance().commit(); } }
	 * 
	 * if(musica!=null) { ConectaBanco.getInstance().beginTransaction();
	 * AvaliarMusica am = avaliarMusicaDAO.getAvaliacaoUsuario(musica,
	 * getUsuarioGlobal(), 2); ConectaBanco.getInstance().commit();
	 * avaliarMusicaPrincipal = am; //naoCurtiuMusica = !naoCurtiuMusica;
	 * 
	 * if(am.getResposta()==null) { setNaoCurtiuMusica(false); } else {
	 * if(am.getResposta()==false) { setNaoCurtiuMusica(true); } }
	 * System.out.println("Curtiu " + isCurtiuMusica() + "N�o Curtiu " +
	 * isNaoCurtiuMusica()); } } } catch(Exception e) {
	 * ConectaBanco.getInstance().rollBack(); e.printStackTrace(); } }
	 */

	public void pesquisaCurtiu() {
		try {
			if (getUsuarioGlobal() != null) {
				setCurtiuMusica(false);
				setNaoCurtiuMusica(false);
				Boolean resposta = avaliarMusicaDAO.pesquisaAvaliacaoUsuario(
						valorIdMusica, getUsuarioGlobal());
				if (resposta != null) {
					if (resposta) {
						setCurtiuMusica(true);
						setNaoCurtiuMusica(false);
					} else if (!resposta) {
						setCurtiuMusica(false);
						setNaoCurtiuMusica(true);
					}
				} else {
					setCurtiuMusica(false);
					setNaoCurtiuMusica(false);
				}
			}
		} catch (Exception e) {
			ConectaBanco.getInstance().rollBack();
			e.printStackTrace();
		}
	}

	public void save(Usuario usuario) {
		try {
			ConectaBanco.getInstance().beginTransaction();
			usuarioDAO.save(usuario);
			ConectaBanco.getInstance().commit();
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void adicionarEmPlaylist(Playlist playlist) {
		try {
			Musica m = null;
			m = musicaDAO.procuraMusicaByID(valorIdMusica);

			if (m != null && m.getPkMusica() > 0) {
				ConectaBanco.getInstance().beginTransaction();

				PlaylistMusica pm = new PlaylistMusica();
				pm.setLancamento(new Date());
				pm.setPlaylist(playlist);
				pm.setMusica(m);

				playlistMusicaDAO.save(pm);
				playlist.setNumeroMusicas(playlist.getNumeroMusicas() + 1);
				playlistDAO.save(playlist);

				ConectaBanco.getInstance().commit();
			}

			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
									+ valorIdMusica + "&m=" + nomeMusica
									+ "&a=" + nomeArtista + "&i="
									+ valorIdMusicaEchoAux + "&u="
									+ valorUrlMusica);
		}

		catch (Exception e) {
			ConectaBanco.getInstance().rollBack();
			e.printStackTrace();
		}
	}

	public void atualizarQuantidadeVezesProcurada() {
		try {
			PossivelAvaliacaoMusica pam = null;
			Musica m = null;
			ConectaBanco.getInstance().beginTransaction();
			m = musicaDAO.procuraMusicaByID(valorIdMusica);
			if (m != null && m.getPkMusica() > 0) {
				AvaliarMusica am = null;
				am = avaliarMusicaDAO.pesquisaUsuarioAvaliouMusicaPelaMusica(m,
						getUsuarioGlobal());

				if (am == null) {
					pam = possivelAvaliacaoMusicaDAO.getMusicasPlaylist(
							getUsuarioGlobal(), m);

					if (pam != null && pam.getPkPossivelAvaliacaoMusica() > 0) {
						pam.setQuantidadeOuvida(pam.getQuantidadeOuvida() + 1);
					} else {
						pam = new PossivelAvaliacaoMusica();
						pam.setMusica(m);
						pam.setUsuario(getUsuarioGlobal());
						pam.setQuantidadeOuvida(1);
					}

					possivelAvaliacaoMusicaDAO.save(pam);

					if (pam.getQuantidadeOuvida() >= 5) {
						am = new AvaliarMusica();
						am.setLancamento(new Date());
						am.setMusica(m);
						am.setUsuario(getUsuarioGlobal());
						am.setResposta(true);
						am.setNota(3);

						avaliarMusicaDAO.save(am);
						
						m.setQuantidadeAvaliacoes(m.getQuantidadeAvaliacoes() + 1);
						m.setMediaAvaliacoes(((m.getMediaAvaliacoes() * (m
								.getQuantidadeAvaliacoes() - 1)) + 3)
								/ m.getQuantidadeAvaliacoes());
						musicaDAO.salvarMusica(m);

					}
				}

			} else {
				m = pesquisaMusica(valorIdMusicaEchoAux);

				// Pesquisa se a banda existe
				Banda banda = null;
				banda = bandaDAO
						.pesquisarBandaExiste(m.getBanda().getIdBanda());
				if (banda != null && banda.getPkBanda() > 0) {
					m.setBanda(banda);
					m.setAlbum(this.nomeAlbum);
					m.setUrlImagem(this.valorUrlMusica);
					m.setIdDeezer(this.valorIdMusica);
					m.setQuantidadeAvaliacoes(0);
					m.setMediaAvaliacoes(0D);
					musicaDAO.salvarMusica(m);
				} else {
					banda = new Banda();
					banda = m.getBanda();
					bandaDAO.salvarBanda(banda);
					Genero genero;
					BandaGenero bg;

					List<String> listasGenerosBanda = requisitarAPIGeneroBanda(banda
							.getIdBanda());
					for (String nomeGenero : listasGenerosBanda) {
						genero = null;
						genero = generoDAO.pesquisarGenero(nomeGenero);

						if (genero == null) {
							genero = new Genero();
							genero.setNomeGenero(nomeGenero);
							genero = generoDAO.salvaListaGeneros(nomeGenero);
						}

						bg = null;
						bg = bandaGeneroDAO.pesquisarBandaGenero(banda, genero);

						if (bg == null) {
							bg = new BandaGenero();
							bg.setBanda(banda);
							bg.setGenero(genero);
							bandaGeneroDAO.salvarBandaGenero(bg);
						}
					}

					m.setBanda(banda);
					m.setAlbum(this.nomeAlbum);
					m.setUrlImagem(this.valorUrlMusica);
					m.setIdDeezer(this.valorIdMusica);
					m.setQuantidadeAvaliacoes(0);
					m.setMediaAvaliacoes(0D);
					musicaDAO.salvarMusica(m);
				}
				
				pam = new PossivelAvaliacaoMusica();
				pam.setMusica(m);
				pam.setUsuario(getUsuarioGlobal());
				pam.setQuantidadeOuvida(1);
				possivelAvaliacaoMusicaDAO.save(pam);
			}
			ConectaBanco.getInstance().commit();
		} catch (Exception e) {
			ConectaBanco.getInstance().rollBack();
			e.printStackTrace();
		}
	}

	public void carregarListaMusicasRecomendadas() {
		try {
			Musica m = null;
			Banda bandaAuxAux = null;
			m = musicaDAO.procuraMusicaByID(valorIdMusica);
			List<String> listasGenerosBanda = null;
			if (m == null) {
				m = pesquisaMusica(valorIdMusicaEchoAux);
				bandaAuxAux = bandaDAO.pesquisarBandaExiste(m.getBanda()
						.getIdBanda());
			}

			if ((m != null && m.getPkMusica() > 0)
					|| (bandaAuxAux != null && bandaAuxAux.getPkBanda() > 0)) {
				List<Musica> listaMusicasRecomendacao = null;

				if (m != null && m.getPkMusica() > 0) {
					listaMusicasRecomendacao = musicaDAO.getMusicasByBanda(m
							.getBanda());
				} else {
					listaMusicasRecomendacao = musicaDAO
							.getMusicasByBanda(bandaAuxAux);
				}

				if (listaMusicasRecomendacao.size() >= 7) {
					if (listaMusicasRecomendacao.size() == 7) {
						musicasRecomendadas = new ArrayList<Musica>();
						musicasRecomendadas.addAll(listaMusicasRecomendacao);
					} else {
						if (listaMusicasRecomendacao != null
								&& listaMusicasRecomendacao.size() > 1) {
							listaMusicasRecomendacao = mudarValoresLista(listaMusicasRecomendacao);
						}

						for (Musica musica : listaMusicasRecomendacao) {
							if(musicasRecomendadas==null)
							{
								musicasRecomendadas = new ArrayList<Musica>();
							}
							musicasRecomendadas.add(musica);
							if (musicasRecomendadas.size() >= 7) {
								break;
							}
						}
					}
				} else {
					musicasRecomendadas = new ArrayList<Musica>();
					musicasRecomendadas.addAll(listaMusicasRecomendacao);

					List<Genero> listaBandasGeneroMusica = null;
					listaBandasGeneroMusica = bandaGeneroDAO
							.pesquisarGenerosBanda(m.getBanda());

					if (listaBandasGeneroMusica != null
							&& listaBandasGeneroMusica.size() > 0) {
						List<Banda> listaBandaAux = new ArrayList<Banda>();
						List<Banda> listaBanda = new ArrayList<Banda>();
						for (Genero genero : listaBandasGeneroMusica) {
							listaBandaAux.addAll(bandaGeneroDAO
									.pesquisarBandas(genero));
						}

						boolean flagBandas;

						for (Banda bandaAux : listaBandaAux) {
							flagBandas = false;
							for (Banda banda : listaBanda) {
								if (bandaAux.getPkBanda() == banda.getPkBanda()) {
									flagBandas = true;
									break;
								}
							}

							if (!flagBandas) {
								listaBanda.add(bandaAux);
							}
						}

						if (listaBanda != null && listaBanda.size() > 1) {
							listaBanda = mudarValoresListaBanda(listaBanda);
						}
						List<Musica> listaMusicas = null;
						for (Banda banda : listaBanda) {
							listaMusicas = musicaDAO.getMusicasByBanda(banda);

							if (listaMusicas != null && listaMusicas.size() > 0) {
								if (listaMusicas != null
										&& listaMusicas.size() > 1) {
									listaMusicas = mudarValoresLista(listaMusicas);
								}
								for (Musica musica : listaMusicas) {
									musicasRecomendadas.add(musica);

									if (musicasRecomendadas.size() >= 7) {
										break;
									}
								}

							}

							if (musicasRecomendadas.size() >= 7) {
								break;
							}
						}
					}
				}
			} else {
				List<Musica> listaMusicasRecomendacao = null;
				listaMusicasRecomendacao = musicaDAO
						.pesquisaMelhoresAvaliadas7();
				musicasRecomendadas = new ArrayList<Musica>();
				musicasRecomendadas.addAll(listaMusicasRecomendacao);
			}

			if (musicasRecomendadas == null || musicasRecomendadas.size() == 0) {
				musicasRecomendadas = null;
			}
			else if(musicasRecomendadas!=null && musicasRecomendadas.size()>0)
			{
				List<Musica> musicasAuxiliar = new ArrayList<Musica>();
				boolean flag = false;
				for (int i = (musicasRecomendadas.size()-1); i >= 0; i--)
				{
					flag = false;
					if((musicasRecomendadas.get(i).getIdDeezer().equals(valorIdMusica)))
					{
						musicasRecomendadas.remove(i);
					}
					else
					{
						if(musicasAuxiliar.size()==0)
						{
							musicasAuxiliar.add(musicasRecomendadas.get(i));
						}
						else
						{
							for (Musica musicaRecomendada : musicasAuxiliar)
							{
								if(!(musicaRecomendada.getIdDeezer().equals(valorIdMusica)))
								{
									if(musicaRecomendada.getIdDeezer().equals(musicasRecomendadas.get(i).getIdDeezer()))
									{
										flag = true;
									}
								}
							}
							
							if(!flag)
							{
								musicasAuxiliar.add(musicasRecomendadas.get(i));
							}
						}
						
					}
				}
				
				musicasRecomendadas = new ArrayList<Musica>();
				musicasRecomendadas.addAll(musicasAuxiliar);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void redirecionaPaginaMusica(String idMusica, String nomeMusica,
			String artistaBandaMusica, String album, String idEcho, String url) {
		try {
			if (album != null && album.length() > 0 && url != null
					&& url.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ idMusica + "&m=" + nomeMusica + "&a="
										+ artistaBandaMusica + "&i=" + idEcho
										+ "&n=" + album + "&u=" + url);
			} else if (album != null && album.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ idMusica + "&m=" + nomeMusica + "&a="
										+ artistaBandaMusica + "&i=" + idEcho
										+ "&n=" + album);
			} else if (url != null && url.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ idMusica + "&m=" + nomeMusica + "&a="
										+ artistaBandaMusica + "&i=" + idEcho
										+ "&u=" + url);
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

	public List<Banda> mudarValoresListaBanda(List<Banda> listaBanda) {
		Random gerador = new Random();
		List<Integer> listaNumerosGenrados = new ArrayList<Integer>();
		List<Banda> listaGerada = new ArrayList<Banda>();
		Integer numeroGerado = 0;
		int count;
		for (int i = 0; i < listaBanda.size(); i++) {
			count = 0;
			numeroGerado = gerador.nextInt(listaBanda.size() - 1);

			if (listaNumerosGenrados != null && listaNumerosGenrados.size() > 0) {
				if (listaNumerosGenrados.contains(numeroGerado)) {
					while (listaNumerosGenrados.contains(numeroGerado)
							&& count != 10) {
						count++;
						numeroGerado = gerador.nextInt(listaBanda.size() - 1);
					}

					if (count >= 10
							&& listaNumerosGenrados.contains(numeroGerado)) {
						for (int x = 0; x < listaBanda.size(); x++) {
							numeroGerado = x;
							if (!listaNumerosGenrados.contains(numeroGerado)) {
								break;
							}
						}
					}
				}

				listaGerada.add(listaBanda.get(numeroGerado));
				listaNumerosGenrados.add(numeroGerado);
			} else {
				listaGerada.add(listaBanda.get(numeroGerado));
				listaNumerosGenrados.add(numeroGerado);
			}
		}

		return listaGerada;
	}

	public List<Musica> mudarValoresLista(List<Musica> listaMusicas) {
		Random gerador = new Random();
		List<Integer> listaNumerosGenrados = new ArrayList<Integer>();
		List<Musica> listaGerada = new ArrayList<Musica>();
		Integer numeroGerado = 0;
		int count;
		for (int i = 0; i < listaMusicas.size(); i++) {
			count = 0;
			numeroGerado = gerador.nextInt(listaMusicas.size() - 1);

			if (listaNumerosGenrados != null && listaNumerosGenrados.size() > 0) {
				if (listaNumerosGenrados.contains(numeroGerado)) {
					while (listaNumerosGenrados.contains(numeroGerado)
							&& count != 10) {
						count++;
						numeroGerado = gerador.nextInt(listaMusicas.size() - 1);
					}

					if (count >= 10
							&& listaNumerosGenrados.contains(numeroGerado)) {
						for (int x = 0; x < listaMusicas.size(); x++) {
							numeroGerado = x;
							if (!listaNumerosGenrados.contains(numeroGerado)) {
								break;
							}
						}
					}
				}

				listaGerada.add(listaMusicas.get(numeroGerado));
				listaNumerosGenrados.add(numeroGerado);
			} else {
				listaGerada.add(listaMusicas.get(numeroGerado));
				listaNumerosGenrados.add(numeroGerado);
			}
		}

		return listaGerada;
	}

	public List<Usuario> findAll() {
		return usuarioDAO.findAll();
	}

	// Getters and Setters //

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getValorIdMusica() {
		return valorIdMusica;
	}

	public void setValorIdMusica(String valorIdMusica) {
		this.valorIdMusica = valorIdMusica;
	}

	public String getValorIdMusicaEcho() {
		return valorIdMusicaEcho;
	}

	public void setValorIdMusicaEcho(String valorIdMusicaEcho) {
		this.valorIdMusicaEcho = valorIdMusicaEcho;
	}

	public boolean isCurtiuMusica() {
		return curtiuMusica;
	}

	public void setCurtiuMusica(boolean curtiuMusica) {
		this.curtiuMusica = curtiuMusica;
	}

	public AvaliarMusica getAvaliarMusicaPrincipal() {
		return avaliarMusicaPrincipal;
	}

	public String getNomeCompletoMusica() {
		return nomeCompletoMusica;
	}

	public void setNomeCompletoMusica(String nomeCompletoMusica) {
		this.nomeCompletoMusica = nomeCompletoMusica;
	}

	public String getNomeMusica() {
		return nomeMusica;
	}

	public void setNomeMusica(String nomeMusica) {
		this.nomeMusica = nomeMusica;
	}

	public String getNomeAlbum() {
		return nomeAlbum;
	}

	public void setNomeAlbum(String nomeAlbum) {
		this.nomeAlbum = nomeAlbum;
	}

	public String getNomeArtista() {
		return nomeArtista;
	}

	public void setNomeArtista(String nomeArtista) {
		this.nomeArtista = nomeArtista;
	}

	public String getTokenRecebido() {
		return tokenRecebido;
	}

	public void setTokenRecebido(String tokenRecebido) {
		this.tokenRecebido = tokenRecebido;
	}

	public void setAvaliarMusicaPrincipal(AvaliarMusica avaliarMusicaPrincipal) {
		this.avaliarMusicaPrincipal = avaliarMusicaPrincipal;
	}

	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	public MusicaDAO getMusicaDAO() {
		return musicaDAO;
	}

	public void setMusicaDAO(MusicaDAO musicaDAO) {
		this.musicaDAO = musicaDAO;
	}

	public BandaDAO getBandaDAO() {
		return bandaDAO;
	}

	public void setBandaDAO(BandaDAO bandaDAO) {
		this.bandaDAO = bandaDAO;
	}

	public GeneroDAO getGeneroDAO() {
		return generoDAO;
	}

	public void setGeneroDAO(GeneroDAO generoDAO) {
		this.generoDAO = generoDAO;
	}

	public InformacaoMusicalCadastroBandaDAO getInformacaoMusicalCadastroBandaDAO() {
		return informacaoMusicalCadastroBandaDAO;
	}

	public void setInformacaoMusicalCadastroBandaDAO(
			InformacaoMusicalCadastroBandaDAO informacaoMusicalCadastroBandaDAO) {
		this.informacaoMusicalCadastroBandaDAO = informacaoMusicalCadastroBandaDAO;
	}

	public InformacaoMusicalCadastroGeneroDAO getInformacaoMusicalCadastroGeneroDAO() {
		return informacaoMusicalCadastroGeneroDAO;
	}

	public BandaGeneroDAO getBandaGeneroDAO() {
		return bandaGeneroDAO;
	}

	public void setBandaGeneroDAO(BandaGeneroDAO bandaGeneroDAO) {
		this.bandaGeneroDAO = bandaGeneroDAO;
	}

	public void setInformacaoMusicalCadastroGeneroDAO(
			InformacaoMusicalCadastroGeneroDAO informacaoMusicalCadastroGeneroDAO) {
		this.informacaoMusicalCadastroGeneroDAO = informacaoMusicalCadastroGeneroDAO;
	}

	public AvaliarMusicaDAO getAvaliarMusicaDAO() {
		return avaliarMusicaDAO;
	}

	public void setAvaliarMusicaDAO(AvaliarMusicaDAO avaliarMusicaDAO) {
		this.avaliarMusicaDAO = avaliarMusicaDAO;
	}

	public InformacaoMusicalCadastroMusicaDAO getInformacaoMusicalCadastroMusicaDAO() {
		return informacaoMusicalCadastroMusicaDAO;
	}

	public void setInformacaoMusicalCadastroMusicaDAO(
			InformacaoMusicalCadastroMusicaDAO informacaoMusicalCadastroMusicaDAO) {
		this.informacaoMusicalCadastroMusicaDAO = informacaoMusicalCadastroMusicaDAO;
	}

	public int getNotaMusica() {
		return notaMusica;
	}

	public void setNotaMusica(int notaMusica) {
		this.notaMusica = notaMusica;
	}

	public String getValorUrlMusica() {
		return valorUrlMusica;
	}

	public void setValorUrlMusica(String valorUrlMusica) {
		this.valorUrlMusica = valorUrlMusica;
	}

	public List<Playlist> getListaPlaylists() {
		return listaPlaylists;
	}

	public void setListaPlaylists(List<Playlist> listaPlaylists) {
		this.listaPlaylists = listaPlaylists;
	}

	public String getValorIdMusicaEchoAux() {
		return valorIdMusicaEchoAux;
	}

	public void setValorIdMusicaEchoAux(String valorIdMusicaEchoAux) {
		this.valorIdMusicaEchoAux = valorIdMusicaEchoAux;
	}

	public List<Musica> getMusicasRecomendadas() {
		return musicasRecomendadas;
	}

	public void setMusicasRecomendadas(List<Musica> musicasRecomendadas) {
		this.musicasRecomendadas = musicasRecomendadas;
	}
}