package br.com.recomusic.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Genero implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkGenero;
	private String nomeGenero;

	@OneToMany(mappedBy="genero")
	private List<BandaGenero> bandaGeneros;

	@OneToMany(mappedBy="genero")
	private List<MusicaGenero> musicaGeneros;

	@OneToMany(mappedBy="genero")
	private List<InformacaoMusicalCadastroGenero> informacaoMusicalCadastroGeneros;


	/*-*-*-* Construtores *-*-*-*/
	public Genero() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkGenero() { return pkGenero; }
	public void setPkGenero(long pkGenero) { this.pkGenero = pkGenero; }

	public String getNomeGenero() { return nomeGenero; }
	public void setNomeGenero(String nomeGenero) { this.nomeGenero = nomeGenero; }

	public List<BandaGenero> getBandaGeneros() { if(bandaGeneros==null) { bandaGeneros = new ArrayList<BandaGenero>(); } return bandaGeneros; }
	public void setBandaGeneros(List<BandaGenero> bandaGeneros) { this.bandaGeneros = bandaGeneros; }

	public List<MusicaGenero> getMusicaGeneros() { if(musicaGeneros==null) { musicaGeneros = new ArrayList<MusicaGenero>(); } return musicaGeneros; }
	public void setMusicaGeneros(List<MusicaGenero> musicaGeneros) { this.musicaGeneros = musicaGeneros; }

	public List<InformacaoMusicalCadastroGenero> getInformacaoMusicalCadastroGeneros() { if(informacaoMusicalCadastroGeneros==null) { informacaoMusicalCadastroGeneros = new ArrayList<InformacaoMusicalCadastroGenero>(); } return informacaoMusicalCadastroGeneros; }
	public void setInformacaoMusicalCadastroGeneros(List<InformacaoMusicalCadastroGenero> informacaoMusicalCadastroGeneros) { this.informacaoMusicalCadastroGeneros = informacaoMusicalCadastroGeneros; }
}