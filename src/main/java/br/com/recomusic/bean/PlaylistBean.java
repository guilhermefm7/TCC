package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.PlaylistDAO;
import br.com.recomusic.im.PlaylistIM;
import br.com.recomusic.om.Playlist;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="PlaylistBean")
@ViewScoped
public class PlaylistBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<String> listaNomesMusica = null;
	private List<PlaylistIM> listaPIM  = null;
	private PlaylistDAO playlistDAO = new PlaylistDAO( ConectaBanco.getInstance().getEntityManager());
	private int qtdPlaylists;

	public PlaylistBean() {	}

	public void iniciar()
	{
		try
		{
			if(UtilidadesTelas.verificarSessao())
			{
				setUsuarioGlobal(getUsuarioGlobal());
				
				List<Playlist> listaP = playlistDAO.getPlaylistsUsuario(getUsuarioGlobal());
				
				listaPIM = new ArrayList<PlaylistIM>();
				PlaylistIM pIM;
				int contador = 0;
				
				//Passa setando as Playlists na lista de PlaylistIM que será exibida na tabela da tela
				for (Playlist playlist : listaP)
				{
					pIM = new PlaylistIM();
					pIM.setNomePlaylist(playlist.getNome());
					pIM.setDataLancamento(playlist.getLancamento());
					pIM.setQtd(contador+1);
					pIM.setQtdFaixas(playlist.getNumeroMusicas());
					listaPIM.add(pIM);
					//Contador indica o numero de Playlists existentes
					contador++;
				}
				
				this.qtdPlaylists = contador;
				//listaMusicas = PesquisaMusica.requisitarMusicaJson(tokenRecebido);
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

	public void redirecionaPaginaMusica(String idMusica, String nomeMusica, String artistaBandaMusica, String album, String idEcho)
	{
		try
		{
			if(album!=null && album.length()>0)
			{
				FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/musica/index.xhtml?t="+ idMusica + "&m=" + nomeMusica + "&a=" + artistaBandaMusica + "&i=" + idEcho + "&n=" + album);
			}
			else
			{
				FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/musica/index.xhtml?t="+ idMusica + "&m=" + nomeMusica + "&a=" + artistaBandaMusica + "&i=" + idEcho);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public List<PlaylistIM> getListaPlaylistIM() {
		return listaPIM;
	}

	public void setListaPlaylistIM(List<PlaylistIM> listaPIM) {
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

	public PlaylistDAO getPlaylistDAO() {
		return playlistDAO;
	}

	public void setPlaylistDAO(PlaylistDAO playlistDAO) {
		this.playlistDAO = playlistDAO;
	}
}