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

import br.com.recomusic.persistencia.utils.Constantes;

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
	private Integer status = Constantes.TIPO_STATUS_ATIVO;

	@OneToMany(mappedBy="banda")
	private List<Musica> musicas;

    @XmlElement(nillable = true)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "banda", orphanRemoval = true)
    @Cascade(value = {
                    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE, CascadeType.DELETE, CascadeType.REMOVE,
                    CascadeType.DELETE_ORPHAN})
    @Fetch(FetchMode.SELECT)
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
	
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }

	public List<Musica> getMusicas() { if(musicas==null) { musicas = new ArrayList<Musica>(); } return musicas; }
	public void setMusicas(List<Musica> musicas) { this.musicas = musicas; }

	public List<BandaGenero> getBandaGeneros() { if(bandaGeneros==null) { bandaGeneros = new ArrayList<BandaGenero>(); } return bandaGeneros; }
	public void setBandaGeneros(List<BandaGenero> bandaGeneros) { this.bandaGeneros = bandaGeneros; }

	public List<InformacaoMusicalCadastroBanda> getInformacaoMusicalCadastroBandas() { if(informacaoMusicalCadastroBandas==null) { informacaoMusicalCadastroBandas = new ArrayList<InformacaoMusicalCadastroBanda>(); } return informacaoMusicalCadastroBandas; }
	public void setInformacaoMusicalCadastroBandas(List<InformacaoMusicalCadastroBanda> informacaoMusicalCadastroBandas) { this.informacaoMusicalCadastroBandas = informacaoMusicalCadastroBandas; }
}