package br.com.recomusic.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="RecomendacaoBean")
@ViewScoped
public class RecomendacaoBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	public RecomendacaoBean() {	}

	public void iniciar()
	{
		try
		{
			if(UtilidadesTelas.verificarSessao())
			{
				
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
}