package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.BandaGeneroDAO;
import br.com.recomusic.dao.MediaUsuarioGeneroDAO;
import br.com.recomusic.dao.MusicaDAO;
import br.com.recomusic.im.MusicaIM;
import br.com.recomusic.im.MusicasRecomendadasIM;
import br.com.recomusic.im.RetornoKMeans;
import br.com.recomusic.om.AvaliarMusica;
import br.com.recomusic.om.Genero;
import br.com.recomusic.om.MediaUsuarioGenero;
import br.com.recomusic.om.Musica;
import br.com.recomusic.persistencia.utils.Constantes;
import br.com.recomusic.persistencia.utils.KMeans;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;
import br.com.recomusic.singleton.GuardaMusicasRecomendadas;
import br.com.recomusic.singleton.SemaforoMusicasRecomendadas;

@ManagedBean(name = "RecomendacaoBean")
@RequestScoped
public class RecomendacaoBean extends UtilidadesTelas implements Serializable {
	private static final long serialVersionUID = 1L;
	private MediaUsuarioGeneroDAO mediaUsuarioGeneroDAO = new MediaUsuarioGeneroDAO(
			ConectaBanco.getInstance().getEntityManager());
	private AvaliarMusicaDAO avaliarMusicaDAO = new AvaliarMusicaDAO(
			ConectaBanco.getInstance().getEntityManager());
	private MusicaDAO musicaDAO = new MusicaDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private BandaGeneroDAO bandaGeneroDAO = new BandaGeneroDAO(ConectaBanco
			.getInstance().getEntityManager());
	private String mensagemIncentivamentoCurtidas;
	private MusicasRecomendadasIM listaIM1 = null;
	private MusicasRecomendadasIM listaIM2 = null;
	private MusicasRecomendadasIM listaIM3 = null;
	private MusicasRecomendadasIM maisAvaliadas = null;

	public RecomendacaoBean() {
		iniciar();
	}

	public void iniciar() {
		try {
			SemaforoMusicasRecomendadas.getSemaforo().acquire();

			try {
				listaIM1 = null;
				listaIM2 = null;
				listaIM3 = null;
				maisAvaliadas = null;
				if (UtilidadesTelas.verificarSessao()) {
					List<MediaUsuarioGenero> listaMUG = null;
					listaMUG = mediaUsuarioGeneroDAO
							.pesquisaGenerosUsuario(getUsuarioGlobal());
					List<Musica> listaIMAux;
					List<Long> listaDeUsuariosAnteriores;
					MusicaIM mIM;

					List<Musica> listaMusicasUsuarioGenero;
					List<Musica> listaTodasMusicasUsuario;
					RetornoKMeans retornoKMeans;
					int posicaoKMeans;
					List<MediaUsuarioGenero> listaUsuariosKMeans;
					List<AvaliarMusica> listaAMMusicasUsuarios;
					boolean existeHash;
					boolean existeMusica;
					boolean existeMusicaUsuario;
					boolean existeListaAnterior;
					boolean existeMusicaListaMaisAvaliadas;

					if (listaMUG != null && listaMUG.size() > 0) {
						if (listaMUG.size() > 3) {
							// Como possui o número de gêneros para serem
							// recomendados então não precisará de mandar
							// mensagem incentivamento o usuário
							// a curtir mais músicas

							// * GERAR RÂNDOMICAMENTE PARA PEGAR GÊNEROS
							// DIFERENTES, o terceiro gênero fazer um número
							// rândomico?
							/*
							 * Random random = new Random(); int x =
							 * ran.nextInt(101);
							 */
							
							listaMUG = mudarValoresLista(listaMUG);

							List<MediaUsuarioGenero> listaAllByGenero;
							for (int i = 0; i < listaMUG.size(); i++) {
								listaMusicasUsuarioGenero = new ArrayList<Musica>();
								listaIMAux = new ArrayList<Musica>();

								// Pesquisa todas as músicas do usuário para
								// não
								// recomendar músicas que ele já tenha
								// curtido.
								listaMusicasUsuarioGenero = avaliarMusicaDAO
										.pesquisaAlMusicasUsuarioGenero(
												getUsuarioGlobal(), listaMUG
														.get(i).getGenero());

								// Dado 3 gênero que estão entre os que
								// possuem
								// maior número de músicas curtidas,
								// pesquisa
								// outros usuários que também curtiram este
								// gênero
								listaAllByGenero = new ArrayList<MediaUsuarioGenero>();
								listaAllByGenero = mediaUsuarioGeneroDAO
										.pesquisaAllByGenero(listaMUG.get(i)
												.getGenero(),
												getUsuarioGlobal());
								/*
								 * Roda o KNN/K-Means, procurando os usuários
								 * mais próximos do gênero corrente
								 */
								if (listaAllByGenero.size() > 2) {
									listaAllByGenero.add(listaMUG.get(i));
									retornoKMeans = new RetornoKMeans();
									retornoKMeans = KMeans
											.rodaAlgoritmo(listaAllByGenero);
									posicaoKMeans = 0;

									for (int x = 0; x < KMeans.Blocos; x++) {
										for (int y = 0; y < retornoKMeans
												.getTamanho(); y++) {
											if (retornoKMeans
													.getDivisaoBlocos()[x][y] == listaMUG
													.get(i).getMedia()) {
												posicaoKMeans = x;
												break;
											}
										}
									}

									listaUsuariosKMeans = new ArrayList<MediaUsuarioGenero>();
									listaAllByGenero.remove(listaAllByGenero
											.size() - 1);

									for (int x = 0; x < retornoKMeans
											.getTamanho(); x++) {
										for (MediaUsuarioGenero mediaUsuarioGenero : listaAllByGenero) {
											if (retornoKMeans
													.getDivisaoBlocos()[posicaoKMeans][x] == mediaUsuarioGenero
													.getMedia()) {
												listaUsuariosKMeans
														.add(mediaUsuarioGenero);
												retornoKMeans
														.getDivisaoBlocos()[posicaoKMeans][x] = -9999;
											}
										}
									}

									existeHash = false;

									if (listaUsuariosKMeans != null
											&& listaUsuariosKMeans.size() > 0) {
										// Limpa a lista de usuários
										// anteriores
										// pois, o gênero foi trocado.
										listaDeUsuariosAnteriores = new ArrayList<Long>();
										for (int j = 0; j < listaUsuariosKMeans
												.size(); j++) {
											// Lista que contém as músicas
											// que
											// serão recomendadas do usuário
											// em
											// questão.
											listaAMMusicasUsuarios = new ArrayList<AvaliarMusica>();
											listaAMMusicasUsuarios = avaliarMusicaDAO
													.pesquisaAvaliacaoUsuarioMaior3(
															listaUsuariosKMeans
																	.get(j)
																	.getUsuario(),
															listaUsuariosKMeans
																	.get(j)
																	.getGenero());

											// Percorre a lista de Músicas
											// encontrada do usuário em
											// questão.
											if (listaAMMusicasUsuarios != null
													&& listaAMMusicasUsuarios
															.size() > 0) {
												for (AvaliarMusica avaliarMusica : listaAMMusicasUsuarios) {
													// Verifica se ela já
													// existia na lista de
													// usuários anteriores a
													// esse (MELHORIA DE
													// PROCESSAMENTO)
													if (listaDeUsuariosAnteriores
															.contains(avaliarMusica
																	.getMusica()
																	.getPkMusica())) {
														continue;
													} else {
														listaDeUsuariosAnteriores
																.add(avaliarMusica
																		.getMusica()
																		.getPkMusica());
													}

													// Caso o HashMap esteja
													// vazio, insere o
													// gênero
													// nele, adiciona a
													// música
													// na lista de músicas
													// daquele gênero e
													// adiciona
													// a música na lista
													// auxiliar.
													if (GuardaMusicasRecomendadas
															.getTokensExisteMusica()
															.isEmpty()) {
														// Verifica se a
														// lista
														// de músicas que o
														// usuário curtiu
														// esta
														// cheia, para fazer
														// as
														// verificações e
														// evitar
														// recomendar
														// músicas
														// que o usuário já
														// curtiu
														if (listaMusicasUsuarioGenero != null
																&& listaMusicasUsuarioGenero
																		.size() > 0) {
															existeMusicaUsuario = false;
															for (Musica musica : listaMusicasUsuarioGenero) {
																if (musica
																		.getIdMusica() == avaliarMusica
																		.getMusica()
																		.getIdMusica()) {
																	existeMusicaUsuario = true;
																	break;
																}
															}

															if (!existeMusicaUsuario) {
																// Insere a
																// música na
																// lista.
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.put(listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero(),
																				new ArrayList<Long>());
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.get(listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero())
																		.add(avaliarMusica
																				.getMusica()
																				.getPkMusica());

																existeListaAnterior = false;
																if (listaIM1 != null
																		&& listaIM1
																				.getListaMusica() != null
																		&& listaIM1
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM1
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior
																		&& listaIM2 != null
																		&& listaIM2
																				.getListaMusica() != null
																		&& listaIM2
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM2
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior) {
																	listaIMAux
																			.add(avaliarMusica
																					.getMusica());
																}
															}
														} else {
															// Insere a
															// música
															// na lista.
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.put(listaUsuariosKMeans
																			.get(j)
																			.getGenero()
																			.getPkGenero(),
																			new ArrayList<Long>());
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.get(listaUsuariosKMeans
																			.get(j)
																			.getGenero()
																			.getPkGenero())
																	.add(avaliarMusica
																			.getMusica()
																			.getPkMusica());

															existeListaAnterior = false;
															if (listaIM1 != null
																	&& listaIM1
																			.getListaMusica() != null
																	&& listaIM1
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM1
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior
																	&& listaIM2 != null
																	&& listaIM2
																			.getListaMusica() != null
																	&& listaIM2
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM2
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior) {
																listaIMAux
																		.add(avaliarMusica
																				.getMusica());
															}
														}

														// Caso a lista
														// esteja
														// cheia, sai do
														// for.
														if (listaIMAux.size() >= 6) {
															break;
														}
													} else {
														if (GuardaMusicasRecomendadas
																.getTokensExisteMusica()
																.containsKey(
																		listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero())) {
															// Caso o Hash
															// principal
															// possua
															// o gênero em
															// questão,
															// então
															// procura nas
															// suas
															// listas de
															// músicas, se
															// existe a
															// música
															// corrente, se
															// existir seta
															// existeHash
															// como
															// true (Flag
															// para
															// indicar que
															// possuem
															// músicas
															// no hash que
															// já
															// foram
															// recomendas
															// e caso seja
															// necessário,
															// verificar se
															// ele
															// precisa ser
															// limpado).
															// Caso não
															// exista,
															// seta ela na
															// lista
															// auxliar.
															existeMusica = false;
															for (Long l : GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.get(listaUsuariosKMeans
																			.get(j)
																			.getGenero()
																			.getPkGenero())) {
																if (l == avaliarMusica
																		.getMusica()
																		.getPkMusica()) {
																	existeHash = true;
																	existeMusica = true;
																	break;
																}
															}

															// Caso a música
															// não
															// exista no
															// Hash
															// principal
															if (!existeMusica) {
																// Verifica
																// se a
																// lista de
																// músicas
																// que o
																// usuário
																// curtiu
																// esta
																// cheia,
																// para
																// fazer as
																// verificações
																// e evitar
																// recomendar
																// músicas
																// que o
																// usuário
																// já
																// curtiu
																if (listaMusicasUsuarioGenero != null
																		&& listaMusicasUsuarioGenero
																				.size() > 0) {
																	existeMusicaUsuario = false;
																	for (Musica musica : listaMusicasUsuarioGenero) {
																		if (musica
																				.getIdMusica() == avaliarMusica
																				.getMusica()
																				.getIdMusica()) {
																			existeMusicaUsuario = true;
																			break;
																		}
																	}

																	if (!existeMusicaUsuario) {
																		// Insere
																		// a
																		// música
																		// na
																		// lista.
																		GuardaMusicasRecomendadas
																				.getTokensExisteMusica()
																				.get(listaUsuariosKMeans
																						.get(j)
																						.getGenero()
																						.getPkGenero())
																				.add(avaliarMusica
																						.getMusica()
																						.getPkMusica());

																		existeListaAnterior = false;
																		if (listaIM1 != null
																				&& listaIM1
																						.getListaMusica() != null
																				&& listaIM1
																						.getListaMusica()
																						.size() > 0) {
																			for (Musica musica : listaIM1
																					.getListaMusica()) {
																				if (avaliarMusica
																						.getMusica()
																						.getPkMusica() == musica
																						.getPkMusica()) {
																					existeListaAnterior = true;
																					break;
																				}
																			}
																		}

																		if (!existeListaAnterior
																				&& listaIM2 != null
																				&& listaIM2
																						.getListaMusica() != null
																				&& listaIM2
																						.getListaMusica()
																						.size() > 0) {
																			for (Musica musica : listaIM2
																					.getListaMusica()) {
																				if (avaliarMusica
																						.getMusica()
																						.getPkMusica() == musica
																						.getPkMusica()) {
																					existeListaAnterior = true;
																					break;
																				}
																			}
																		}

																		if (!existeListaAnterior) {
																			listaIMAux
																					.add(avaliarMusica
																							.getMusica());
																		}
																	}
																} else {
																	// Insere
																	// a
																	// música
																	// na
																	// lista.
																	GuardaMusicasRecomendadas
																			.getTokensExisteMusica()
																			.get(listaUsuariosKMeans
																					.get(j)
																					.getGenero()
																					.getPkGenero())
																			.add(avaliarMusica
																					.getMusica()
																					.getPkMusica());

																	existeListaAnterior = false;
																	if (listaIM1 != null
																			&& listaIM1
																					.getListaMusica() != null
																			&& listaIM1
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM1
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior
																			&& listaIM2 != null
																			&& listaIM2
																					.getListaMusica() != null
																			&& listaIM2
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM2
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior) {
																		listaIMAux
																				.add(avaliarMusica
																						.getMusica());
																	}
																}
															}

															// Caso a lista
															// esteja cheia,
															// sai
															// do for.
															if (listaIMAux
																	.size() >= 6) {
																break;
															}
														} else {
															// Verifica se a
															// lista de
															// músicas
															// que o usuário
															// curtiu esta
															// cheia, para
															// fazer
															// as
															// verificações
															// e
															// evitar
															// recomendar
															// músicas que o
															// usuário já
															// curtiu
															if (listaMusicasUsuarioGenero != null
																	&& listaMusicasUsuarioGenero
																			.size() > 0) {
																existeMusicaUsuario = false;
																for (Musica musica : listaMusicasUsuarioGenero) {
																	if (musica
																			.getIdMusica() == avaliarMusica
																			.getMusica()
																			.getIdMusica()) {
																		existeMusicaUsuario = true;
																		break;
																	}
																}

																if (!existeMusicaUsuario) {
																	// Caso
																	// não
																	// exista
																	// o
																	// gênero
																	// no
																	// hash
																	// principal,
																	// insere
																	// o
																	// gênero
																	// nele,
																	// adiciona
																	// a
																	// música
																	// na
																	// lista
																	// de
																	// músicas
																	// daquele
																	// gênero
																	// e
																	// adiciona
																	// a
																	// música
																	// na
																	// lista
																	// auxiliar.
																	GuardaMusicasRecomendadas
																			.getTokensExisteMusica()
																			.put(listaUsuariosKMeans
																					.get(j)
																					.getGenero()
																					.getPkGenero(),
																					new ArrayList<Long>());
																	GuardaMusicasRecomendadas
																			.getTokensExisteMusica()
																			.get(listaUsuariosKMeans
																					.get(j)
																					.getGenero()
																					.getPkGenero())
																			.add(avaliarMusica
																					.getMusica()
																					.getPkMusica());

																	existeListaAnterior = false;
																	if (listaIM1 != null
																			&& listaIM1
																					.getListaMusica() != null
																			&& listaIM1
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM1
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior
																			&& listaIM2 != null
																			&& listaIM2
																					.getListaMusica() != null
																			&& listaIM2
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM2
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior) {
																		listaIMAux
																				.add(avaliarMusica
																						.getMusica());
																	}
																}
															} else {
																// Caso não
																// exista o
																// gênero no
																// hash
																// principal,
																// insere o
																// gênero
																// nele,
																// adiciona
																// a
																// música na
																// lista de
																// músicas
																// daquele
																// gênero e
																// adiciona
																// a
																// música na
																// lista
																// auxiliar.
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.put(listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero(),
																				new ArrayList<Long>());
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.get(listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero())
																		.add(avaliarMusica
																				.getMusica()
																				.getPkMusica());

																existeListaAnterior = false;
																if (listaIM1 != null
																		&& listaIM1
																				.getListaMusica() != null
																		&& listaIM1
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM1
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior
																		&& listaIM2 != null
																		&& listaIM2
																				.getListaMusica() != null
																		&& listaIM2
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM2
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior) {
																	listaIMAux
																			.add(avaliarMusica
																					.getMusica());
																}
															}

															// Caso a lista
															// esteja cheia,
															// sai
															// do for.
															if (listaIMAux
																	.size() >= 6) {
																break;
															}
														}
													}
												}
											}

											// Caso a lista esteja cheia,
											// sai do
											// for.
											if (listaIMAux.size() >= 6) {
												break;
											}
										}
									}

									// Caso o hash de músicas recomendadas
									// esteja cheio, e por isso não tenha
									// completado a lista de músicas, então
									// limpa ele e volta para o mesmo gênero
									if (existeHash && listaIMAux.size() < 6) {
										GuardaMusicasRecomendadas
												.getTokensExisteMusica()
												.get(listaMUG.get(i)
														.getGenero()
														.getPkGenero()).clear();
										i--;
									} else {
										// Caso a lista auxiliar contenha
										// músicas, adiciona ela em algum
										// hash
										// de músicas para recomendação
										if (listaIMAux.size() > 0) {
											if (listaIM1 == null) {
												listaIM1 = new MusicasRecomendadasIM();
												listaIM1.setGenero(listaMUG
														.get(i).getGenero());
												listaIM1.setListaMusica(new ArrayList<Musica>());
												listaIM1.getListaMusica()
														.addAll(listaIMAux);
												listaIM1.setNomeGenero(listaIM1
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM1.setNota(0);

												if (listaIM1.getListaMusica()
														.size() == 0) {
													listaIM1.setTamanhoLista(1);
												}

												if (listaIM1.getListaMusica()
														.size() > 2) {
													listaIM1.setTamanhoLista(3);
												} else {
													listaIM1.setTamanhoLista(listaIM1
															.getListaMusica()
															.size());
												}
											} else if (listaIM2 == null) {
												listaIM2 = new MusicasRecomendadasIM();
												listaIM2.setGenero(listaMUG
														.get(i).getGenero());
												listaIM2.setListaMusica(new ArrayList<Musica>());
												listaIM2.getListaMusica()
														.addAll(listaIMAux);
												listaIM2.setNomeGenero(listaIM2
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM2.setNota(0);

												if (listaIM2.getListaMusica()
														.size() == 0) {
													listaIM2.setTamanhoLista(1);
												}

												if (listaIM2.getListaMusica()
														.size() > 2) {
													listaIM2.setTamanhoLista(3);
												} else {
													listaIM2.setTamanhoLista(listaIM2
															.getListaMusica()
															.size());
												}
											} else {
												listaIM3 = new MusicasRecomendadasIM();
												listaIM3.setGenero(listaMUG
														.get(i).getGenero());
												listaIM3.setListaMusica(new ArrayList<Musica>());
												listaIM3.getListaMusica()
														.addAll(listaIMAux);
												listaIM3.setNomeGenero(listaIM3
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM3.setNota(0);

												if (listaIM3.getListaMusica()
														.size() == 0) {
													listaIM3.setTamanhoLista(1);
												}

												if (listaIM3.getListaMusica()
														.size() > 2) {
													listaIM3.setTamanhoLista(3);
												} else {
													listaIM3.setTamanhoLista(listaIM3
															.getListaMusica()
															.size());
												}

												// Caso todas os hash's
												// tenham
												// sido preenchidos, então
												// sai
												// do loop
												break;
											}
										}
									}
								} else if (listaAllByGenero.size() == 1
										|| listaAllByGenero.size() == 2) {
									existeHash = false;
									listaDeUsuariosAnteriores = new ArrayList<Long>();
									for (MediaUsuarioGenero mediaUsuarioGenero : listaAllByGenero) {
										// Lista que contém as músicas que
										// serão
										// recomendadas do usuário em
										// questão.
										listaAMMusicasUsuarios = new ArrayList<AvaliarMusica>();
										listaAMMusicasUsuarios = avaliarMusicaDAO
												.pesquisaAvaliacaoUsuarioMaior3(
														mediaUsuarioGenero
																.getUsuario(),
														mediaUsuarioGenero
																.getGenero());

										// Percorre a lista de Músicas
										// encontrada do usuário em questão.
										if (listaAMMusicasUsuarios != null
												&& listaAMMusicasUsuarios
														.size() > 0) {
											for (AvaliarMusica avaliarMusica : listaAMMusicasUsuarios) {
												// Verifica se ela já
												// existia na
												// lista de usuários
												// anteriores
												// a esse (MELHORIA DE
												// PROCESSAMENTO)
												if (listaDeUsuariosAnteriores
														.contains(avaliarMusica
																.getMusica()
																.getPkMusica())) {
													continue;
												} else {
													listaDeUsuariosAnteriores
															.add(avaliarMusica
																	.getMusica()
																	.getPkMusica());
												}

												// Caso o HashMap esteja
												// vazio,
												// insere o gênero nele,
												// adiciona a música na
												// lista de
												// músicas daquele gênero e
												// adiciona a música na
												// lista
												// auxiliar.
												if (GuardaMusicasRecomendadas
														.getTokensExisteMusica()
														.isEmpty()) {
													// Verifica se a lista
													// de
													// músicas que o usuário
													// curtiu esta cheia,
													// para
													// fazer as verificações
													// e
													// evitar recomendar
													// músicas
													// que o usuário já
													// curtiu
													if (listaMusicasUsuarioGenero != null
															&& listaMusicasUsuarioGenero
																	.size() > 0) {
														existeMusicaUsuario = false;
														for (Musica musica : listaMusicasUsuarioGenero) {
															if (musica
																	.getIdMusica() == avaliarMusica
																	.getMusica()
																	.getIdMusica()) {
																existeMusicaUsuario = true;
																break;
															}
														}

														if (!existeMusicaUsuario) {
															// Caso não
															// exista o
															// gênero no
															// hash
															// principal,
															// insere
															// o gênero
															// nele,
															// adiciona a
															// música
															// na lista de
															// músicas
															// daquele
															// gênero e
															// adiciona
															// a música na
															// lista
															// auxiliar.
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.put(mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero(),
																			new ArrayList<Long>());
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.get(mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero())
																	.add(avaliarMusica
																			.getMusica()
																			.getPkMusica());

															existeListaAnterior = false;
															if (listaIM1 != null
																	&& listaIM1
																			.getListaMusica() != null
																	&& listaIM1
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM1
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior
																	&& listaIM2 != null
																	&& listaIM2
																			.getListaMusica() != null
																	&& listaIM2
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM2
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior) {
																listaIMAux
																		.add(avaliarMusica
																				.getMusica());
															}
														}
													} else {
														// Caso não exista o
														// gênero no hash
														// principal, insere
														// o
														// gênero nele,
														// adiciona
														// a música na lista
														// de
														// músicas daquele
														// gênero e adiciona
														// a
														// música na lista
														// auxiliar.
														GuardaMusicasRecomendadas
																.getTokensExisteMusica()
																.put(mediaUsuarioGenero
																		.getGenero()
																		.getPkGenero(),
																		new ArrayList<Long>());
														GuardaMusicasRecomendadas
																.getTokensExisteMusica()
																.get(mediaUsuarioGenero
																		.getGenero()
																		.getPkGenero())
																.add(avaliarMusica
																		.getMusica()
																		.getPkMusica());

														existeListaAnterior = false;
														if (listaIM1 != null
																&& listaIM1
																		.getListaMusica() != null
																&& listaIM1
																		.getListaMusica()
																		.size() > 0) {
															for (Musica musica : listaIM1
																	.getListaMusica()) {
																if (avaliarMusica
																		.getMusica()
																		.getPkMusica() == musica
																		.getPkMusica()) {
																	existeListaAnterior = true;
																	break;
																}
															}
														}

														if (!existeListaAnterior
																&& listaIM2 != null
																&& listaIM2
																		.getListaMusica() != null
																&& listaIM2
																		.getListaMusica()
																		.size() > 0) {
															for (Musica musica : listaIM2
																	.getListaMusica()) {
																if (avaliarMusica
																		.getMusica()
																		.getPkMusica() == musica
																		.getPkMusica()) {
																	existeListaAnterior = true;
																	break;
																}
															}
														}

														if (!existeListaAnterior) {
															listaIMAux
																	.add(avaliarMusica
																			.getMusica());
														}
													}

													// Caso a lista esteja
													// cheia, sai do for.
													if (listaIMAux.size() >= 6) {
														break;
													}
												} else {
													if (GuardaMusicasRecomendadas
															.getTokensExisteMusica()
															.containsKey(
																	mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero())) {
														// Caso o Hash
														// principal
														// possua o gênero
														// em
														// questão, então
														// procura nas suas
														// listas de
														// músicas, se
														// existe a música
														// corrente, se
														// existir
														// seta existeHash
														// como
														// true (Flag para
														// indicar que
														// possuem
														// músicas no hash
														// que
														// já foram
														// recomendas e
														// caso seja
														// necessário,
														// verificar se ele
														// precisa ser
														// limpado).
														// Caso não exista,
														// seta
														// ela na lista
														// auxliar.
														existeMusica = false;
														for (Long l : GuardaMusicasRecomendadas
																.getTokensExisteMusica()
																.get(mediaUsuarioGenero
																		.getGenero()
																		.getPkGenero())) {
															if (l == avaliarMusica
																	.getMusica()
																	.getPkMusica()) {
																existeHash = true;
																existeMusica = true;
																break;
															}
														}

														if (!existeMusica) {
															// Verifica se a
															// lista de
															// músicas
															// que o usuário
															// curtiu esta
															// cheia, para
															// fazer
															// as
															// verificações
															// e
															// evitar
															// recomendar
															// músicas que o
															// usuário já
															// curtiu
															if (listaMusicasUsuarioGenero != null
																	&& listaMusicasUsuarioGenero
																			.size() > 0) {
																existeMusicaUsuario = false;
																for (Musica musica : listaMusicasUsuarioGenero) {
																	if (musica
																			.getIdMusica() == avaliarMusica
																			.getMusica()
																			.getIdMusica()) {
																		existeMusicaUsuario = true;
																		break;
																	}
																}

																if (!existeMusicaUsuario) {
																	// Insere
																	// a
																	// música
																	// na
																	// lista
																	GuardaMusicasRecomendadas
																			.getTokensExisteMusica()
																			.get(mediaUsuarioGenero
																					.getGenero()
																					.getPkGenero())
																			.add(avaliarMusica
																					.getMusica()
																					.getPkMusica());

																	existeListaAnterior = false;
																	if (listaIM1 != null
																			&& listaIM1
																					.getListaMusica() != null
																			&& listaIM1
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM1
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior
																			&& listaIM2 != null
																			&& listaIM2
																					.getListaMusica() != null
																			&& listaIM2
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM2
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior) {
																		listaIMAux
																				.add(avaliarMusica
																						.getMusica());
																	}
																}
															} else {
																// Insere a
																// música na
																// lista
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.get(mediaUsuarioGenero
																				.getGenero()
																				.getPkGenero())
																		.add(avaliarMusica
																				.getMusica()
																				.getPkMusica());

																existeListaAnterior = false;
																if (listaIM1 != null
																		&& listaIM1
																				.getListaMusica() != null
																		&& listaIM1
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM1
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior
																		&& listaIM2 != null
																		&& listaIM2
																				.getListaMusica() != null
																		&& listaIM2
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM2
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior) {
																	listaIMAux
																			.add(avaliarMusica
																					.getMusica());
																}
															}
														}

														// Caso a lista
														// esteja
														// cheia, sai do
														// for.
														if (listaIMAux.size() >= 6) {
															break;
														}
													} else {
														// Verifica se a
														// lista
														// de músicas que o
														// usuário curtiu
														// esta
														// cheia, para fazer
														// as
														// verificações e
														// evitar
														// recomendar
														// músicas
														// que o usuário já
														// curtiu
														if (listaMusicasUsuarioGenero != null
																&& listaMusicasUsuarioGenero
																		.size() > 0) {
															existeMusicaUsuario = false;
															for (Musica musica : listaMusicasUsuarioGenero) {
																if (musica
																		.getIdMusica() == avaliarMusica
																		.getMusica()
																		.getIdMusica()) {
																	existeMusicaUsuario = true;
																	break;
																}
															}

															if (!existeMusicaUsuario) {
																// Caso não
																// exista o
																// gênero no
																// hash
																// principal,
																// insere o
																// gênero
																// nele,
																// adiciona
																// a
																// música na
																// lista de
																// músicas
																// daquele
																// gênero e
																// adiciona
																// a
																// música na
																// lista
																// auxiliar.
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.put(mediaUsuarioGenero
																				.getGenero()
																				.getPkGenero(),
																				new ArrayList<Long>());
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.get(mediaUsuarioGenero
																				.getGenero()
																				.getPkGenero())
																		.add(avaliarMusica
																				.getMusica()
																				.getPkMusica());

																existeListaAnterior = false;
																if (listaIM1 != null
																		&& listaIM1
																				.getListaMusica() != null
																		&& listaIM1
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM1
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior
																		&& listaIM2 != null
																		&& listaIM2
																				.getListaMusica() != null
																		&& listaIM2
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM2
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior) {
																	listaIMAux
																			.add(avaliarMusica
																					.getMusica());
																}
															}
														} else {
															// Caso não
															// exista o
															// gênero no
															// hash
															// principal,
															// insere
															// o gênero
															// nele,
															// adiciona a
															// música
															// na lista de
															// músicas
															// daquele
															// gênero e
															// adiciona
															// a música na
															// lista
															// auxiliar.
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.put(mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero(),
																			new ArrayList<Long>());
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.get(mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero())
																	.add(avaliarMusica
																			.getMusica()
																			.getPkMusica());

															existeListaAnterior = false;
															if (listaIM1 != null
																	&& listaIM1
																			.getListaMusica() != null
																	&& listaIM1
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM1
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior
																	&& listaIM2 != null
																	&& listaIM2
																			.getListaMusica() != null
																	&& listaIM2
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM2
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior) {
																listaIMAux
																		.add(avaliarMusica
																				.getMusica());
															}
														}

														// Caso a lista
														// esteja
														// cheia, sai do
														// for.
														if (listaIMAux.size() >= 6) {
															break;
														}
													}
												}
											}
										}
									}

									// Caso o hash de músicas recomendadas
									// esteja cheio, e por isso não tenha
									// completado a lista de músicas, então
									// limpa ele e volta para o mesmo gênero
									if (existeHash && listaIMAux.size() < 6) {
										GuardaMusicasRecomendadas
												.getTokensExisteMusica()
												.get(listaMUG.get(i)
														.getGenero()
														.getPkGenero()).clear();
										i--;
									} else {
										// Caso a lista auxiliar contenha
										// músicas, adiciona ela em algum
										// hash
										// de músicas para recomendação
										if (listaIMAux.size() > 0) {
											if (listaIM1 == null) {
												listaIM1 = new MusicasRecomendadasIM();
												listaIM1.setGenero(listaMUG
														.get(i).getGenero());
												listaIM1.setListaMusica(new ArrayList<Musica>());
												listaIM1.getListaMusica()
														.addAll(listaIMAux);
												listaIM1.setNomeGenero(listaIM1
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM1.setNota(0);

												if (listaIM1.getListaMusica()
														.size() == 0) {
													listaIM1.setTamanhoLista(1);
												}

												if (listaIM1.getListaMusica()
														.size() > 2) {
													listaIM1.setTamanhoLista(3);
												} else {
													listaIM1.setTamanhoLista(listaIM1
															.getListaMusica()
															.size());
												}
											} else if (listaIM2 == null) {
												listaIM2 = new MusicasRecomendadasIM();
												listaIM2.setGenero(listaMUG
														.get(i).getGenero());
												listaIM2.setListaMusica(new ArrayList<Musica>());
												listaIM2.getListaMusica()
														.addAll(listaIMAux);
												listaIM2.setNomeGenero(listaIM2
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM2.setNota(0);

												if (listaIM2.getListaMusica()
														.size() == 0) {
													listaIM2.setTamanhoLista(1);
												}

												if (listaIM2.getListaMusica()
														.size() > 2) {
													listaIM2.setTamanhoLista(3);
												} else {
													listaIM2.setTamanhoLista(listaIM2
															.getListaMusica()
															.size());
												}
											} else {
												listaIM3 = new MusicasRecomendadasIM();
												listaIM3.setGenero(listaMUG
														.get(i).getGenero());
												listaIM3.setListaMusica(new ArrayList<Musica>());
												listaIM3.getListaMusica()
														.addAll(listaIMAux);
												listaIM3.setNomeGenero(listaIM3
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM3.setNota(0);

												if (listaIM3.getListaMusica()
														.size() == 0) {
													listaIM3.setTamanhoLista(1);
												}

												if (listaIM3.getListaMusica()
														.size() > 2) {
													listaIM3.setTamanhoLista(3);
												} else {
													listaIM3.setTamanhoLista(listaIM3
															.getListaMusica()
															.size());
												}

												// Caso todas os hash's
												// tenham
												// sido preenchidos, então
												// sai
												// do loop
												break;
											}
										}
									}
								}
							}
							if ((listaIM1 == null || listaIM1.getListaMusica()
									.size() == 0)
									|| (listaIM2 == null || listaIM2
											.getListaMusica().size() == 0)
									|| (listaIM3 == null || listaIM3
											.getListaMusica().size() == 0)) {
								// Caso não exista significa que o usuário não
								// curtiu
								// nenhuma música então recomenda as músicas
								// mais
								// avaliadas do site
								// Para completar manda uma mensagem
								// incentivando o
								// usuário a curtir mais músicas no sistemas

								// Procura no Banco de Dados as músicas mais bem
								// avaliadas e insere ela na lista que irá
								// aparecer
								// na
								// tela do usuário
								List<Musica> listaMusicas = musicaDAO
										.pesquisaMelhoresAvaliadas();
								maisAvaliadas = new MusicasRecomendadasIM();
								maisAvaliadas
										.setListaMusica(new ArrayList<Musica>());
								maisAvaliadas.setNota(0);
								listaTodasMusicasUsuario = new ArrayList<Musica>();
								listaTodasMusicasUsuario = avaliarMusicaDAO
										.getAllAvaliacoesUsuario(getUsuarioGlobal());

								if (listaTodasMusicasUsuario != null
										&& listaTodasMusicasUsuario.size() > 0) {
									for (Musica m1 : listaMusicas) {
										if (maisAvaliadas.getListaMusica()
												.size() == 15) {
											break;
										}

										existeMusicaListaMaisAvaliadas = false;
										for (Musica m2 : listaTodasMusicasUsuario) {
											if (m1.getPkMusica() == m2
													.getPkMusica()) {
												existeMusicaListaMaisAvaliadas = true;
											}
										}

										if (!existeMusicaListaMaisAvaliadas) {
											maisAvaliadas.getListaMusica().add(
													m1);
										}
									}

									if (maisAvaliadas.getListaMusica() != null
											&& maisAvaliadas.getListaMusica()
													.size() > 2) {
										maisAvaliadas.setTamanhoLista(3);
									} else if (maisAvaliadas.getListaMusica() != null
											&& maisAvaliadas.getListaMusica()
													.size() > 0) {
										maisAvaliadas
												.setTamanhoLista(listaMusicas
														.size());
									} else {
										maisAvaliadas = null;
									}
								} else {
									if (listaMusicas != null
											&& listaMusicas.size() > 2) {
										maisAvaliadas.setTamanhoLista(3);

										if (listaMusicas.size() > 15) {
											for (Musica musica : listaMusicas) {
												if (maisAvaliadas
														.getListaMusica()
														.size() == 15) {
													break;
												}

												maisAvaliadas.getListaMusica()
														.add(musica);
											}
										} else {
											maisAvaliadas.getListaMusica()
													.addAll(listaMusicas);
										}

									} else if (listaMusicas != null
											&& listaMusicas.size() > 0) {
										maisAvaliadas
												.setTamanhoLista(listaMusicas
														.size());
										maisAvaliadas.getListaMusica().addAll(
												listaMusicas);
									} else {
										maisAvaliadas = null;
									}
								}
							}
						} else {

							List<MediaUsuarioGenero> listaAllByGenero;
							for (int i = 0; i < listaMUG.size(); i++) {
								listaMusicasUsuarioGenero = new ArrayList<Musica>();
								listaIMAux = new ArrayList<Musica>();

								// Pesquisa todas as músicas do usuário para
								// não
								// recomendar músicas que ele já tenha
								// curtido.
								listaMusicasUsuarioGenero = avaliarMusicaDAO
										.pesquisaAlMusicasUsuarioGenero(
												getUsuarioGlobal(), listaMUG
														.get(i).getGenero());

								// Dado 3 gênero que estão entre os que
								// possuem
								// maior número de músicas curtidas,
								// pesquisa
								// outros usuários que também curtiram este
								// gênero
								listaAllByGenero = new ArrayList<MediaUsuarioGenero>();
								listaAllByGenero = mediaUsuarioGeneroDAO
										.pesquisaAllByGenero(listaMUG.get(i)
												.getGenero(),
												getUsuarioGlobal());
								/*
								 * Roda o KNN/K-Means, procurando os usuários
								 * mais próximos do gênero corrente
								 */
								if (listaAllByGenero.size() > 2) {
									listaAllByGenero.add(listaMUG.get(i));
									retornoKMeans = new RetornoKMeans();
									retornoKMeans = KMeans
											.rodaAlgoritmo(listaAllByGenero);
									posicaoKMeans = 0;

									for (int x = 0; x < KMeans.Blocos; x++) {
										for (int y = 0; y < retornoKMeans
												.getTamanho(); y++) {
											if (retornoKMeans
													.getDivisaoBlocos()[x][y] == listaMUG
													.get(i).getMedia()) {
												posicaoKMeans = x;
												break;
											}
										}
									}

									listaUsuariosKMeans = new ArrayList<MediaUsuarioGenero>();
									listaAllByGenero.remove(listaAllByGenero
											.size() - 1);

									for (int x = 0; x < retornoKMeans
											.getTamanho(); x++) {
										for (MediaUsuarioGenero mediaUsuarioGenero : listaAllByGenero) {
											if (retornoKMeans
													.getDivisaoBlocos()[posicaoKMeans][x] == mediaUsuarioGenero
													.getMedia()) {
												listaUsuariosKMeans
														.add(mediaUsuarioGenero);
												retornoKMeans
														.getDivisaoBlocos()[posicaoKMeans][x] = -9999;
											}
										}
									}

									existeHash = false;

									if (listaUsuariosKMeans != null
											&& listaUsuariosKMeans.size() > 0) {
										// Limpa a lista de usuários
										// anteriores
										// pois, o gênero foi trocado.
										listaDeUsuariosAnteriores = new ArrayList<Long>();
										for (int j = 0; j < listaUsuariosKMeans
												.size(); j++) {
											// Lista que contém as músicas
											// que
											// serão recomendadas do usuário
											// em
											// questão.
											listaAMMusicasUsuarios = new ArrayList<AvaliarMusica>();
											listaAMMusicasUsuarios = avaliarMusicaDAO
													.pesquisaAvaliacaoUsuarioMaior3(
															listaUsuariosKMeans
																	.get(j)
																	.getUsuario(),
															listaUsuariosKMeans
																	.get(j)
																	.getGenero());

											// Percorre a lista de Músicas
											// encontrada do usuário em
											// questão.
											if (listaAMMusicasUsuarios != null
													&& listaAMMusicasUsuarios
															.size() > 0) {
												for (AvaliarMusica avaliarMusica : listaAMMusicasUsuarios) {
													// Verifica se ela já
													// existia na lista de
													// usuários anteriores a
													// esse (MELHORIA DE
													// PROCESSAMENTO)
													if (listaDeUsuariosAnteriores
															.contains(avaliarMusica
																	.getMusica()
																	.getPkMusica())) {
														continue;
													} else {
														listaDeUsuariosAnteriores
																.add(avaliarMusica
																		.getMusica()
																		.getPkMusica());
													}

													// Caso o HashMap esteja
													// vazio, insere o
													// gênero
													// nele, adiciona a
													// música
													// na lista de músicas
													// daquele gênero e
													// adiciona
													// a música na lista
													// auxiliar.
													if (GuardaMusicasRecomendadas
															.getTokensExisteMusica()
															.isEmpty()) {
														// Verifica se a
														// lista
														// de músicas que o
														// usuário curtiu
														// esta
														// cheia, para fazer
														// as
														// verificações e
														// evitar
														// recomendar
														// músicas
														// que o usuário já
														// curtiu
														if (listaMusicasUsuarioGenero != null
																&& listaMusicasUsuarioGenero
																		.size() > 0) {
															existeMusicaUsuario = false;
															for (Musica musica : listaMusicasUsuarioGenero) {
																if (musica
																		.getIdMusica() == avaliarMusica
																		.getMusica()
																		.getIdMusica()) {
																	existeMusicaUsuario = true;
																	break;
																}
															}

															if (!existeMusicaUsuario) {
																// Insere a
																// música na
																// lista.
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.put(listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero(),
																				new ArrayList<Long>());
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.get(listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero())
																		.add(avaliarMusica
																				.getMusica()
																				.getPkMusica());

																existeListaAnterior = false;
																if (listaIM1 != null
																		&& listaIM1
																				.getListaMusica() != null
																		&& listaIM1
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM1
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior
																		&& listaIM2 != null
																		&& listaIM2
																				.getListaMusica() != null
																		&& listaIM2
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM2
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior) {
																	listaIMAux
																			.add(avaliarMusica
																					.getMusica());
																}
															}
														} else {
															// Insere a
															// música
															// na lista.
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.put(listaUsuariosKMeans
																			.get(j)
																			.getGenero()
																			.getPkGenero(),
																			new ArrayList<Long>());
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.get(listaUsuariosKMeans
																			.get(j)
																			.getGenero()
																			.getPkGenero())
																	.add(avaliarMusica
																			.getMusica()
																			.getPkMusica());

															existeListaAnterior = false;
															if (listaIM1 != null
																	&& listaIM1
																			.getListaMusica() != null
																	&& listaIM1
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM1
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior
																	&& listaIM2 != null
																	&& listaIM2
																			.getListaMusica() != null
																	&& listaIM2
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM2
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior) {
																listaIMAux
																		.add(avaliarMusica
																				.getMusica());
															}
														}

														// Caso a lista
														// esteja
														// cheia, sai do
														// for.
														if (listaIMAux.size() >= 6) {
															break;
														}
													} else {
														if (GuardaMusicasRecomendadas
																.getTokensExisteMusica()
																.containsKey(
																		listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero())) {
															// Caso o Hash
															// principal
															// possua
															// o gênero em
															// questão,
															// então
															// procura nas
															// suas
															// listas de
															// músicas, se
															// existe a
															// música
															// corrente, se
															// existir seta
															// existeHash
															// como
															// true (Flag
															// para
															// indicar que
															// possuem
															// músicas
															// no hash que
															// já
															// foram
															// recomendas
															// e caso seja
															// necessário,
															// verificar se
															// ele
															// precisa ser
															// limpado).
															// Caso não
															// exista,
															// seta ela na
															// lista
															// auxliar.
															existeMusica = false;
															for (Long l : GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.get(listaUsuariosKMeans
																			.get(j)
																			.getGenero()
																			.getPkGenero())) {
																if (l == avaliarMusica
																		.getMusica()
																		.getPkMusica()) {
																	existeHash = true;
																	existeMusica = true;
																	break;
																}
															}

															// Caso a música
															// não
															// exista no
															// Hash
															// principal
															if (!existeMusica) {
																// Verifica
																// se a
																// lista de
																// músicas
																// que o
																// usuário
																// curtiu
																// esta
																// cheia,
																// para
																// fazer as
																// verificações
																// e evitar
																// recomendar
																// músicas
																// que o
																// usuário
																// já
																// curtiu
																if (listaMusicasUsuarioGenero != null
																		&& listaMusicasUsuarioGenero
																				.size() > 0) {
																	existeMusicaUsuario = false;
																	for (Musica musica : listaMusicasUsuarioGenero) {
																		if (musica
																				.getIdMusica() == avaliarMusica
																				.getMusica()
																				.getIdMusica()) {
																			existeMusicaUsuario = true;
																			break;
																		}
																	}

																	if (!existeMusicaUsuario) {
																		// Insere
																		// a
																		// música
																		// na
																		// lista.
																		GuardaMusicasRecomendadas
																				.getTokensExisteMusica()
																				.get(listaUsuariosKMeans
																						.get(j)
																						.getGenero()
																						.getPkGenero())
																				.add(avaliarMusica
																						.getMusica()
																						.getPkMusica());

																		existeListaAnterior = false;
																		if (listaIM1 != null
																				&& listaIM1
																						.getListaMusica() != null
																				&& listaIM1
																						.getListaMusica()
																						.size() > 0) {
																			for (Musica musica : listaIM1
																					.getListaMusica()) {
																				if (avaliarMusica
																						.getMusica()
																						.getPkMusica() == musica
																						.getPkMusica()) {
																					existeListaAnterior = true;
																					break;
																				}
																			}
																		}

																		if (!existeListaAnterior
																				&& listaIM2 != null
																				&& listaIM2
																						.getListaMusica() != null
																				&& listaIM2
																						.getListaMusica()
																						.size() > 0) {
																			for (Musica musica : listaIM2
																					.getListaMusica()) {
																				if (avaliarMusica
																						.getMusica()
																						.getPkMusica() == musica
																						.getPkMusica()) {
																					existeListaAnterior = true;
																					break;
																				}
																			}
																		}

																		if (!existeListaAnterior) {
																			listaIMAux
																					.add(avaliarMusica
																							.getMusica());
																		}
																	}
																} else {
																	// Insere
																	// a
																	// música
																	// na
																	// lista.
																	GuardaMusicasRecomendadas
																			.getTokensExisteMusica()
																			.get(listaUsuariosKMeans
																					.get(j)
																					.getGenero()
																					.getPkGenero())
																			.add(avaliarMusica
																					.getMusica()
																					.getPkMusica());

																	existeListaAnterior = false;
																	if (listaIM1 != null
																			&& listaIM1
																					.getListaMusica() != null
																			&& listaIM1
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM1
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior
																			&& listaIM2 != null
																			&& listaIM2
																					.getListaMusica() != null
																			&& listaIM2
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM2
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior) {
																		listaIMAux
																				.add(avaliarMusica
																						.getMusica());
																	}
																}
															}

															// Caso a lista
															// esteja cheia,
															// sai
															// do for.
															if (listaIMAux
																	.size() >= 6) {
																break;
															}
														} else {
															// Verifica se a
															// lista de
															// músicas
															// que o usuário
															// curtiu esta
															// cheia, para
															// fazer
															// as
															// verificações
															// e
															// evitar
															// recomendar
															// músicas que o
															// usuário já
															// curtiu
															if (listaMusicasUsuarioGenero != null
																	&& listaMusicasUsuarioGenero
																			.size() > 0) {
																existeMusicaUsuario = false;
																for (Musica musica : listaMusicasUsuarioGenero) {
																	if (musica
																			.getIdMusica() == avaliarMusica
																			.getMusica()
																			.getIdMusica()) {
																		existeMusicaUsuario = true;
																		break;
																	}
																}

																if (!existeMusicaUsuario) {
																	// Caso
																	// não
																	// exista
																	// o
																	// gênero
																	// no
																	// hash
																	// principal,
																	// insere
																	// o
																	// gênero
																	// nele,
																	// adiciona
																	// a
																	// música
																	// na
																	// lista
																	// de
																	// músicas
																	// daquele
																	// gênero
																	// e
																	// adiciona
																	// a
																	// música
																	// na
																	// lista
																	// auxiliar.
																	GuardaMusicasRecomendadas
																			.getTokensExisteMusica()
																			.put(listaUsuariosKMeans
																					.get(j)
																					.getGenero()
																					.getPkGenero(),
																					new ArrayList<Long>());
																	GuardaMusicasRecomendadas
																			.getTokensExisteMusica()
																			.get(listaUsuariosKMeans
																					.get(j)
																					.getGenero()
																					.getPkGenero())
																			.add(avaliarMusica
																					.getMusica()
																					.getPkMusica());

																	existeListaAnterior = false;
																	if (listaIM1 != null
																			&& listaIM1
																					.getListaMusica() != null
																			&& listaIM1
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM1
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior
																			&& listaIM2 != null
																			&& listaIM2
																					.getListaMusica() != null
																			&& listaIM2
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM2
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior) {
																		listaIMAux
																				.add(avaliarMusica
																						.getMusica());
																	}
																}
															} else {
																// Caso não
																// exista o
																// gênero no
																// hash
																// principal,
																// insere o
																// gênero
																// nele,
																// adiciona
																// a
																// música na
																// lista de
																// músicas
																// daquele
																// gênero e
																// adiciona
																// a
																// música na
																// lista
																// auxiliar.
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.put(listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero(),
																				new ArrayList<Long>());
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.get(listaUsuariosKMeans
																				.get(j)
																				.getGenero()
																				.getPkGenero())
																		.add(avaliarMusica
																				.getMusica()
																				.getPkMusica());

																existeListaAnterior = false;
																if (listaIM1 != null
																		&& listaIM1
																				.getListaMusica() != null
																		&& listaIM1
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM1
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior
																		&& listaIM2 != null
																		&& listaIM2
																				.getListaMusica() != null
																		&& listaIM2
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM2
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior) {
																	listaIMAux
																			.add(avaliarMusica
																					.getMusica());
																}
															}

															// Caso a lista
															// esteja cheia,
															// sai
															// do for.
															if (listaIMAux
																	.size() >= 6) {
																break;
															}
														}
													}
												}
											}

											// Caso a lista esteja cheia,
											// sai do
											// for.
											if (listaIMAux.size() >= 6) {
												break;
											}
										}
									}

									// Caso o hash de músicas recomendadas
									// esteja cheio, e por isso não tenha
									// completado a lista de músicas, então
									// limpa ele e volta para o mesmo gênero
									if (existeHash && listaIMAux.size() < 6) {
										GuardaMusicasRecomendadas
												.getTokensExisteMusica()
												.get(listaMUG.get(i)
														.getGenero()
														.getPkGenero()).clear();
										i--;
									} else {
										// Caso a lista auxiliar contenha
										// músicas, adiciona ela em algum
										// hash
										// de músicas para recomendação
										if (listaIMAux.size() > 0) {
											if (listaIM1 == null) {
												listaIM1 = new MusicasRecomendadasIM();
												listaIM1.setGenero(listaMUG
														.get(i).getGenero());
												listaIM1.setListaMusica(new ArrayList<Musica>());
												listaIM1.getListaMusica()
														.addAll(listaIMAux);
												listaIM1.setNomeGenero(listaIM1
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM1.setNota(0);

												if (listaIM1.getListaMusica()
														.size() == 0) {
													listaIM1.setTamanhoLista(1);
												}

												if (listaIM1.getListaMusica()
														.size() > 2) {
													listaIM1.setTamanhoLista(3);
												} else {
													listaIM1.setTamanhoLista(listaIM1
															.getListaMusica()
															.size());
												}
											} else if (listaIM2 == null) {
												listaIM2 = new MusicasRecomendadasIM();
												listaIM2.setGenero(listaMUG
														.get(i).getGenero());
												listaIM2.setListaMusica(new ArrayList<Musica>());
												listaIM2.getListaMusica()
														.addAll(listaIMAux);
												listaIM2.setNomeGenero(listaIM2
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM2.setNota(0);

												if (listaIM2.getListaMusica()
														.size() == 0) {
													listaIM2.setTamanhoLista(1);
												}

												if (listaIM2.getListaMusica()
														.size() > 2) {
													listaIM2.setTamanhoLista(3);
												} else {
													listaIM2.setTamanhoLista(listaIM2
															.getListaMusica()
															.size());
												}
											}
										}
									}
								} else if (listaAllByGenero.size() == 1
										|| listaAllByGenero.size() == 2) {
									existeHash = false;
									listaDeUsuariosAnteriores = new ArrayList<Long>();
									for (MediaUsuarioGenero mediaUsuarioGenero : listaAllByGenero) {
										// Lista que contém as músicas que
										// serão
										// recomendadas do usuário em
										// questão.
										listaAMMusicasUsuarios = new ArrayList<AvaliarMusica>();
										listaAMMusicasUsuarios = avaliarMusicaDAO
												.pesquisaAvaliacaoUsuarioMaior3(
														mediaUsuarioGenero
																.getUsuario(),
														mediaUsuarioGenero
																.getGenero());

										// Percorre a lista de Músicas
										// encontrada do usuário em questão.
										if (listaAMMusicasUsuarios != null
												&& listaAMMusicasUsuarios
														.size() > 0) {
											for (AvaliarMusica avaliarMusica : listaAMMusicasUsuarios) {
												// Verifica se ela já
												// existia na
												// lista de usuários
												// anteriores
												// a esse (MELHORIA DE
												// PROCESSAMENTO)
												if (listaDeUsuariosAnteriores
														.contains(avaliarMusica
																.getMusica()
																.getPkMusica())) {
													continue;
												} else {
													listaDeUsuariosAnteriores
															.add(avaliarMusica
																	.getMusica()
																	.getPkMusica());
												}

												// Caso o HashMap esteja
												// vazio,
												// insere o gênero nele,
												// adiciona a música na
												// lista de
												// músicas daquele gênero e
												// adiciona a música na
												// lista
												// auxiliar.
												if (GuardaMusicasRecomendadas
														.getTokensExisteMusica()
														.isEmpty()) {
													// Verifica se a lista
													// de
													// músicas que o usuário
													// curtiu esta cheia,
													// para
													// fazer as verificações
													// e
													// evitar recomendar
													// músicas
													// que o usuário já
													// curtiu
													if (listaMusicasUsuarioGenero != null
															&& listaMusicasUsuarioGenero
																	.size() > 0) {
														existeMusicaUsuario = false;
														for (Musica musica : listaMusicasUsuarioGenero) {
															if (musica
																	.getIdMusica() == avaliarMusica
																	.getMusica()
																	.getIdMusica()) {
																existeMusicaUsuario = true;
																break;
															}
														}

														if (!existeMusicaUsuario) {
															// Caso não
															// exista o
															// gênero no
															// hash
															// principal,
															// insere
															// o gênero
															// nele,
															// adiciona a
															// música
															// na lista de
															// músicas
															// daquele
															// gênero e
															// adiciona
															// a música na
															// lista
															// auxiliar.
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.put(mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero(),
																			new ArrayList<Long>());
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.get(mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero())
																	.add(avaliarMusica
																			.getMusica()
																			.getPkMusica());

															existeListaAnterior = false;
															if (listaIM1 != null
																	&& listaIM1
																			.getListaMusica() != null
																	&& listaIM1
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM1
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior
																	&& listaIM2 != null
																	&& listaIM2
																			.getListaMusica() != null
																	&& listaIM2
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM2
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior) {
																listaIMAux
																		.add(avaliarMusica
																				.getMusica());
															}
														}
													} else {
														// Caso não exista o
														// gênero no hash
														// principal, insere
														// o
														// gênero nele,
														// adiciona
														// a música na lista
														// de
														// músicas daquele
														// gênero e adiciona
														// a
														// música na lista
														// auxiliar.
														GuardaMusicasRecomendadas
																.getTokensExisteMusica()
																.put(mediaUsuarioGenero
																		.getGenero()
																		.getPkGenero(),
																		new ArrayList<Long>());
														GuardaMusicasRecomendadas
																.getTokensExisteMusica()
																.get(mediaUsuarioGenero
																		.getGenero()
																		.getPkGenero())
																.add(avaliarMusica
																		.getMusica()
																		.getPkMusica());

														existeListaAnterior = false;
														if (listaIM1 != null
																&& listaIM1
																		.getListaMusica() != null
																&& listaIM1
																		.getListaMusica()
																		.size() > 0) {
															for (Musica musica : listaIM1
																	.getListaMusica()) {
																if (avaliarMusica
																		.getMusica()
																		.getPkMusica() == musica
																		.getPkMusica()) {
																	existeListaAnterior = true;
																	break;
																}
															}
														}

														if (!existeListaAnterior
																&& listaIM2 != null
																&& listaIM2
																		.getListaMusica() != null
																&& listaIM2
																		.getListaMusica()
																		.size() > 0) {
															for (Musica musica : listaIM2
																	.getListaMusica()) {
																if (avaliarMusica
																		.getMusica()
																		.getPkMusica() == musica
																		.getPkMusica()) {
																	existeListaAnterior = true;
																	break;
																}
															}
														}

														if (!existeListaAnterior) {
															listaIMAux
																	.add(avaliarMusica
																			.getMusica());
														}
													}

													// Caso a lista esteja
													// cheia, sai do for.
													if (listaIMAux.size() >= 6) {
														break;
													}
												} else {
													if (GuardaMusicasRecomendadas
															.getTokensExisteMusica()
															.containsKey(
																	mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero())) {
														// Caso o Hash
														// principal
														// possua o gênero
														// em
														// questão, então
														// procura nas suas
														// listas de
														// músicas, se
														// existe a música
														// corrente, se
														// existir
														// seta existeHash
														// como
														// true (Flag para
														// indicar que
														// possuem
														// músicas no hash
														// que
														// já foram
														// recomendas e
														// caso seja
														// necessário,
														// verificar se ele
														// precisa ser
														// limpado).
														// Caso não exista,
														// seta
														// ela na lista
														// auxliar.
														existeMusica = false;
														for (Long l : GuardaMusicasRecomendadas
																.getTokensExisteMusica()
																.get(mediaUsuarioGenero
																		.getGenero()
																		.getPkGenero())) {
															if (l == avaliarMusica
																	.getMusica()
																	.getPkMusica()) {
																existeHash = true;
																existeMusica = true;
																break;
															}
														}

														if (!existeMusica) {
															// Verifica se a
															// lista de
															// músicas
															// que o usuário
															// curtiu esta
															// cheia, para
															// fazer
															// as
															// verificações
															// e
															// evitar
															// recomendar
															// músicas que o
															// usuário já
															// curtiu
															if (listaMusicasUsuarioGenero != null
																	&& listaMusicasUsuarioGenero
																			.size() > 0) {
																existeMusicaUsuario = false;
																for (Musica musica : listaMusicasUsuarioGenero) {
																	if (musica
																			.getIdMusica() == avaliarMusica
																			.getMusica()
																			.getIdMusica()) {
																		existeMusicaUsuario = true;
																		break;
																	}
																}

																if (!existeMusicaUsuario) {
																	// Insere
																	// a
																	// música
																	// na
																	// lista
																	GuardaMusicasRecomendadas
																			.getTokensExisteMusica()
																			.get(mediaUsuarioGenero
																					.getGenero()
																					.getPkGenero())
																			.add(avaliarMusica
																					.getMusica()
																					.getPkMusica());

																	existeListaAnterior = false;
																	if (listaIM1 != null
																			&& listaIM1
																					.getListaMusica() != null
																			&& listaIM1
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM1
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior
																			&& listaIM2 != null
																			&& listaIM2
																					.getListaMusica() != null
																			&& listaIM2
																					.getListaMusica()
																					.size() > 0) {
																		for (Musica musica : listaIM2
																				.getListaMusica()) {
																			if (avaliarMusica
																					.getMusica()
																					.getPkMusica() == musica
																					.getPkMusica()) {
																				existeListaAnterior = true;
																				break;
																			}
																		}
																	}

																	if (!existeListaAnterior) {
																		listaIMAux
																				.add(avaliarMusica
																						.getMusica());
																	}
																}
															} else {
																// Insere a
																// música na
																// lista
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.get(mediaUsuarioGenero
																				.getGenero()
																				.getPkGenero())
																		.add(avaliarMusica
																				.getMusica()
																				.getPkMusica());

																existeListaAnterior = false;
																if (listaIM1 != null
																		&& listaIM1
																				.getListaMusica() != null
																		&& listaIM1
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM1
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior
																		&& listaIM2 != null
																		&& listaIM2
																				.getListaMusica() != null
																		&& listaIM2
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM2
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior) {
																	listaIMAux
																			.add(avaliarMusica
																					.getMusica());
																}
															}
														}

														// Caso a lista
														// esteja
														// cheia, sai do
														// for.
														if (listaIMAux.size() >= 6) {
															break;
														}
													} else {
														// Verifica se a
														// lista
														// de músicas que o
														// usuário curtiu
														// esta
														// cheia, para fazer
														// as
														// verificações e
														// evitar
														// recomendar
														// músicas
														// que o usuário já
														// curtiu
														if (listaMusicasUsuarioGenero != null
																&& listaMusicasUsuarioGenero
																		.size() > 0) {
															existeMusicaUsuario = false;
															for (Musica musica : listaMusicasUsuarioGenero) {
																if (musica
																		.getIdMusica() == avaliarMusica
																		.getMusica()
																		.getIdMusica()) {
																	existeMusicaUsuario = true;
																	break;
																}
															}

															if (!existeMusicaUsuario) {
																// Caso não
																// exista o
																// gênero no
																// hash
																// principal,
																// insere o
																// gênero
																// nele,
																// adiciona
																// a
																// música na
																// lista de
																// músicas
																// daquele
																// gênero e
																// adiciona
																// a
																// música na
																// lista
																// auxiliar.
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.put(mediaUsuarioGenero
																				.getGenero()
																				.getPkGenero(),
																				new ArrayList<Long>());
																GuardaMusicasRecomendadas
																		.getTokensExisteMusica()
																		.get(mediaUsuarioGenero
																				.getGenero()
																				.getPkGenero())
																		.add(avaliarMusica
																				.getMusica()
																				.getPkMusica());

																existeListaAnterior = false;
																if (listaIM1 != null
																		&& listaIM1
																				.getListaMusica() != null
																		&& listaIM1
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM1
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior
																		&& listaIM2 != null
																		&& listaIM2
																				.getListaMusica() != null
																		&& listaIM2
																				.getListaMusica()
																				.size() > 0) {
																	for (Musica musica : listaIM2
																			.getListaMusica()) {
																		if (avaliarMusica
																				.getMusica()
																				.getPkMusica() == musica
																				.getPkMusica()) {
																			existeListaAnterior = true;
																			break;
																		}
																	}
																}

																if (!existeListaAnterior) {
																	listaIMAux
																			.add(avaliarMusica
																					.getMusica());
																}
															}
														} else {
															// Caso não
															// exista o
															// gênero no
															// hash
															// principal,
															// insere
															// o gênero
															// nele,
															// adiciona a
															// música
															// na lista de
															// músicas
															// daquele
															// gênero e
															// adiciona
															// a música na
															// lista
															// auxiliar.
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.put(mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero(),
																			new ArrayList<Long>());
															GuardaMusicasRecomendadas
																	.getTokensExisteMusica()
																	.get(mediaUsuarioGenero
																			.getGenero()
																			.getPkGenero())
																	.add(avaliarMusica
																			.getMusica()
																			.getPkMusica());

															existeListaAnterior = false;
															if (listaIM1 != null
																	&& listaIM1
																			.getListaMusica() != null
																	&& listaIM1
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM1
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior
																	&& listaIM2 != null
																	&& listaIM2
																			.getListaMusica() != null
																	&& listaIM2
																			.getListaMusica()
																			.size() > 0) {
																for (Musica musica : listaIM2
																		.getListaMusica()) {
																	if (avaliarMusica
																			.getMusica()
																			.getPkMusica() == musica
																			.getPkMusica()) {
																		existeListaAnterior = true;
																		break;
																	}
																}
															}

															if (!existeListaAnterior) {
																listaIMAux
																		.add(avaliarMusica
																				.getMusica());
															}
														}

														// Caso a lista
														// esteja
														// cheia, sai do
														// for.
														if (listaIMAux.size() >= 6) {
															break;
														}
													}
												}
											}
										}
									}

									// Caso o hash de músicas recomendadas
									// esteja cheio, e por isso não tenha
									// completado a lista de músicas, então
									// limpa ele e volta para o mesmo gênero
									if (existeHash && listaIMAux.size() < 6) {
										GuardaMusicasRecomendadas
												.getTokensExisteMusica()
												.get(listaMUG.get(i)
														.getGenero()
														.getPkGenero()).clear();
										i--;
									} else {
										// Caso a lista auxiliar contenha
										// músicas, adiciona ela em algum
										// hash
										// de músicas para recomendação
										if (listaIMAux.size() > 0) {
											if (listaIM1 == null) {
												listaIM1 = new MusicasRecomendadasIM();
												listaIM1.setGenero(listaMUG
														.get(i).getGenero());
												listaIM1.setListaMusica(new ArrayList<Musica>());
												listaIM1.getListaMusica()
														.addAll(listaIMAux);
												listaIM1.setNomeGenero(listaIM1
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM1.setNota(0);

												if (listaIM1.getListaMusica()
														.size() == 0) {
													listaIM1.setTamanhoLista(1);
												}

												if (listaIM1.getListaMusica()
														.size() > 2) {
													listaIM1.setTamanhoLista(3);
												} else {
													listaIM1.setTamanhoLista(listaIM1
															.getListaMusica()
															.size());
												}
											} else if (listaIM2 == null) {
												listaIM2 = new MusicasRecomendadasIM();
												listaIM2.setGenero(listaMUG
														.get(i).getGenero());
												listaIM2.setListaMusica(new ArrayList<Musica>());
												listaIM2.getListaMusica()
														.addAll(listaIMAux);
												listaIM2.setNomeGenero(listaIM2
														.getGenero()
														.getNomeGenero()
														.toUpperCase());
												listaIM2.setNota(0);

												if (listaIM2.getListaMusica()
														.size() == 0) {
													listaIM2.setTamanhoLista(1);
												}

												if (listaIM2.getListaMusica()
														.size() > 2) {
													listaIM2.setTamanhoLista(3);
												} else {
													listaIM2.setTamanhoLista(listaIM2
															.getListaMusica()
															.size());
												}
											}
										}
									}
								}
							}

							// Não existe número suficientes de gêneros a
							// serem
							// recomendados, então serão recomendados as
							// músicas
							// mais avaliadas do site para
							// completar a recomendação

							// Procura no Banco de Dados as músicas mais bem
							// avaliadas e insere ela na lista que irá
							// aparecer
							// na tela do usuário
							List<Musica> listaMusicas = musicaDAO
									.pesquisaMelhoresAvaliadas();
							maisAvaliadas = new MusicasRecomendadasIM();
							maisAvaliadas
									.setListaMusica(new ArrayList<Musica>());
							maisAvaliadas.setNota(0);
							listaTodasMusicasUsuario = new ArrayList<Musica>();
							listaTodasMusicasUsuario = avaliarMusicaDAO
									.getAllAvaliacoesUsuario(getUsuarioGlobal());

							if (listaTodasMusicasUsuario != null
									&& listaTodasMusicasUsuario.size() > 0) {
								for (Musica m1 : listaMusicas) {
									if (maisAvaliadas.getListaMusica().size() == 15) {
										break;
									}

									existeMusicaListaMaisAvaliadas = false;
									for (Musica m2 : listaTodasMusicasUsuario) {
										if (m1.getPkMusica() == m2
												.getPkMusica()) {
											existeMusicaListaMaisAvaliadas = true;
										}
									}

									if (!existeMusicaListaMaisAvaliadas) {
										maisAvaliadas.getListaMusica().add(m1);
									}
								}

								if (maisAvaliadas.getListaMusica() != null
										&& maisAvaliadas.getListaMusica()
												.size() > 2) {
									maisAvaliadas.setTamanhoLista(3);
								} else if (maisAvaliadas.getListaMusica() != null
										&& maisAvaliadas.getListaMusica()
												.size() > 0) {
									maisAvaliadas.setTamanhoLista(listaMusicas
											.size());
								} else {
									maisAvaliadas = null;
								}
							} else {
								if (listaMusicas != null
										&& listaMusicas.size() > 2) {
									maisAvaliadas.setTamanhoLista(3);

									if (listaMusicas.size() > 15) {
										for (Musica musica : listaMusicas) {
											if (maisAvaliadas.getListaMusica()
													.size() == 15) {
												break;
											}

											maisAvaliadas.getListaMusica().add(
													musica);
										}
									} else {
										maisAvaliadas.getListaMusica().addAll(
												listaMusicas);
									}

								} else if (listaMusicas != null
										&& listaMusicas.size() > 0) {
									maisAvaliadas.setTamanhoLista(listaMusicas
											.size());
									maisAvaliadas.getListaMusica().addAll(
											listaMusicas);
								} else {
									maisAvaliadas = null;
								}
							}

							// Para completar manda uma mensagem
							// incentivando o
							// usuário a curtir mais músicas no sistemas
							this.mensagemIncentivamentoCurtidas = "Para melhorar as recomendações navegue pelo site avaliando músicas!";
							addMessage(
									mensagemIncentivamentoCurtidas,
									FacesMessage.SEVERITY_INFO);
						}
					} else if (listaMUG == null || listaMUG.size() == 0) {
						// Caso não exista significa que o usuário não
						// curtiu
						// nenhuma música então recomenda as músicas mais
						// avaliadas do site
						// Para completar manda uma mensagem incentivando o
						// usuário a curtir mais músicas no sistemas

						// Procura no Banco de Dados as músicas mais bem
						// avaliadas e insere ela na lista que irá aparecer
						// na
						// tela do usuário
						List<Musica> listaMusicas = musicaDAO
								.pesquisaMelhoresAvaliadas();
						maisAvaliadas = new MusicasRecomendadasIM();
						maisAvaliadas.setListaMusica(new ArrayList<Musica>());
						maisAvaliadas.setNota(0);
						listaTodasMusicasUsuario = new ArrayList<Musica>();
						listaTodasMusicasUsuario = avaliarMusicaDAO
								.getAllAvaliacoesUsuario(getUsuarioGlobal());

						if (listaTodasMusicasUsuario != null
								&& listaTodasMusicasUsuario.size() > 0) {
							for (Musica m1 : listaMusicas) {
								if (maisAvaliadas.getListaMusica().size() == 15) {
									break;
								}

								existeMusicaListaMaisAvaliadas = false;
								for (Musica m2 : listaTodasMusicasUsuario) {
									if (m1.getPkMusica() == m2.getPkMusica()) {
										existeMusicaListaMaisAvaliadas = true;
									}
								}

								if (!existeMusicaListaMaisAvaliadas) {
									maisAvaliadas.getListaMusica().add(m1);
								}
							}

							if (maisAvaliadas.getListaMusica() != null
									&& maisAvaliadas.getListaMusica().size() > 2) {
								maisAvaliadas.setTamanhoLista(3);
							} else if (maisAvaliadas.getListaMusica() != null
									&& maisAvaliadas.getListaMusica().size() > 0) {
								maisAvaliadas.setTamanhoLista(listaMusicas
										.size());
							} else {
								maisAvaliadas = null;
							}
						} else {
							if (listaMusicas != null && listaMusicas.size() > 2) {
								maisAvaliadas.setTamanhoLista(3);

								if (listaMusicas.size() > 15) {
									for (Musica musica : listaMusicas) {
										if (maisAvaliadas.getListaMusica()
												.size() == 15) {
											break;
										}

										maisAvaliadas.getListaMusica().add(
												musica);
									}
								} else {
									maisAvaliadas.getListaMusica().addAll(
											listaMusicas);
								}

							} else if (listaMusicas != null
									&& listaMusicas.size() > 0) {
								maisAvaliadas.setTamanhoLista(listaMusicas
										.size());
								maisAvaliadas.getListaMusica().addAll(
										listaMusicas);
							} else {
								maisAvaliadas = null;
							}
						}

						this.mensagemIncentivamentoCurtidas = "Para melhorar as recomendações navegue pelo site avaliando músicas!";
						addMessage(
								mensagemIncentivamentoCurtidas,
								FacesMessage.SEVERITY_INFO);
					}
				} else {
					encerrarSessao();
				}
			} catch (Exception e) {
				e.printStackTrace();
				ConectaBanco.getInstance().rollBack();
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SemaforoMusicasRecomendadas.getSemaforo().release();
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

	public void avaliarMusicas(String nota, String numLista) {
		try {
			ConectaBanco.getInstance().beginTransaction();

			List<Genero> listaGenero;
			MediaUsuarioGenero mUG;
			AvaliarMusica am;
			MusicasRecomendadasIM listaMusicas = null;
			int lista = Integer.valueOf(numLista);

			if (lista == 1) {
				listaMusicas = listaIM1;
			} else if (lista == 2) {
				listaMusicas = listaIM2;
			} else if (lista == 3) {
				listaMusicas = listaIM3;
			} else if (lista == 4) {
				listaMusicas = maisAvaliadas;
			}

			if (listaMusicas.getNota() == 0) {
				for (Musica m : listaMusicas.getListaMusica()) {
					listaGenero = new ArrayList<Genero>();
					listaGenero = bandaGeneroDAO.pesquisarGenerosBanda(m
							.getBanda());

					// Percorre a lista de gênero para criar/atualizar o
					// MediaUsuarioGenero
					for (Genero genero : listaGenero) {
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
							mUG.setQuantidadeMusicas(mUG.getQuantidadeMusicas() + 1);
							mUG.setMedia(((mUG.getMedia() * (mUG
									.getQuantidadeMusicas() - 1)) + m
									.getBPMMUsica())
									/ mUG.getQuantidadeMusicas());
							mUG.setMediaAvaliacoes(((mUG.getMediaAvaliacoes() * (mUG
									.getQuantidadeMusicas() - 1)) + Double
									.valueOf(nota))
									/ mUG.getQuantidadeMusicas());
						}

						mediaUsuarioGeneroDAO.salvaMediaUsuarioGenero(mUG);
					}

					// Atualizar as avaliações da música e cria a AvaliarMusica
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
					am.setNota(Integer.valueOf(nota));
					avaliarMusicaDAO.salvarAvaliacao(am);
				}
			} else {
				for (Musica m : listaMusicas.getListaMusica()) {

					am = null;
					am = avaliarMusicaDAO
							.pesquisaUsuarioAvaliouMusicaPelaMusica(m,
									getUsuarioGlobal());
					if (am != null && am.getPkAvaliarMusica() > 0) {

						listaGenero = new ArrayList<Genero>();
						listaGenero = bandaGeneroDAO.pesquisarGenerosBanda(m
								.getBanda());

						// Percorre a lista de gênero para criar/atualizar o
						// MediaUsuarioGenero
						for (Genero genero : listaGenero) {
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
										- am.getNota() + Double.valueOf(nota))
										/ mUG.getQuantidadeMusicas());
							}

							mediaUsuarioGeneroDAO.salvaMediaUsuarioGenero(mUG);
						}
					}

					m.setMediaAvaliacoes(((m.getMediaAvaliacoes() * m
							.getQuantidadeAvaliacoes()) - am.getNota() + Integer
								.valueOf(nota)) / m.getQuantidadeAvaliacoes());
					musicaDAO.salvarMusica(m);

					am.setNota(Integer.valueOf(nota));
					avaliarMusicaDAO.salvarAvaliacao(am);
				}
			}

			ConectaBanco.getInstance().commit();
			// Atribui a nota a lista das notas passada como parâmetro
			listaMusicas.setNota(Integer.valueOf(nota));

		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public List<MediaUsuarioGenero> mudarValoresLista(List<MediaUsuarioGenero> listaGenerosUsuario)
	{
		Random gerador = new Random();
		List<Integer> listaNumerosGenrados = new ArrayList<Integer>();
		List<MediaUsuarioGenero> listaGerada = new ArrayList<MediaUsuarioGenero>();
		Integer numeroGerado = 0;
		int count;
		for (int i = 0; i < listaGenerosUsuario.size(); i++) {
			count = 0;
			numeroGerado = gerador.nextInt(listaGenerosUsuario.size()-1);
			
			if(listaNumerosGenrados!=null && listaNumerosGenrados.size()>0)
			{
				if(listaNumerosGenrados.contains(numeroGerado))
				{
					while(listaNumerosGenrados.contains(numeroGerado) && count != 10)
					{
						count++;
						numeroGerado = gerador.nextInt(listaGenerosUsuario.size()-1);
					}
					
					if(count>=10 && listaNumerosGenrados.contains(numeroGerado))
					{
						for (int x = 0; x < listaGenerosUsuario.size(); x++) {
							numeroGerado = x;
							if(!listaNumerosGenrados.contains(numeroGerado))
							{
								break;
							}
						}
					}
				}
				
				
				listaGerada.add(listaGenerosUsuario.get(numeroGerado));
				listaNumerosGenrados.add(numeroGerado);
			}
			else
			{
				listaGerada.add(listaGenerosUsuario.get(numeroGerado));
				listaNumerosGenrados.add(numeroGerado);
			}
		}
		
		return listaGerada;
	}

	public String getMensagemIncentivamentoCurtidas() {
		return mensagemIncentivamentoCurtidas;
	}

	public void setMensagemIncentivamentoCurtidas(
			String mensagemIncentivamentoCurtidas) {
		this.mensagemIncentivamentoCurtidas = mensagemIncentivamentoCurtidas;
	}

	public MusicasRecomendadasIM getListaIM1() {
		return listaIM1;
	}

	public void setListaIM1(MusicasRecomendadasIM listaIM1) {
		this.listaIM1 = listaIM1;
	}

	public MusicasRecomendadasIM getListaIM2() {
		return listaIM2;
	}

	public void setListaIM2(MusicasRecomendadasIM listaIM2) {
		this.listaIM2 = listaIM2;
	}

	public MusicasRecomendadasIM getListaIM3() {
		return listaIM3;
	}

	public void setListaIM3(MusicasRecomendadasIM listaIM3) {
		this.listaIM3 = listaIM3;
	}

	public MusicasRecomendadasIM getMaisAvaliadas() {
		return maisAvaliadas;
	}

	public void setMaisAvaliadas(MusicasRecomendadasIM maisAvaliadas) {
		this.maisAvaliadas = maisAvaliadas;
	}
}