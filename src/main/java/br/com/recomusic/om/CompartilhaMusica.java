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
public class CompartilhaMusica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkCompartilhaMusica;
	@Type(type="timestamp")
	private Date lancamento;
	private Integer status = Constantes.TIPO_STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;


	/*-*-*-* Construtores *-*-*-*/
	public CompartilhaMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkCompartilhaMusica() { return pkCompartilhaMusica; }
	public void setPkCompartilhaMusica(long pkCompartilhaMusica) { this.pkCompartilhaMusica = pkCompartilhaMusica; }

	public Date getLancamento() { return lancamento; }
	public void setLancamento(Date lancamento) { this.lancamento = lancamento; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }
}