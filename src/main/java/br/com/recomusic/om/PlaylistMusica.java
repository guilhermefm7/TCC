package br.com.mresolucoes.atta.modulos.frota.persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import utils.data.Data;
import br.com.mresolucoes.atta.modulos.atta.utils.BDConstantesAtta;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

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
	private Data lancamento;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;

	@ManyToOne @JoinColumn(name="fkPlaylist")
	private Playlist playlist;


	/*-*-*-* Construtores *-*-*-*/
	public PlaylistMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkPlaylistMusica() { return pkPlaylistMusica; }
	public void setPkPlaylistMusica(long pkPlaylistMusica) { this.pkPlaylistMusica = pkPlaylistMusica; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }

	public Playlist getPlaylist() { return playlist; }
	public void setPlaylist(Playlist playlist) { this.playlist = playlist; }
}