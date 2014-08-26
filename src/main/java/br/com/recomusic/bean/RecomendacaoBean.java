package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.MediaUsuarioGeneroDAO;
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
	private String mensagemIncentivamentoCurtidas;
	public RecomendacaoBean() {	}

	public void iniciar()
	{
		try
		{
		SemaforoMusicasRecomendadas.getSemaforo().acquire();
		try
		{
			System.out.println("Entrei1");
/*			if(UtilidadesTelas.verificarSessao())
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
						//Como possui o número de gêneros para serem recomendados então não precisará de mandar mensagem incentivamento o usuário
						//a curtir mais músicas
						
						
						 * GERAR RÂNDOMICAMENTE PARA PEGAR GÊNEROS DIFERENTES
						 
						
						List<MediaUsuarioGenero> listaAllByGenero;
						for (int i = 0; i < 3; i++)
						{
							listaAllByGenero = new ArrayList<MediaUsuarioGenero>();
							listaAllByGenero = mediaUsuarioGeneroDAO.pesquisaAllByGenero(listaAux.get(i).getGenero(), getUsuarioGlobal());
							
							
							
							 * Roda o KNN/K-Means, procurando os usuários mais próximos do gênero corrente
							 
							for (int j = 0; j < 3; j++)
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
			}*/
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