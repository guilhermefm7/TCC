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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class Playlist implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkPlaylist;
	private String nome;
	@Type(type="timestamp")
	private Data lancamento;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@OneToMany(mappedBy="playlist")
	private List<PlaylistMusica> playlistMusicas;


	/*-*-*-* Construtores *-*-*-*/
	public Playlist() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkPlaylist() { return pkPlaylist; }
	public void setPkPlaylist(long pkPlaylist) { this.pkPlaylist = pkPlaylist; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public List<PlaylistMusica> getPlaylistMusicas() { if(playlistMusicas==null) { playlistMusicas = new ArrayList<PlaylistMusica>(); } return playlistMusicas; }
	public void setPlaylistMusicas(List<PlaylistMusica> playlistMusicas) { this.playlistMusicas = playlistMusicas; }
}