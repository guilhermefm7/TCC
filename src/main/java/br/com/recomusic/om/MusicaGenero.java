package br.com.mresolucoes.atta.modulos.frota.persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class MusicaGenero implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkMusicaGenero;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;

	@ManyToOne @JoinColumn(name="fkGenero")
	private Genero genero;


	/*-*-*-* Construtores *-*-*-*/
	public MusicaGenero() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkMusicaGenero() { return pkMusicaGenero; }
	public void setPkMusicaGenero(long pkMusicaGenero) { this.pkMusicaGenero = pkMusicaGenero; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }

	public Genero getGenero() { return genero; }
	public void setGenero(Genero genero) { this.genero = genero; }
}