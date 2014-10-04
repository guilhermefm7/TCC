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
public class PossivelAvaliacaoMusica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkPossivelAvaliacaoMusica;
	private Integer quantidadeOuvida;
	private Integer status = Constantes.TIPO_STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;


	/*-*-*-* Construtores *-*-*-*/
	public PossivelAvaliacaoMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }

	public long getPkPossivelAvaliacaoMusica() {
		return pkPossivelAvaliacaoMusica;
	}

	public void setPkPossivelAvaliacaoMusica(long pkPossivelAvaliacaoMusica) {
		this.pkPossivelAvaliacaoMusica = pkPossivelAvaliacaoMusica;
	}

	public Integer getQuantidadeOuvida() {
		return quantidadeOuvida;
	}

	public void setQuantidadeOuvida(Integer quantidadeOuvida) {
		this.quantidadeOuvida = quantidadeOuvida;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}