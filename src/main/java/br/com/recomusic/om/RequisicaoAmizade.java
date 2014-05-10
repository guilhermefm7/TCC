package br.com.mresolucoes.atta.modulos.frota.persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.com.mresolucoes.atta.modulos.atta.utils.BDConstantesAtta;
import org.hibernate.annotations.Type;
import utils.data.Data;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

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
	private int status = BDConstantesAtta.STATUS_ATIVO;
	@Type(type="timestamp")
	private Data lancamento;

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

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public Usuario getUsuarioRequisitante() { return usuarioRequisitante; }
	public void setUsuarioRequisitante(Usuario usuarioRequisitante) { this.usuarioRequisitante = usuarioRequisitante; }

	public Usuario getUsuarioRequisitado() { return usuarioRequisitado; }
	public void setUsuarioRequisitado(Usuario usuarioRequisitado) { this.usuarioRequisitado = usuarioRequisitado; }
}