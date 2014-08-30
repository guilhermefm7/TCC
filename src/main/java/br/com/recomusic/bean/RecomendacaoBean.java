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
					List<MediaUsuarioGenero> listaMUG = mediaUsuarioGeneroDAO.pesquisaGenerosUsuario(getUsuarioGlobal());
					
					MusicasRecomendadasIM listaIM1 = null;
					MusicasRecomendadasIM listaIM2 = null;
					MusicasRecomendadasIM listaIM3 = null;
					List<Musica> listaIMAux;
					MusicaIM mIM;
					
					RetornoKMeans retornoKMeans;
					int posicaoKMeans;
					List<MediaUsuarioGenero> listaUsuariosKMeans;
					List<AvaliarMusica> listaAMMusicasUsuarios;
					boolean existeHash;
					boolean existeMusica;
					
					if(listaMUG!=null && listaMUG.size()>0)
					{
						if(listaMUG.size()>3)
						{
							//Como possui o número de gêneros para serem recomendados então não precisará de mandar mensagem incentivamento o usuário
							//a curtir mais músicas
							
							
							 //* GERAR RÂNDOMICAMENTE PARA PEGAR GÊNEROS DIFERENTES, o terceiro gênero fazer um número rândomico?
/*							Random random = new Random();
							int x = ran.nextInt(101);*/
							
							List<MediaUsuarioGenero> listaAllByGenero;
							for (int i = 0; i < listaMUG.size(); i++)
							{
								listaIMAux = new ArrayList<Musica>();
								//Dado 3 gênero que estão entre os que possuem maior número de músicas curtidas, pesquisa outros usuários que também curtiram este gênero
								listaAllByGenero = new ArrayList<MediaUsuarioGenero>();
								listaAllByGenero = mediaUsuarioGeneroDAO.pesquisaAllByGenero(listaMUG.get(i).getGenero(), getUsuarioGlobal());
								listaAllByGenero.add(listaMUG.get(i));
								 /*
								  *  Roda o KNN/K-Means, procurando os usuários mais próximos do gênero corrente
								  */
								if(listaAllByGenero.size()>3)
								{
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
											//Lista que contém as músicas que serão recomendadas do usuário em questão.
											listaAMMusicasUsuarios = new ArrayList<AvaliarMusica>();
											listaAMMusicasUsuarios = avaliarMusicaDAO.pesquisaAvaliacaoUsuarioMaior3(listaUsuariosKMeans.get(j).getUsuario(), listaUsuariosKMeans.get(j).getGenero());
											
											//Percorre a lista de Músicas encontrada do usuário em questão. 
											if(listaAMMusicasUsuarios!=null && listaAMMusicasUsuarios.size()>0)
											{
												for (AvaliarMusica avaliarMusica : listaAMMusicasUsuarios)
												{
													//Caso o HashMap esteja vazio, insere o gênero nele, adiciona a música na lista de músicas daquele gênero e adiciona a música na lista auxiliar.
													if(GuardaMusicasRecomendadas.getTokensExisteMusica().isEmpty())
													{
														GuardaMusicasRecomendadas.getTokensExisteMusica().put(listaUsuariosKMeans.get(j).getGenero().getPkGenero(), new ArrayList<Long>());
														GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
														
														listaIMAux.add(avaliarMusica.getMusica());
														
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
															//Caso o Hash principal possua o gênero em questão, então procura nas suas listas de músicas, se existe a música corrente, se existir seta existeHash como true (Flag para indicar que possuem músicas no hash que já foram recomendas e caso seja necessário, verificar se ele precisa ser limpado).
															//Caso não exista, seta ela na lista auxliar.
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
															
															//Insere a música na lista.
															if(!existeMusica)
															{
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
															//Caso não exista o gênero no hash principal, insere o gênero nele, adiciona a música na lista de músicas daquele gênero e adiciona a música na lista auxiliar.
															GuardaMusicasRecomendadas.getTokensExisteMusica().put(listaUsuariosKMeans.get(j).getGenero().getPkGenero(), new ArrayList<Long>());
															GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaUsuariosKMeans.get(j).getGenero().getPkGenero()).add(avaliarMusica.getMusica().getPkMusica());
															listaIMAux.add(avaliarMusica.getMusica());
															
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
									
									//Caso o hash de músicas recomendadas esteja cheio, e por isso não tenha completado a lista de músicas, então limpa ele e volta para o mesmo gênero
									if(listaIMAux.size()==0 && existeHash || listaIMAux.size()<6 && listaUsuariosKMeans!=null && listaUsuariosKMeans.size()>0)
									{
										GuardaMusicasRecomendadas.getTokensExisteMusica().get(listaMUG.get(i).getGenero().getPkGenero()).clear();
										i--;
									}
									else
									{
										//Caso a lista auxiliar contenha músicas, adiciona ela em algum hash de músicas para recomendação
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
												//Caso todas os hash's tenham sido preenchidos, então sai do loop
												break;
											}
										}
									}
								}
								else if(listaAllByGenero.size()==2 || listaAllByGenero.size()==3)
								{
									
								}
							}
						}
						else
						{
							//Não existe número suficientes de gêneros a serem recomendados, então serão recomendados as músicas mais avaliadas do site para
							//completar a recomendação
							
							//Para completar manda uma mensagem incentivando o usuário a curtir mais músicas no sistemas
							 this.mensagemIncentivamentoCurtidas = "Para melhorar as recomendações navegue pelo site avaliando músicas!";
		    				 addMessage("Este email já está cadastrado em outro usuário", FacesMessage.SEVERITY_INFO);
						}
					}
					else
					{
						//Caso não exista significa que o usuário não curtiu nenhuma música então recomenda as músicas mais avaliadas do site
						//Para completar manda uma mensagem incentivando o usuário a curtir mais músicas no sistemas
						this.mensagemIncentivamentoCurtidas = "Para melhorar as recomendações navegue pelo site avaliando músicas!";
	   				 	addMessage("Este email já está cadastrado em outro usuário", FacesMessage.SEVERITY_INFO);
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