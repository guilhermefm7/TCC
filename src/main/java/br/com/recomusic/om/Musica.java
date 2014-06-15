package br.com.recomusic.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Musica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkMusica;
	private String titulo;
	private String ano;
	private String album;
	private String idMUsica;

	@ManyToOne @JoinColumn(name="fkPais")
	private Pais pais;

	@ManyToOne @JoinColumn(name="fkBanda")
	private Banda banda;

	@OneToMany(mappedBy="musica")
	private List<MusicaGenero> musicaGeneros;

	@OneToMany(mappedBy="musica")
	private List<InformacaoMusicalCadastroMusica> informacaoMusicalCadastroMusicas;

	@OneToMany(mappedBy="musica")
	private List<PlaylistMusica> playlistMusicas;
	
	@OneToMany(mappedBy="musica")
	private List<AvaliarMusica> avaliarMusica;

	/*-*-*-* Construtores *-*-*-*/
	public Musica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkMusica() { return pkMusica; }
	public void setPkMusica(long pkMusica) { this.pkMusica = pkMusica; }

	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }

	public String getAno() { return ano; }
	public void setAno(String ano) { this.ano = ano; }

	public String getAlbum() { return album; }
	public void setAlbum(String album) { this.album = album; }

	public String getIdMUsica() { return idMUsica; }
	public void setIdMUsica(String idMUsica) { this.idMUsica = idMUsica; }

	public Pais getPais() { return pais; }
	public void setPais(Pais pais) { this.pais = pais; }

	public Banda getBanda() { return banda; }
	public void setBanda(Banda banda) { this.banda = banda; }

	public List<MusicaGenero> getMusicaGeneros() { if(musicaGeneros==null) { musicaGeneros = new ArrayList<MusicaGenero>(); } return musicaGeneros; }
	public void setMusicaGeneros(List<MusicaGenero> musicaGeneros) { this.musicaGeneros = musicaGeneros; }

	public List<InformacaoMusicalCadastroMusica> getInformacaoMusicalCadastroMusicas() { if(informacaoMusicalCadastroMusicas==null) { informacaoMusicalCadastroMusicas = new ArrayList<InformacaoMusicalCadastroMusica>(); } return informacaoMusicalCadastroMusicas; }
	public void setInformacaoMusicalCadastroMusicas(List<InformacaoMusicalCadastroMusica> informacaoMusicalCadastroMusicas) { this.informacaoMusicalCadastroMusicas = informacaoMusicalCadastroMusicas; }

	public List<PlaylistMusica> getPlaylistMusicas() { if(playlistMusicas==null) { playlistMusicas = new ArrayList<PlaylistMusica>(); } return playlistMusicas; }
	public void setPlaylistMusicas(List<PlaylistMusica> playlistMusicas) { this.playlistMusicas = playlistMusicas; }

	public List<AvaliarMusica> getAvaliarMusica() { return avaliarMusica; }
	public void setAvaliarMusica(List<AvaliarMusica> avaliarMusica) { this.avaliarMusica = avaliarMusica; }
}