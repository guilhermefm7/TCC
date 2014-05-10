package br.com.recomusic.om;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
public class EnviaMensagem implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long EnviaMensagem;
	@Type(type="text")
	private String mensagem;
	private int mensagemEnviada;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuarioEnviou;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuarioRecebeu;


	/*-*-*-* Construtores *-*-*-*/
	public EnviaMensagem() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getEnviaMensagem() { return EnviaMensagem; }
	public void setEnviaMensagem(long EnviaMensagem) { this.EnviaMensagem = EnviaMensagem; }

	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

	public int getMensagemEnviada() { return mensagemEnviada; }
	public void setMensagemEnviada(int mensagemEnviada) { this.mensagemEnviada = mensagemEnviada; }

	public Usuario getUsuarioEnviou() { return usuarioEnviou; }
	public void setUsuarioEnviou(Usuario usuarioEnviou) { this.usuarioEnviou = usuarioEnviou; }

	public Usuario getUsuarioRecebeu() { return usuarioRecebeu; }
	public void setUsuarioRecebeu(Usuario usuarioRecebeu) { this.usuarioRecebeu = usuarioRecebeu; }
}