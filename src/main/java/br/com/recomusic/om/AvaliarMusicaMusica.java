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

@Entity
public class AvaliarMusicaMusica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkAvaliarMusicaMusica;
	private Boolean avaliar;
	@Type(type="timestamp")
	private Date lancamento;

	@ManyToOne @JoinColumn(name="fkAvaliarMusica")
	private AvaliarMusica avaliarMusica;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;


	/*-*-*-* Construtores *-*-*-*/
	public AvaliarMusicaMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkAvaliarMusicaMusica() { return pkAvaliarMusicaMusica; }
	public void setPkAvaliarMusicaMusica(long pkAvaliarMusicaMusica) { this.pkAvaliarMusicaMusica = pkAvaliarMusicaMusica; }

	public Boolean getAvaliar() { return avaliar; }
	public void setAvaliar(Boolean avaliar) { this.avaliar = avaliar; }

	public Date getLancamento() { return lancamento; }
	public void setLancamento(Date lancamento) { this.lancamento = lancamento; }

	public AvaliarMusica getAvaliarMusica() { return avaliarMusica; }
	public void setAvaliarMusica(AvaliarMusica avaliarMusica) { this.avaliarMusica = avaliarMusica; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }
}