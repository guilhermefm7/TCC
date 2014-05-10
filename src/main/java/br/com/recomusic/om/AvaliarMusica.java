package br.com.recomusic.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@OneToMany(mappedBy="avaliarMusica")
	private List<AvaliarMusicaMusica> avaliarMusicaMusicas;


	/*-*-*-* Construtores *-*-*-*/
	public AvaliarMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkAvaliarMusica() { return pkAvaliarMusica; }
	public void setPkAvaliarMusica(long pkAvaliarMusica) { this.pkAvaliarMusica = pkAvaliarMusica; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public List<AvaliarMusicaMusica> getAvaliarMusicaMusicas() { if(avaliarMusicaMusicas==null) { avaliarMusicaMusicas = new ArrayList<AvaliarMusicaMusica>(); } return avaliarMusicaMusicas; }
	public void setAvaliarMusicaMusicas(List<AvaliarMusicaMusica> avaliarMusicaMusicas) { this.avaliarMusicaMusicas = avaliarMusicaMusicas; }
}