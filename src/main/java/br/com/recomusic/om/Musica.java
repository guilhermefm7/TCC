package br.com.recomusic.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Musica implements Serializable {
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pkMusica;
	private String titulo;
	private String ano;
	private String album;
	private String idMusica;
	private String idDeezer;
	private String urlImagem;
	private Double BPMMUsica;
	private Double EnergMusica;
	private Integer quantidadeAvaliacoes;
	private Double mediaAvaliacoes;

	@ManyToOne
	@JoinColumn(name = "fkPais")
	private Pais pais;

	@ManyToOne
	@JoinColumn(name = "fkBanda")
	private Banda banda;

    @XmlElement(nillable = true)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "musica", orphanRemoval = true)
    @Cascade(value = {
                    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE, CascadeType.DELETE, CascadeType.REMOVE,
                    CascadeType.DELETE_ORPHAN})
    @Fetch(FetchMode.SELECT)
	private List<MusicaGenero> musicaGeneros;

	@OneToMany(mappedBy = "musica")
	private List<InformacaoMusicalCadastroMusica> informacaoMusicalCadastroMusicas;

	@OneToMany(mappedBy = "musica")
	private List<PlaylistMusica> playlistMusicas;

	@OneToMany(mappedBy = "musica")
	private List<AvaliarMusica> avaliarMusica;

	/*-*-*-* Construtores *-*-*-*/
	public Musica() {
	}

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkMusica() {
		return pkMusica;
	}

	public void setPkMusica(long pkMusica) {
		this.pkMusica = pkMusica;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getIdMusica() {
		return idMusica;
	}

	public void setIdMusica(String idMusica) {
		this.idMusica = idMusica;
	}

	public String getIdDeezer() {
		return idDeezer;
	}

	public void setIdDeezer(String idDeezer) {
		this.idDeezer = idDeezer;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Banda getBanda() {
		return banda;
	}

	public void setBanda(Banda banda) {
		this.banda = banda;
	}

	public Double getBPMMUsica() {
		return BPMMUsica;
	}

	public void setBPMMUsica(Double bPMMUsica) {
		BPMMUsica = bPMMUsica;
	}

	public Double getEnergMusica() {
		return EnergMusica;
	}

	public void setEnergMusica(Double energMusica) {
		EnergMusica = energMusica;
	}

	public List<MusicaGenero> getMusicaGeneros() {
		if (musicaGeneros == null) {
			musicaGeneros = new ArrayList<MusicaGenero>();
		}
		return musicaGeneros;
	}

	public void setMusicaGeneros(List<MusicaGenero> musicaGeneros) {
		this.musicaGeneros = musicaGeneros;
	}

	public List<InformacaoMusicalCadastroMusica> getInformacaoMusicalCadastroMusicas() {
		if (informacaoMusicalCadastroMusicas == null) {
			informacaoMusicalCadastroMusicas = new ArrayList<InformacaoMusicalCadastroMusica>();
		}
		return informacaoMusicalCadastroMusicas;
	}

	public void setInformacaoMusicalCadastroMusicas(
			List<InformacaoMusicalCadastroMusica> informacaoMusicalCadastroMusicas) {
		this.informacaoMusicalCadastroMusicas = informacaoMusicalCadastroMusicas;
	}

	public List<PlaylistMusica> getPlaylistMusicas() {
		if (playlistMusicas == null) {
			playlistMusicas = new ArrayList<PlaylistMusica>();
		}
		return playlistMusicas;
	}

	public void setPlaylistMusicas(List<PlaylistMusica> playlistMusicas) {
		this.playlistMusicas = playlistMusicas;
	}

	public List<AvaliarMusica> getAvaliarMusica() {
		return avaliarMusica;
	}

	public void setAvaliarMusica(List<AvaliarMusica> avaliarMusica) {
		this.avaliarMusica = avaliarMusica;
	}

	public Integer getQuantidadeAvaliacoes() {
		return quantidadeAvaliacoes;
	}

	public void setQuantidadeAvaliacoes(Integer quantidadeAvaliacoes) {
		this.quantidadeAvaliacoes = quantidadeAvaliacoes;
	}

	public Double getMediaAvaliacoes() {
		return mediaAvaliacoes;
	}

	public void setMediaAvaliacoes(Double mediaAvaliacoes) {
		this.mediaAvaliacoes = mediaAvaliacoes;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}
}