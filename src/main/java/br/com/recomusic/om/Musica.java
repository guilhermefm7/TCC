package br.com.mresolucoes.atta.modulos.frota.persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

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
	private List<AvaliarMusicaMusica> avaliarMusicaMusicas;

	@OneToMany(mappedBy="musica")
	private List<RecomendaMusica> recomendaMusicas;

	@OneToMany(mappedBy="musica")
	private List<CompartilhaMusica> compartilhaMusicas;

	@OneToMany(mappedBy="musica")
	private List<EspectroMusicalAnexo> espectroMusicalAnexos;


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

	public List<AvaliarMusicaMusica> getAvaliarMusicaMusicas() { if(avaliarMusicaMusicas==null) { avaliarMusicaMusicas = new ArrayList<AvaliarMusicaMusica>(); } return avaliarMusicaMusicas; }
	public void setAvaliarMusicaMusicas(List<AvaliarMusicaMusica> avaliarMusicaMusicas) { this.avaliarMusicaMusicas = avaliarMusicaMusicas; }

	public List<RecomendaMusica> getRecomendaMusicas() { if(recomendaMusicas==null) { recomendaMusicas = new ArrayList<RecomendaMusica>(); } return recomendaMusicas; }
	public void setRecomendaMusicas(List<RecomendaMusica> recomendaMusicas) { this.recomendaMusicas = recomendaMusicas; }

	public List<CompartilhaMusica> getCompartilhaMusicas() { if(compartilhaMusicas==null) { compartilhaMusicas = new ArrayList<CompartilhaMusica>(); } return compartilhaMusicas; }
	public void setCompartilhaMusicas(List<CompartilhaMusica> compartilhaMusicas) { this.compartilhaMusicas = compartilhaMusicas; }

	public List<EspectroMusicalAnexo> getEspectroMusicalAnexos() { if(espectroMusicalAnexos==null) { espectroMusicalAnexos = new ArrayList<EspectroMusicalAnexo>(); } return espectroMusicalAnexos; }
	public void setEspectroMusicalAnexos(List<EspectroMusicalAnexo> espectroMusicalAnexos) { this.espectroMusicalAnexos = espectroMusicalAnexos; }
}