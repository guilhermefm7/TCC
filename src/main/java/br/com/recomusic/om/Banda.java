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
public class Banda implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkBanda;
	private String nome;
	private String ano;
	private String idBanda;

	@ManyToOne @JoinColumn(name="fkPais")
	private Pais pais;

	@OneToMany(mappedBy="banda")
	private List<Musica> musicas;

	@OneToMany(mappedBy="banda")
	private List<BandaGenero> bandaGeneros;

	@OneToMany(mappedBy="banda")
	private List<InformacaoMusicalCadastroBanda> informacaoMusicalCadastroBandas;


	/*-*-*-* Construtores *-*-*-*/
	public Banda() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkBanda() { return pkBanda; }
	public void setPkBanda(long pkBanda) { this.pkBanda = pkBanda; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getAno() { return ano; }
	public void setAno(String ano) { this.ano = ano; }
	
	public String getIdBanda() { return idBanda; }
	public void setIdBanda(String idBanda) { this.idBanda = idBanda; }

	public Pais getPais() { return pais; }
	public void setPais(Pais pais) { this.pais = pais; }

	public List<Musica> getMusicas() { if(musicas==null) { musicas = new ArrayList<Musica>(); } return musicas; }
	public void setMusicas(List<Musica> musicas) { this.musicas = musicas; }

	public List<BandaGenero> getBandaGeneros() { if(bandaGeneros==null) { bandaGeneros = new ArrayList<BandaGenero>(); } return bandaGeneros; }
	public void setBandaGeneros(List<BandaGenero> bandaGeneros) { this.bandaGeneros = bandaGeneros; }

	public List<InformacaoMusicalCadastroBanda> getInformacaoMusicalCadastroBandas() { if(informacaoMusicalCadastroBandas==null) { informacaoMusicalCadastroBandas = new ArrayList<InformacaoMusicalCadastroBanda>(); } return informacaoMusicalCadastroBandas; }
	public void setInformacaoMusicalCadastroBandas(List<InformacaoMusicalCadastroBanda> informacaoMusicalCadastroBandas) { this.informacaoMusicalCadastroBandas = informacaoMusicalCadastroBandas; }
}