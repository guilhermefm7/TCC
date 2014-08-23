package br.com.recomusic.om;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class MediaUsuarioGenero implements Serializable {
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pkMediaUsuarioGenero;
	private Double media;
	private Double quantidadeMusicas;
	private Double mediaAvaliacoes;

	@ManyToOne
	@JoinColumn(name = "fkUsuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "fkGenero")
	private Genero genero;

	/*-*-*-* Construtores *-*-*-*/
	public MediaUsuarioGenero() {
	}

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkMediaUsuarioGenero() {
		return pkMediaUsuarioGenero;
	}

	public void setPkMediaUsuarioGenero(long pkMediaUsuarioGenero) {
		this.pkMediaUsuarioGenero = pkMediaUsuarioGenero;
	}

	public Double getMedia() {
		return media;
	}

	public void setMedia(Double media) {
		this.media = media;
	}

	public Double getQuantidadeMusicas() {
		return quantidadeMusicas;
	}

	public void setQuantidadeMusicas(Double quantidadeMusicas) {
		this.quantidadeMusicas = quantidadeMusicas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public Double getMediaAvaliacoes() {
		return mediaAvaliacoes;
	}

	public void setMediaAvaliacoes(Double mediaAvaliacoes) {
		this.mediaAvaliacoes = mediaAvaliacoes;
	}
}