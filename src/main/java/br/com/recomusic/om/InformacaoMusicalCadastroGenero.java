package br.com.recomusic.om;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import br.com.recomusic.persistencia.utils.Constantes;

@Entity
public class InformacaoMusicalCadastroGenero implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkInformacaoMusicalCadastroGenero;
	private Integer status = Constantes.TIPO_STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkGenero")
	private Genero genero;

	/*-*-*-* Construtores *-*-*-*/
	public InformacaoMusicalCadastroGenero() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkInformacaoMusicalCadastroGenero() { return pkInformacaoMusicalCadastroGenero; }
	public void setPkInformacaoMusicalCadastroGenero(long pkInformacaoMusicalCadastroGenero) { this.pkInformacaoMusicalCadastroGenero = pkInformacaoMusicalCadastroGenero; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }
	
	public Genero getGenero() { return genero; }
	public void setGenero(Genero genero) { this.genero = genero; }
}