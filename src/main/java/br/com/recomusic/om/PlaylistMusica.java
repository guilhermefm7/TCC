package br.com.recomusic.om;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.recomusic.persistencia.utils.Constantes;

@Entity
public class PlaylistMusica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkPlaylistMusica;
	@Type(type="timestamp")
	private Date lancamento;
	private int status = Constantes.TIPO_STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;

	@ManyToOne @JoinColumn(name="fkPlaylist")
	private Playlist playlist;


	/*-*-*-* Construtores *-*-*-*/
	public PlaylistMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkPlaylistMusica() { return pkPlaylistMusica; }
	public void setPkPlaylistMusica(long pkPlaylistMusica) { this.pkPlaylistMusica = pkPlaylistMusica; }

	public Date getLancamento() { return lancamento; }
	public void setLancamento(Date lancamento) { this.lancamento = lancamento; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }

	public Playlist getPlaylist() { return playlist; }
	public void setPlaylist(Playlist playlist) { this.playlist = playlist; }
}