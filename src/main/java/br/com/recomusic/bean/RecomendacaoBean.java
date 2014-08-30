package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.MediaUsuarioGeneroDAO;
import br.com.recomusic.im.MusicaIM;
import br.com.recomusic.im.MusicasRecomendadasIM;
import br.com.recomusic.im.RetornoKMeans;
import br.com.recomusic.om.AvaliarMusica;
import br.com.recomusic.om.MediaUsuarioGenero;
import br.com.recomusic.om.Musica;
import br.com.recomusic.persistencia.utils.KMeans;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;
import br.com.recomusic.singleton.GuardaMusicasRecomendadas;
import br.com.recomusic.singleton.SemaforoMusicasRecomendadas;


@ManagedBean(name="RecomendacaoBean")
@ViewScoped
public class RecomendacaoBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private MediaUsuarioGeneroDAO mediaUsuarioGeneroDAO = new MediaUsuarioGeneroDAO( ConectaBanco.getInstance().getEntityManager());
	private AvaliarMusicaDAO avaliarMusicaDAO = new AvaliarMusicaDAO( ConectaBanco.getInstance().getEntityManager());
	private String mensagemIncentivamentoCurtidas;
	public RecomendacaoBean() {	}

	public void iniciar()
	{
		try
		{
			SemaforoMusicasRecomendadas.getSemaforo().acquire();
			
			try
			{
				if(UtilidadesTelas.verificarSessao())
				{
					List<MediaUsuarioGenero> listaMUG = null;
					listaMUG = mediaUsuarioGeneroDAO.pesquisaGenerosUsuario(getUsuarioGlobal());
					
					MusicasRecomendadasIM listaIM1 = null;
					MusicasRecomendadasIM listaIM2 = null;
					MusicasRecomendadasIM listaIM3 = null;
					List<Musica> listaIMAux;
					MusicaIM mIM;
					
					List<Musica> listaMusicasUsuarioGenero;
					RetornoKMeans retornoKMeans;
					int posicaoKMeans;
					List<MediaUsuarioGenero> listaUsuariosKMeans;
					List<AvaliarMusica> listaAMMusicasUsuarios;
					boolean existeHash;
					boolean existeMusica;
					boolean existeMusicaUsuario;
					
					if(listaMUG!=null && listaMUG.size()>0)
					{
						if(listaMUG.size()>3)
						{
							//Como possui o n�mero de g�neros para serem recomendados ent�o n�o precisar� de mandar mensagem incentivamento o usu�rio
							//a curtir mais m�sicas
							
							
							 //* GERAR R�NDOMICAMENTE PARA PEGAR G�NEROS DIFERENTES, o terceiro g�nero fazer um n�mero r�ndomico?
/*							Random random = new Random();
							int x = ran.nextInt(101);*/
							
							List<MediaUsuarioGenero> listaAllByGenero;
							for (int i = 0; i < listaMUG.size(); i++)
							{
								listaMusicasUsuarioGenero = new ArrayList<Musica>();
								listaIMAux = new ArrayList<Musica>();
								
								//Pesquisa todas as m�sicas do usu�rio para n�o recomendar m�sicas que ele j� tenha curtido.
								listaMusicasUsuarioGenero = avaliarMusicaDAO.pesquisaAlMusicasUsuarioGenero( getUsuarioGlobal(), listaMUG.get(i).getGenero());
								
								//Dado 3 g�nero que est�o entre os que possuem maior n�mero de m�sicas curtidas, pesquisa outros usu�rios que tamb�m curtiram este g�nero
								listaAllByGenero = new ArrayList<MediaUsuarioGenero>();
								listaAllByGenero = mediaUsuarioGeneroDAO.pesquisaAllByGenero(listaMUG.get(i).getGenero(), getUsuarioGlobal());
								 /*
								  *  Roda o KNN/K-Means, procurando os usu�rios mais pr�ximos do g�nero corrente
								  */
								if(listaAllByGenero.size()>2)
								{
									listaAllByGenero.add(listaMUG.get(i));
									retornoKMeans = new RetornoKMeans();
									retornoKMeans = KMeans.rodaAlgoritmo(listaAllByGenero);
									posicaoKMeans = 0;
									
									for (int x = 0; x < KMeans.Blocos; x++)
									{
										for (int y = 0; y < retornoKMeans.getTamanho(); y++)
										{
											if(retornoKMeans.getDivisaoBlocos()[x][y]==listaMUG.get(i).getMedia())
											{
												posicaoKMeans = x;
												break;
											}
										}
									}
									
									listaUsuariosKMeans = new ArrayList<MediaUsuarioGenero>();
									listaAllByGenero.remove(listaAllByGenero.size()-1);
									
									for (int x = 0; x < retornoKMeans.getTamanho(); x++)
									{
										for (MediaUsuarioGenero mediaUsuarioGenero : listaAllByGenero)
										{
											if(retornoKMeans.getDivisaoBlocos()[posicaoKMeans][x]==mediaUsuarioGenero.getMedia())
											{
												listaUsuariosKMeans.add(mediaUsuarioGenero);
												retornoKMeans.getDivisaoBlocos()[posicaoKMeans][x]=-9999;
											}
										}
									}
									
									existeHash = false;
									
									if(listaUsuariosKMeans!=null && listaUsuariosKMeans.size()>0)
									{
										for (int j = 0; j < listaUsuariosKMeans.size(); j++)
										{
											//Lista que cont�m as m�sicas que ser�o recomendadas do usu�rio em quest�o.
											listaAMMusicasUsuarios = new ArrayList<AvaliarMusica>();
											listaAMMusicasUsuarios = avaliarMusicaDAO.pesquisaAvaliacaoUsuarioMaior3(listaUsuariosKMeans.get(j).getUsuario(), listaUsuariosKMeans.get(j).getGenero());
											
											//Percorre a lista de M�sicas encontrada do usu�rio em quest�o. 
											if(listaAMMusicasUsuarios!=null && listaAMMusicasUsuarios.size()>0)
											{
												for (AvaliarMusica avaliarMusica : listaAMMusicasUsuarios)
												{
													//Caso o HashMap esteja vazio, insere o g�nero nele, adiciona a m�sica na lista de m�sicas daquele g�nero e adiciona a m�sica na lista auxiliar.
													if(GuardaMusicasRecomendadas.getTokensExisteMusica().isEmpty())
													{
														//Verifica se a lista de m�sicas que o usu�rio curtiu esta cheia, para fazer as verifica��es e evitar recomendar m�sicas que o usu�rio j� curtiu
														if(listaMusicasUsuarioGenero!=null && listaMusicasUsuarioGenero.size()>0)
														{
															existeMusicaUsuario = false;
															for (Musica musica : listaMusicasUsuarioGenero)
															{
																if(musica.getIdMUsica()==avaliarMusica.getMusica().getIdMUsica())
																{
																	existeMusicaUsuario = true;
																	break;
																}
															}
															
															if(!existeMusicaUsuario)
															{
																//Insere a m�sica na lista.
																GuardaMusicasRecomendadas.getTokensExisteMusica().put(listaUsuariosKMeans.get(j).getGenero().getPkGenero(), new ArrayList<Long>());
																GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
																listaIMAux.add(avaliarMusica.getMusica());
															}
														}
														else
														{
															//Insere a m�sica na lista.
															GuardaMusicasRecomendadas.getTokensExisteMusica().put(listaUsuariosKMeans.get(j).getGenero().getPkGenero(), new ArrayList<Long>());
															GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
															listaIMAux.add(avaliarMusica.getMusica());
														}
														
														//Caso a lista esteja cheia, sai do for.
														if(listaIMAux.size()>6)
														{
															break;
														}
													}
													else
													{
														if(GuardaMusicasRecomendadas.getTokensExisteMusica().containsKey(listaUsuariosKMeans.get(j).getGenero().getPkGenero()))
														{
															//Caso o Hash principal possua o g�nero em quest�o, ent�o procura nas suas listas de m�sicas, se existe a m�sica corrente, se existir seta existeHash como true (Flag para indicar que possuem m�sicas no hash que j� foram recomendas e caso seja necess�rio, verificar se ele precisa ser limpado).
															//Caso n�o exista, seta ela na lista auxliar.
															existeMusica = false;
															for (Long l : GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()))
															{
																if(l==avaliarMusica.getMusica().getPkMusica())
																{
																	existeHash = true;
																	existeMusica = true;
																	break;
																}
															}
															
															//Caso a m�sica n�o exista no Hash principal
															if(!existeMusica)
															{
																//Verifica se a lista de m�sicas que o usu�rio curtiu esta cheia, para fazer as verifica��es e evitar recomendar m�sicas que o usu�rio j� curtiu
																if(listaMusicasUsuarioGenero!=null && listaMusicasUsuarioGenero.size()>0)
																{
																	existeMusicaUsuario = false;
																	for (Musica musica : listaMusicasUsuarioGenero)
																	{
																		if(musica.getIdMUsica()==avaliarMusica.getMusica().getIdMUsica())
																		{
																			existeMusicaUsuario = true;
																			break;
																		}
																	}
																	
																	if(!existeMusicaUsuario)
																	{
																		//Insere a m�sica na lista.
																		GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
																		listaIMAux.add(avaliarMusica.getMusica());
																	}
																}
																else
																{
																	//Insere a m�sica na lista.
																	GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
																	listaIMAux.add(avaliarMusica.getMusica());
																}
															}
															
															//Caso a lista esteja cheia, sai do for.
															if(listaIMAux.size()>6)
															{
																break;
															}
														}
														else
														{
															//Verifica se a lista de m�sicas que o usu�rio curtiu esta cheia, para fazer as verifica��es e evitar recomendar m�sicas que o usu�rio j� curtiu
															if(listaMusicasUsuarioGenero!=null && listaMusicasUsuarioGenero.size()>0)
															{
																existeMusicaUsuario = false;
																for (Musica musica : listaMusicasUsuarioGenero)
																{
																	if(musica.getIdMUsica()==avaliarMusica.getMusica().getIdMUsica())
																	{
																		existeMusicaUsuario = true;
																		break;
																	}
																}
																
																if(!existeMusicaUsuario)
																{
																	//Caso n�o exista o g�nero no hash principal, insere o g�nero nele, adiciona a m�sica na lista de m�sicas daquele g�nero e adiciona a m�sica na lista auxiliar.
																	GuardaMusicasRecomendadas.getTokensExisteMusica().put(listaUsuariosKMeans.get(j).getGenero().getPkGenero(), new ArrayList<Long>());
																	GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
																	listaIMAux.add(avaliarMusica.getMusica());
																}
															}
															else
															{
																//Caso n�o exista o g�nero no hash principal, insere o g�nero nele, adiciona a m�sica na lista de m�sicas daquele g�nero e adiciona a m�sica na lista auxiliar.
																GuardaMusicasRecomendadas.getTokensExisteMusica().put(listaUsuariosKMeans.get(j).getGenero().getPkGenero(), new ArrayList<Long>());
																GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
																listaIMAux.add(avaliarMusica.getMusica());
															}
															
															//Caso a lista esteja cheia, sai do for.
															if(listaIMAux.size()>6)
															{
																break;
															}
														}
													}
												}
											}
											
											//Caso a lista esteja cheia, sai do for.
											if(listaIMAux.size()>6)
											{
												break;
											}
										}
									}
									
									//Caso o hash de m�sicas recomendadas esteja cheio, e por isso n�o tenha completado a lista de m�sicas, ent�o limpa ele e volta para o mesmo g�nero
									if(existeHash && listaIMAux.size()<6)
									{
										GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaMUG.get(i).getGenero().getPkGenero()).clear();
										i--;
									}
									else
									{
										//Caso a lista auxiliar contenha m�sicas, adiciona ela em algum hash de m�sicas para recomenda��o
										if(listaIMAux.size()>0)
										{
											if(listaIM1==null)
											{
												listaIM1 = new MusicasRecomendadasIM();
												listaIM1.setGenero(listaMUG.get(i).getGenero());
												listaIM1.setListaMusica(new ArrayList<Musica>());
												listaIM1.getListaMusica().addAll(listaIMAux);
											}
											else if(listaIM2==null)
											{
												listaIM2 = new MusicasRecomendadasIM();
												listaIM2.setGenero(listaMUG.get(i).getGenero());
												listaIM2.setListaMusica(new ArrayList<Musica>());
												listaIM2.getListaMusica().addAll(listaIMAux);
											}
											else
											{
												listaIM3 = new MusicasRecomendadasIM();
												listaIM3.setGenero(listaMUG.get(i).getGenero());
												listaIM3.setListaMusica(new ArrayList<Musica>());
												listaIM3.getListaMusica().addAll(listaIMAux);
												//Caso todas os hash's tenham sido preenchidos, ent�o sai do loop
												break;
											}
										}
									}
								}
								else if(listaAllByGenero.size()==1 || listaAllByGenero.size()==2)
								{
									existeHash = false;
									for (MediaUsuarioGenero mediaUsuarioGenero : listaAllByGenero)
									{
										//Lista que cont�m as m�sicas que ser�o recomendadas do usu�rio em quest�o.
										listaAMMusicasUsuarios = new ArrayList<AvaliarMusica>();
										listaAMMusicasUsuarios = avaliarMusicaDAO.pesquisaAvaliacaoUsuarioMaior3(mediaUsuarioGenero.getUsuario(), mediaUsuarioGenero.getGenero());
										
										//Percorre a lista de M�sicas encontrada do usu�rio em quest�o. 
										if(listaAMMusicasUsuarios!=null && listaAMMusicasUsuarios.size()>0)
										{
											for (AvaliarMusica avaliarMusica : listaAMMusicasUsuarios)
											{
												//Caso o HashMap esteja vazio, insere o g�nero nele, adiciona a m�sica na lista de m�sicas daquele g�nero e adiciona a m�sica na lista auxiliar.
												if(GuardaMusicasRecomendadas.getTokensExisteMusica().isEmpty())
												{
													//Verifica se a lista de m�sicas que o usu�rio curtiu esta cheia, para fazer as verifica��es e evitar recomendar m�sicas que o usu�rio j� curtiu
													if(listaMusicasUsuarioGenero!=null && listaMusicasUsuarioGenero.size()>0)
													{
														existeMusicaUsuario = false;
														for (Musica musica : listaMusicasUsuarioGenero)
														{
															if(musica.getIdMUsica()==avaliarMusica.getMusica().getIdMUsica())
															{
																existeMusicaUsuario = true;
																break;
															}
														}
														
														if(!existeMusicaUsuario)
														{
															//Caso n�o exista o g�nero no hash principal, insere o g�nero nele, adiciona a m�sica na lista de m�sicas daquele g�nero e adiciona a m�sica na lista auxiliar.
															GuardaMusicasRecomendadas.getTokensExisteMusica().put(mediaUsuarioGenero.getGenero().getPkGenero(), new ArrayList<Long>());
															GuardaMusicasRecomendadas.getTokensExisteMusica().get(mediaUsuarioGenero.getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
															listaIMAux.add(avaliarMusica.getMusica());
														}
													}
													else
													{
														//Caso n�o exista o g�nero no hash principal, insere o g�nero nele, adiciona a m�sica na lista de m�sicas daquele g�nero e adiciona a m�sica na lista auxiliar.
														GuardaMusicasRecomendadas.getTokensExisteMusica().put(mediaUsuarioGenero.getGenero().getPkGenero(), new ArrayList<Long>());
														GuardaMusicasRecomendadas.getTokensExisteMusica().get(mediaUsuarioGenero.getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
														listaIMAux.add(avaliarMusica.getMusica());
													}
													
													//Caso a lista esteja cheia, sai do for.
													if(listaIMAux.size()>6)
													{
														break;
													}
												}
												else
												{
													if(GuardaMusicasRecomendadas.getTokensExisteMusica().containsKey(mediaUsuarioGenero.getGenero().getPkGenero()))
													{
														//Caso o Hash principal possua o g�nero em quest�o, ent�o procura nas suas listas de m�sicas, se existe a m�sica corrente, se existir seta existeHash como true (Flag para indicar que possuem m�sicas no hash que j� foram recomendas e caso seja necess�rio, verificar se ele precisa ser limpado).
														//Caso n�o exista, seta ela na lista auxliar.
														existeMusica = false;
														for (Long l : GuardaMusicasRecomendadas.getTokensExisteMusica().get(mediaUsuarioGenero.getGenero().getPkGenero()))
														{
															if(l==avaliarMusica.getMusica().getPkMusica())
															{
																existeHash = true;
																existeMusica = true;
																break;
															}
														}
														
														if(!existeMusica)
														{
															//Verifica se a lista de m�sicas que o usu�rio curtiu esta cheia, para fazer as verifica��es e evitar recomendar m�sicas que o usu�rio j� curtiu
															if(listaMusicasUsuarioGenero!=null && listaMusicasUsuarioGenero.size()>0)
															{
																existeMusicaUsuario = false;
																for (Musica musica : listaMusicasUsuarioGenero)
																{
																	if(musica.getIdMUsica()==avaliarMusica.getMusica().getIdMUsica())
																	{
																		existeMusicaUsuario = true;
																		break;
																	}
																}
																
																if(!existeMusicaUsuario)
																{
																	//Insere a m�sica na lista
																	GuardaMusicasRecomendadas.getTokensExisteMusica().get(mediaUsuarioGenero.getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
																	listaIMAux.add(avaliarMusica.getMusica());
																}
															}
															else
															{
																//Insere a m�sica na lista
																GuardaMusicasRecomendadas.getTokensExisteMusica().get(mediaUsuarioGenero.getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
																listaIMAux.add(avaliarMusica.getMusica());
															}
														}
														
														//Caso a lista esteja cheia, sai do for.
														if(listaIMAux.size()>6)
														{
															break;
														}
													}
													else
													{
														//Verifica se a lista de m�sicas que o usu�rio curtiu esta cheia, para fazer as verifica��es e evitar recomendar m�sicas que o usu�rio j� curtiu
														if(listaMusicasUsuarioGenero!=null && listaMusicasUsuarioGenero.size()>0)
														{
															existeMusicaUsuario = false;
															for (Musica musica : listaMusicasUsuarioGenero)
															{
																if(musica.getIdMUsica()==avaliarMusica.getMusica().getIdMUsica())
																{
																	existeMusicaUsuario = true;
																	break;
																}
															}
															
															if(!existeMusicaUsuario)
															{
																//Caso n�o exista o g�nero no hash principal, insere o g�nero nele, adiciona a m�sica na lista de m�sicas daquele g�nero e adiciona a m�sica na lista auxiliar.
																GuardaMusicasRecomendadas.getTokensExisteMusica().put(mediaUsuarioGenero.getGenero().getPkGenero(), new ArrayList<Long>());
																GuardaMusicasRecomendadas.getTokensExisteMusica().get(mediaUsuarioGenero.getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
																listaIMAux.add(avaliarMusica.getMusica());
															}
														}
														else
														{
															//Caso n�o exista o g�nero no hash principal, insere o g�nero nele, adiciona a m�sica na lista de m�sicas daquele g�nero e adiciona a m�sica na lista auxiliar.
															GuardaMusicasRecomendadas.getTokensExisteMusica().put(mediaUsuarioGenero.getGenero().getPkGenero(), new ArrayList<Long>());
															GuardaMusicasRecomendadas.getTokensExisteMusica().get(mediaUsuarioGenero.getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
															listaIMAux.add(avaliarMusica.getMusica());
														}
														
														//Caso a lista esteja cheia, sai do for.
														if(listaIMAux.size()>6)
														{
															break;
														}
													}
												}
											}
										}
									}
									
									//Caso o hash de m�sicas recomendadas esteja cheio, e por isso n�o tenha completado a lista de m�sicas, ent�o limpa ele e volta para o mesmo g�nero
									if(existeHash && listaIMAux.size()<6)
									{
										GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaMUG.get(i).getGenero().getPkGenero()).clear();
										i--;
									}
									else
									{
										//Caso a lista auxiliar contenha m�sicas, adiciona ela em algum hash de m�sicas para recomenda��o
										if(listaIMAux.size()>0)
										{
											if(listaIM1==null)
											{
												listaIM1 = new MusicasRecomendadasIM();
												listaIM1.setGenero(listaMUG.get(i).getGenero());
												listaIM1.setListaMusica(new ArrayList<Musica>());
												listaIM1.getListaMusica().addAll(listaIMAux);
											}
											else if(listaIM2==null)
											{
												listaIM2 = new MusicasRecomendadasIM();
												listaIM2.setGenero(listaMUG.get(i).getGenero());
												listaIM2.setListaMusica(new ArrayList<Musica>());
												listaIM2.getListaMusica().addAll(listaIMAux);
											}
											else
											{
												listaIM3 = new MusicasRecomendadasIM();
												listaIM3.setGenero(listaMUG.get(i).getGenero());
												listaIM3.setListaMusica(new ArrayList<Musica>());
												listaIM3.getListaMusica().addAll(listaIMAux);
												//Caso todas os hash's tenham sido preenchidos, ent�o sai do loop
												break;
											}
										}
									}
								}
							}
						}
						else
						{
							//N�o existe n�mero suficientes de g�neros a serem recomendados, ent�o ser�o recomendados as m�sicas mais avaliadas do site para
							//completar a recomenda��o
							
							
							//Para completar manda uma mensagem incentivando o usu�rio a curtir mais m�sicas no sistemas
							 this.mensagemIncentivamentoCurtidas = "Para melhorar as recomenda��es navegue pelo site avaliando m�sicas!";
		    				 addMessage("Este email j� est� cadastrado em outro usu�rio", FacesMessage.SEVERITY_INFO);
						}
					}
					else if(listaMUG==null || listaMUG.size()==0)
					{
						//Caso n�o exista significa que o usu�rio n�o curtiu nenhuma m�sica ent�o recomenda as m�sicas mais avaliadas do site
						//Para completar manda uma mensagem incentivando o usu�rio a curtir mais m�sicas no sistemas
						this.mensagemIncentivamentoCurtidas = "Para melhorar as recomenda��es navegue pelo site avaliando m�sicas!";
	   				 	addMessage("Este email j� est� cadastrado em outro usu�rio", FacesMessage.SEVERITY_INFO);
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
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			SemaforoMusicasRecomendadas.getSemaforo().release();
		}
	}

	public String getMensagemIncentivamentoCurtidas() {
		return mensagemIncentivamentoCurtidas;
	}

	public void setMensagemIncentivamentoCurtidas(
			String mensagemIncentivamentoCurtidas) {
		this.mensagemIncentivamentoCurtidas = mensagemIncentivamentoCurtidas;
	}
}