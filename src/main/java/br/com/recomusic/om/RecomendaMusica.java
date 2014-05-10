package br.com.mresolucoes.atta.modulos.frota.persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import utils.data.Data;
import br.com.mresolucoes.atta.core.persistencia.annotations.ValorPadrao;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class RecomendaMusica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkRecomendaMusica;
	@Type(type="timestamp")
	private Data lancamento;
	@ValorPadrao(valor="false")
	private boolean enviouRecomendacao;

	@ManyToOne @JoinColumn(name="fkUsuarioRecomendou")
	private Usuario usuarioRecomendou;

	@ManyToOne @JoinColumn(name="fkUsuarioRecomendado")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;


	/*-*-*-* Construtores *-*-*-*/
	public RecomendaMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkRecomendaMusica() { return pkRecomendaMusica; }
	public void setPkRecomendaMusica(long pkRecomendaMusica) { this.pkRecomendaMusica = pkRecomendaMusica; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public boolean getEnviouRecomendacao() { return enviouRecomendacao; }
	public void setEnviouRecomendacao(boolean enviouRecomendacao) { this.enviouRecomendacao = enviouRecomendacao; }

	public Usuario getUsuarioRecomendou() { return usuarioRecomendou; }
	public void setUsuarioRecomendou(Usuario usuarioRecomendou) { this.usuarioRecomendou = usuarioRecomendou; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }
}