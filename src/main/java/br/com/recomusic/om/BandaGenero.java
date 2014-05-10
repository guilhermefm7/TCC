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
public class BandaGenero implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkBandaGenero;

	@ManyToOne @JoinColumn(name="fkGenero")
	private Genero genero;

	@ManyToOne @JoinColumn(name="fkBanda")
	private Banda banda;


	/*-*-*-* Construtores *-*-*-*/
	public BandaGenero() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkBandaGenero() { return pkBandaGenero; }
	public void setPkBandaGenero(long pkBandaGenero) { this.pkBandaGenero = pkBandaGenero; }

	public Genero getGenero() { return genero; }
	public void setGenero(Genero genero) { this.genero = genero; }

	public Banda getBanda() { return banda; }
	public void setBanda(Banda banda) { this.banda = banda; }
}