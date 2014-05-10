package br.com.mresolucoes.atta.modulos.frota.persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import utils.data.Data;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

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
	private Data lancamento;

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

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public AvaliarMusica getAvaliarMusica() { return avaliarMusica; }
	public void setAvaliarMusica(AvaliarMusica avaliarMusica) { this.avaliarMusica = avaliarMusica; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }
}