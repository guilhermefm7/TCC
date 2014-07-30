package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.PlaylistDAO;
import br.com.recomusic.im.PlaylistIM;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="PlaylistSelecionadaBean")
@ViewScoped
public class PlaylistSelecionadaBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<String> listaNomesMusica = null;
	private String pkPlaylist = null;
	private List<PlaylistIM> listaPIM  = null;
	private PlaylistDAO playlistDAO = new PlaylistDAO( ConectaBanco.getInstance().getEntityManager());
	private int qtdPlaylists;

	public PlaylistSelecionadaBean() {	}

	public void iniciar()
	{
		try
		{
			if((pkPlaylist!=null && pkPlaylist.length()>0))
			{
				if(UtilidadesTelas.verificarSessao())
				{
					setUsuarioGlobal(getUsuarioGlobal());
					//
				}
				else
				{
					encerrarSessao();
				}
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

	public void redirecionaPaginaMPlaylist(String pkPlaylist)
	{
		try
		{
			if(pkPlaylist!=null && pkPlaylist.length()>0)
			{
				FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/playlistSelecionada/index.xhtml?t="+ pkPlaylist);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public List<PlaylistIM> getListaPIM() {
		return listaPIM;
	}

	public void setListaPIM(List<PlaylistIM> listaPIM) {
		this.listaPIM = listaPIM;
	}

	public List<String> getListaNomesMusica() {
		return listaNomesMusica;
	}

	public void setListaNomesMusica(List<String> listaNomesMusica) {
		this.listaNomesMusica = listaNomesMusica;
	}

	public int getQtdPlaylists() {
		return qtdPlaylists;
	}

	public void setQtdPlaylists(int qtdPlaylists) {
		this.qtdPlaylists = qtdPlaylists;
	}

	
	public String getPkPlaylist() {
		return pkPlaylist;
	}

	public void setPkPlaylist(String pkPlaylist) {
		this.pkPlaylist = pkPlaylist;
	}

	public PlaylistDAO getPlaylistDAO() {
		return playlistDAO;
	}

	public void setPlaylistDAO(PlaylistDAO playlistDAO) {
		this.playlistDAO = playlistDAO;
	}
}