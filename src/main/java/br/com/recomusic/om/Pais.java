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
public class Pais implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkPais;
	private String nomePais;
	private String siglaPais;

	@OneToMany(mappedBy="pais")
	private List<Cadastro> cadastros;

	@OneToMany(mappedBy="pais")
	private List<Banda> bandas;

	@OneToMany(mappedBy="pais")
	private List<Musica> musicas;


	/*-*-*-* Construtores *-*-*-*/
	public Pais() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkPais() { return pkPais; }
	public void setPkPais(long pkPais) { this.pkPais = pkPais; }

	public String getNomePais() { return nomePais; }
	public void setNomePais(String nomePais) { this.nomePais = nomePais; }

	public String getSiglaPais() { return siglaPais; }
	public void setSiglaPais(String siglaPais) { this.siglaPais = siglaPais; }

	public List<Cadastro> getCadastros() { if(cadastros==null) { cadastros = new ArrayList<Cadastro>(); } return cadastros; }
	public void setCadastros(List<Cadastro> cadastros) { this.cadastros = cadastros; }

	public List<Banda> getBandas() { if(bandas==null) { bandas = new ArrayList<Banda>(); } return bandas; }
	public void setBandas(List<Banda> bandas) { this.bandas = bandas; }

	public List<Musica> getMusicas() { if(musicas==null) { musicas = new ArrayList<Musica>(); } return musicas; }
	public void setMusicas(List<Musica> musicas) { this.musicas = musicas; }
}