package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.MediaUsuarioGeneroDAO;
import br.com.recomusic.om.AvaliarMusica;
import br.com.recomusic.om.MediaUsuarioGenero;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;
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
					
					if(listaMUG!=null && listaMUG.size()>0)
					{
						List<MediaUsuarioGenero> listaAux = new ArrayList<MediaUsuarioGenero>();
						for (MediaUsuarioGenero mediaUsuarioGenero : listaMUG)
						{
							if(mediaUsuarioGenero.getMediaAvaliacoes()>3D)
							{
								listaAux.add(mediaUsuarioGenero);
							}
						}
						
						if(listaAux.size()>3)
						{
							//Como possui o n�mero de g�neros para serem recomendados ent�o n�o precisar� de mandar mensagem incentivamento o usu�rio
							//a curtir mais m�sicas
							
							
							 //* GERAR R�NDOMICAMENTE PARA PEGAR G�NEROS DIFERENTES
/*							Random random = new Random();
							int x = ran.nextInt(101);*/
							
							List<MediaUsuarioGenero> listaAllByGenero;
							for (int i = 0; i < 3; i++)
							{
								//Dado 3 g�nero que est�o entre os que possuem maior n�mero de m�sicas curtidas, pesquisa outros usu�rios que tamb�m curtiram este g�nero
								listaAllByGenero = new ArrayList<MediaUsuarioGenero>();
								listaAllByGenero = mediaUsuarioGeneroDAO.pesquisaAllByGenero(listaAux.get(i).getGenero(), getUsuarioGlobal());
								
								 /*
								  *  Roda o KNN/K-Means, procurando os usu�rios mais pr�ximos do g�nero corrente
								  */
								
								/**
								 * Faz um while at� dar o n�mero de m�sicas recomendadas
								 */
								
								//Pesquisa se o n�mero de usu�rios resultantes do algoritmo � maior que X, que � o n�mero de usu�rios necess�rios
								List<AvaliarMusica> listaAM;
								//Dado X usu�rios escolhidos pelo Algoritmo (K-Means/KNN) pega um n�mero X deles 
								for (int j = 0; j < listaAllByGenero.size(); j++)
								{
									listaAM = new ArrayList<AvaliarMusica>();
									//Passa um usu�rio, escolhido pelo algoritmo, para pegar as m�sicas dele do g�nero corrente
									listaAM = avaliarMusicaDAO.pesquisaAvaliacaoUsuarioMaior3(listaAllByGenero.get(j).getUsuario(), listaAux.get(i).getGenero());
									
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
					else
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