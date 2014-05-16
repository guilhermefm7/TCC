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

import br.com.recomusic.persistencia.utils.Constantes;

@Entity
public class InformacaoMusicalCadastro implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkInformacaoMusicalCadastro;
	private Integer status = Constantes.TIPO_STATUS_ATIVO;

	@OneToMany(mappedBy="informacaoMusicalCadastro")
	private List<Cadastro> cadastros;

	@OneToMany(mappedBy="informacaoMusicalCadastro")
	private List<InformacaoMusicalCadastroGenero> informacaoMusicalCadastroGeneros;

	@OneToMany(mappedBy="informacaoMusicalCadastro")
	private List<InformacaoMusicalCadastroMusica> informacaoMusicalCadastroMusicas;

	@OneToMany(mappedBy="informacaoMusicalCadastro")
	private List<InformacaoMusicalCadastroBanda> informacaoMusicalCadastroBandas;


	/*-*-*-* Construtores *-*-*-*/
	public InformacaoMusicalCadastro() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkInformacaoMusicalCadastro() { return pkInformacaoMusicalCadastro; }
	public void setPkInformacaoMusicalCadastro(long pkInformacaoMusicalCadastro) { this.pkInformacaoMusicalCadastro = pkInformacaoMusicalCadastro; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public List<Cadastro> getCadastros() { if(cadastros==null) { cadastros = new ArrayList<Cadastro>(); } return cadastros; }
	public void setCadastros(List<Cadastro> cadastros) { this.cadastros = cadastros; }

	public List<InformacaoMusicalCadastroGenero> getInformacaoMusicalCadastroGeneros() { if(informacaoMusicalCadastroGeneros==null) { informacaoMusicalCadastroGeneros = new ArrayList<InformacaoMusicalCadastroGenero>(); } return informacaoMusicalCadastroGeneros; }
	public void setInformacaoMusicalCadastroGeneros(List<InformacaoMusicalCadastroGenero> informacaoMusicalCadastroGeneros) { this.informacaoMusicalCadastroGeneros = informacaoMusicalCadastroGeneros; }

	public List<InformacaoMusicalCadastroMusica> getInformacaoMusicalCadastroMusicas() { if(informacaoMusicalCadastroMusicas==null) { informacaoMusicalCadastroMusicas = new ArrayList<InformacaoMusicalCadastroMusica>(); } return informacaoMusicalCadastroMusicas; }
	public void setInformacaoMusicalCadastroMusicas(List<InformacaoMusicalCadastroMusica> informacaoMusicalCadastroMusicas) { this.informacaoMusicalCadastroMusicas = informacaoMusicalCadastroMusicas; }

	public List<InformacaoMusicalCadastroBanda> getInformacaoMusicalCadastroBandas() { if(informacaoMusicalCadastroBandas==null) { informacaoMusicalCadastroBandas = new ArrayList<InformacaoMusicalCadastroBanda>(); } return informacaoMusicalCadastroBandas; }
	public void setInformacaoMusicalCadastroBandas(List<InformacaoMusicalCadastroBanda> informacaoMusicalCadastroBandas) { this.informacaoMusicalCadastroBandas = informacaoMusicalCadastroBandas; }
}