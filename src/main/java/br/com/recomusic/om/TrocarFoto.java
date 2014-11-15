package br.com.recomusic.om;

import java.io.InputStream;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import br.com.recomusic.persistencia.utils.Constantes;

@Entity
public class TrocarFoto implements Serializable {
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pkTrocarFoto;
	private String nome;
	private String tipo;
	private String pathFoto;
	private String pathFotoImagem;
	private String tamanho;
	private Integer status = Constantes.TIPO_STATUS_ATIVO;

    
    @XmlTransient
    @Transient
    private InputStream inputStream;
	
	@ManyToOne
	@JoinColumn(name = "fkUsuario")
	private Usuario usuario;

	/*-*-*-* Construtores *-*-*-*/
	public TrocarFoto() {
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public int getStatus() {
		return status;
	}

	public long getPkTrocarFoto() {
		return pkTrocarFoto;
	}

	public void setPkTrocarFoto(long pkTrocarFoto) {
		this.pkTrocarFoto = pkTrocarFoto;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPathFoto() {
		return pathFoto;
	}

	public void setPathFoto(String pathFoto) {
		this.pathFoto = pathFoto;
	}

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getPathFotoImagem() {
		return pathFotoImagem;
	}

	public void setPathFotoImagem(String pathFotoImagem) {
		this.pathFotoImagem = pathFotoImagem;
	}
}