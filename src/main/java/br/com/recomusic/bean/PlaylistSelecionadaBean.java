package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.PlaylistDAO;
import br.com.recomusic.dao.PlaylistMusicaDAO;
import br.com.recomusic.im.MusicaIM;
import br.com.recomusic.im.PlaylistIM;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.Playlist;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="PlaylistSelecionadaBean")
@ViewScoped
public class PlaylistSelecionadaBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<String> listaNomesMusica = null;
	private String pkPlaylist = null;
	private String nomePlaylist = null;
	private String playlistsTocadas = null;
	private List<PlaylistIM> listaPIM  = null;
	private PlaylistDAO playlistDAO = new PlaylistDAO( ConectaBanco.getInstance().getEntityManager());
	private PlaylistMusicaDAO playlistMusicaDAO = new PlaylistMusicaDAO( ConectaBanco.getInstance().getEntityManager());
	private int qtdPlaylists = 0;
	private List<MusicaIM> listaMusicas  = null;

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
					Playlist playlist = null;
					playlist = playlistDAO.getPlaylist(pkPlaylist);
					
					if(playlist!=null && playlist.getPkPlaylist()>0)
					{
						nomePlaylist = playlist.getNome();
						List<Musica> listaM = playlistMusicaDAO.getMusicasPlaylist(pkPlaylist);
						listaMusicas = new ArrayList<MusicaIM>();
						
						if(listaM!=null && listaM.size()>0)
						{
							MusicaIM mIM;
							int contador = 0;
							
							playlistsTocadas = "";
							
							for (int i = 0; i< listaM.size();i++)
							{
								mIM = new MusicaIM();
								mIM.setAlbumMusica(listaM.get(i).getAlbum());
								mIM.setIdDeezer(listaM.get(i).getIdDeezer());
								mIM.setIdMusica(listaM.get(i).getIdMusica());
								
								if(i==(listaM.size()-1))
								{
									playlistsTocadas = playlistsTocadas + listaM.get(i).getIdDeezer();
								}
								else
								{
									playlistsTocadas = playlistsTocadas + listaM.get(i).getIdDeezer() + ",";
								}
								
								if(listaM.get(i).getBanda()!=null && listaM.get(i).getBanda().getPkBanda()>0 && listaM.get(i).getBanda().getNome()!=null)
								{
									mIM.setNomeArtista(listaM.get(i).getBanda().getNome());
								}
								else
								{
									mIM.setNomeArtista("");
								}
								
								mIM.setNomeMusica(listaM.get(i).getTitulo());
								listaMusicas.add(mIM);
								contador++;
								mIM.setQtd(contador);
							}
							this.qtdPlaylists = contador;
						}
						else
						{
							playlistsTocadas = "";
							qtdPlaylists = 0;
						}
					}
					else
					{
						redirecionarErro();
					}
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
	
	public void excluir(String idDeezer)
	{
		try
		{
			if((pkPlaylist!=null && pkPlaylist.length()>0) && (idDeezer!=null && idDeezer.length()>0))
			{
				ConectaBanco.getInstance().beginTransaction();
				playlistMusicaDAO.removeMusicaPlaylist(pkPlaylist, idDeezer);
				ConectaBanco.getInstance().commit();
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

	public String getPlaylistsTocadas() {
		return playlistsTocadas;
	}

	public void setPlaylistsTocadas(String playlistsTocadas) {
		this.playlistsTocadas = playlistsTocadas;
	}

	public List<MusicaIM> getListaMusicas() {
		return listaMusicas;
	}

	public void setListaMusicas(List<MusicaIM> listaMusicas) {
		this.listaMusicas = listaMusicas;
	}

	public PlaylistMusicaDAO getPlaylistMusicaDAO() {
		return playlistMusicaDAO;
	}

	public void setPlaylistMusicaDAO(PlaylistMusicaDAO playlistMusicaDAO) {
		this.playlistMusicaDAO = playlistMusicaDAO;
	}

	public String getNomePlaylist() {
		return nomePlaylist;
	}

	public void setNomePlaylist(String nomePlaylist) {
		this.nomePlaylist = nomePlaylist;
	}
}