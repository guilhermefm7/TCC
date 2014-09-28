package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.BandaDAO;
import br.com.recomusic.dao.BandaGeneroDAO;
import br.com.recomusic.dao.GeneroDAO;
import br.com.recomusic.dao.InformacaoMusicalCadastroBandaDAO;
import br.com.recomusic.dao.InformacaoMusicalCadastroGeneroDAO;
import br.com.recomusic.dao.InformacaoMusicalCadastroMusicaDAO;
import br.com.recomusic.dao.MediaUsuarioGeneroDAO;
import br.com.recomusic.dao.MusicaDAO;
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
	private BandaDAO bandaDAO = new BandaDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private GeneroDAO generoDAO = new GeneroDAO(ConectaBanco.getInstance()
			.getEntityManager());
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

	public MusicaBean() {	}

	/**
	 * Responsavel por iniciar a pagina desktop.xhtml, carregando tdo oq ela
	 * precisa, verificando sessao, etc.
	 */
	public void iniciar() {
		try {
			if ((valorIdMusica != null && valorIdMusica.length() > 0)
					&& (nomeMusica != null && nomeMusica.length() > 0)
					&& (valorIdMusicaEcho != null && valorIdMusicaEcho.length() > 0)) {

				this.valorIdMusicaEchoAux = valorIdMusicaEcho;
				this.valorIdMusicaEcho = null;
				this.notaMusica = 0;
				if (UtilidadesTelas.verificarSessao()) {
					setUsuario(getUsuarioGlobal());

					if (nomeArtista != null && nomeArtista.length() > 0) {
						nomeCompletoMusica = nomeMusica + " - " + nomeArtista;
					} else {
						nomeCompletoMusica = nomeMusica;
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
	 * System.out.println("Curtiu " + isCurtiuMusica() + "Não Curtiu " +
	 * isNaoCurtiuMusica()); } } } catch(Exception e) {
	 * ConectaBanco.getInstance().rollBack(); e.printStackTrace(); } }
	 */

	/**
	 * Avalia uma música (Curti ou Descurti uma música)
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

			// Caso a música ainda não tenha sido curtida
			if (!this.curtiuMusica) {
				m = musicaDAO.procuraMusicaByID(valorIdMusica);

				if (m != null && m.getPkMusica() > 0) {
					// Caso a música já exista
					// Pesquisa se a banda existe
					banda = bandaDAO.pesquisarBandaExiste(m.getBanda()
							.getIdBanda());

					if (banda != null && banda.getPkBanda() > 0) {
						// Caso exista, atualiza as informações do cadastro de
						// banda caso existam ou cria nova caso seja necessário
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

						// Como a banda já existem, então a bandaGenêro e os
						// gêneros já existem
						// Então só precisará de atualizar as informações do
						// cadastro de gênero
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

						// Fim do if quando vai curtir uma música que não
						// existem mas a banda já existe
					} else {
						// Como a banda não existia então cria a banda, cria os
						// gêneros que ainda não existem, as informações de
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

						// Banda e Musica não existem, então elas terão de ser
						// salvas e terá que salvar os generos e criar todas as
						// informações de cadastro necessária
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

						// Fim do if que a banda não existia
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

					// Fim do if quando vai curtir uma música e essa música já
					// exista no BD
				} else {
					// Caso a música não exista
					m = pesquisaMusica(valorIdMusicaEchoAux);

					// Pesquisa se a banda existe
					banda = bandaDAO.pesquisarBandaExiste(m.getBanda()
							.getIdBanda());

					if (banda != null && banda.getPkBanda() > 0) {
						// Caso exista, atualiza as informações do cadastro de
						// banda caso existam ou cria nova caso seja necessário
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

						// Como a banda já existem, então a bandaGenêro e os
						// gêneros já existem
						// Então só precisará de atualizar as informações do
						// cadastro de gênero
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

						// Fim do if quando vai curtir uma música que não
						// existem mas a banda já existe
					} else {
						// Como a banda não existia então cria a banda, cria os
						// gêneros que ainda não existem, as informações de
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

						// Banda e Musica não existem, então elas terão de ser
						// salvas e terá que salvar os generos e criar todas as
						// informações de cadastro necessárias
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

						// Fim do if que a banda não existia
					}

					// Cria a musica setando a banda, as informações de cadastro
					// de música e a AvaliarMusica
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

					// Fim do if que a música não existia
				}

				// Fim do if que a música ainda não tinha sido curtida
			} else {
				// Caso a música tenha sido curtida
				m = musicaDAO.procuraMusicaByID(valorIdMusica);

				// Supõe-se que a música e a banda já existam
				if (m != null && m.getPkMusica() > 0) {

					/*
					 * if(banda!=null && banda.getPkBanda()>0) { //atualiza as
					 * informações do cadastro de banda e gênero que já devem
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
					 * //Como a banda já existem, então a bandaGenêro e os
					 * gêneros já existem //Então só precisará de atualizar as
					 * informações do cadastro de gênero List<String>
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
					 * //Fim quando procura pela banda que já existe }
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
							// Como a banda já existem, então a bandaGenêro e os
							// gêneros já existem
							// Então só precisará de atualizar as informações do
							// cadastro de gênero
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

					// Fim quando procura pela música que já existe
				}

				// Fim do if quando vai descurtir uma música
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
	 * System.out.println("Curtiu " + isCurtiuMusica() + "Não Curtiu " +
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
}