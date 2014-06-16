package br.com.recomusic.om;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class AvaliarMusica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkAvaliarMusica;
	private Boolean resposta;
	
	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;

	/*-*-*-* Construtores *-*-*-*/
	public AvaliarMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkAvaliarMusica() { return pkAvaliarMusica; }
	public void setPkAvaliarMusica(long pkAvaliarMusica) { this.pkAvaliarMusica = pkAvaliarMusica; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Musica getMusica() {return musica;}
	public void setMusica(Musica musica) {this.musica = musica;}
	
	public Boolean getResposta() { return resposta; }
	public void setResposta(Boolean resposta) { this.resposta = resposta; }
}