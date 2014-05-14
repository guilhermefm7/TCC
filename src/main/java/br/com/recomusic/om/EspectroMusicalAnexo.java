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
public class EspectroMusicalAnexo implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkEspectroMusicalAnexo;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;


	/*-*-*-* Construtores *-*-*-*/
	public EspectroMusicalAnexo() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkEspectroMusicalAnexo() { return pkEspectroMusicalAnexo; }
	public void setPkEspectroMusicalAnexo(long pkEspectroMusicalAnexo) { this.pkEspectroMusicalAnexo = pkEspectroMusicalAnexo; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }
}