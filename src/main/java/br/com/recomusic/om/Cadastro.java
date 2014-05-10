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

import br.com.recomusic.persistencia.utils.Constantes;

@Entity
public class Cadastro implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkCadastro;
	private String nome;
	@Type(type="date")
	private Date dataNascimento;
	private int sexo;
	private int status = Constantes.TIPO_STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkPais")
	private Pais pais;

	@ManyToOne @JoinColumn(name="fkInformacaoMusicalCadastro")
	private InformacaoMusicalCadastro informacaoMusicalCadastro;
	private String sobrenome;


	/*-*-*-* Construtores *-*-*-*/
	public Cadastro() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkCadastro() { return pkCadastro; }
	public void setPkCadastro(long pkCadastro) { this.pkCadastro = pkCadastro; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public Date getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

	public int getSexo() { return sexo; }
	public void setSexo(int sexo) { this.sexo = sexo; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Pais getPais() { return pais; }
	public void setPais(Pais pais) { this.pais = pais; }

	public InformacaoMusicalCadastro getInformacaoMusicalCadastro() { return informacaoMusicalCadastro; }
	public void setInformacaoMusicalCadastro(InformacaoMusicalCadastro informacaoMusicalCadastro) { this.informacaoMusicalCadastro = informacaoMusicalCadastro; }

	public String getSobrenome() { return sobrenome; }
	public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }
}