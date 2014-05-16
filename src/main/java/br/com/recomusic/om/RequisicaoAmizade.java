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
public class RequisicaoAmizade implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkRequisicaoAmizade;
	private Boolean resposta;
	private Integer status = Constantes.TIPO_STATUS_ATIVO;
	@Type(type="timestamp")
	private Date lancamento;

	@ManyToOne @JoinColumn(name="fkUsuarioRequisitante")
	private Usuario usuarioRequisitante;

	@ManyToOne @JoinColumn(name="fkUsuarioRequisitado")
	private Usuario usuarioRequisitado;


	/*-*-*-* Construtores *-*-*-*/
	public RequisicaoAmizade() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkRequisicaoAmizade() { return pkRequisicaoAmizade; }
	public void setPkRequisicaoAmizade(long pkRequisicaoAmizade) { this.pkRequisicaoAmizade = pkRequisicaoAmizade; }

	public Boolean getResposta() { return resposta; }
	public void setResposta(Boolean resposta) { this.resposta = resposta; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Date getLancamento() { return lancamento; }
	public void setLancamento(Date lancamento) { this.lancamento = lancamento; }

	public Usuario getUsuarioRequisitante() { return usuarioRequisitante; }
	public void setUsuarioRequisitante(Usuario usuarioRequisitante) { this.usuarioRequisitante = usuarioRequisitante; }

	public Usuario getUsuarioRequisitado() { return usuarioRequisitado; }
	public void setUsuarioRequisitado(Usuario usuarioRequisitado) { this.usuarioRequisitado = usuarioRequisitado; }
}