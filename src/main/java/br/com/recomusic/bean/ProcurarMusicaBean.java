package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.im.MusicaIM;
import br.com.recomusic.persistencia.utils.PesquisaMusica;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="ProcurarMusicaBean")
@ViewScoped
public class ProcurarMusicaBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	//private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private String email;
	private String tokenRecebido = null;
	private List<String> listaNomesMusica = null;
	private List<MusicaIM> listaMusicas  = null;
	

	public ProcurarMusicaBean() {	}

	public void iniciar()
	{
		try
		{
			if((tokenRecebido!=null && tokenRecebido.length()>0))
			{
				listaMusicas = PesquisaMusica.requisitarMusicaJson(tokenRecebido);
				
/*				for (MusicaIM musicaIM : listaMusicas)
				{
					listaNomesMusica.add(musicaIM.getNomeMusica());
				}*/
			}
			else
			{
				redirecionarErro();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void teste(String n)
	{
		try
		{
			System.out.println("dsdsds " + n);
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/musica/index.xhtml?t="+ n);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	 
	public List<MusicaIM> getListaMusicas() {
		return listaMusicas;
	}

	public void setListaMusicas(List<MusicaIM> listaMusicas) {
		this.listaMusicas = listaMusicas;
	}

	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getTokenRecebido() {
		return tokenRecebido;
	}

	public void setTokenRecebido(String tokenRecebido) {
		this.tokenRecebido = tokenRecebido;
	}

	public List<String> getListaNomesMusica() {
		return listaNomesMusica;
	}

	public void setListaNomesMusica(List<String> listaNomesMusica) {
		this.listaNomesMusica = listaNomesMusica;
	}
}