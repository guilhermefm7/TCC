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
public class InformacaoMusicalCadastroBanda implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkInformacaoMusicalCadastroBanda;
	private Integer status = Constantes.TIPO_STATUS_ATIVO;
	private int quantidade;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkBanda")
	private Banda banda;


	/*-*-*-* Construtores *-*-*-*/
	public InformacaoMusicalCadastroBanda() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkInformacaoMusicalCadastroBanda() { return pkInformacaoMusicalCadastroBanda; }
	public void setPkInformacaoMusicalCadastroBanda(long pkInformacaoMusicalCadastroBanda) { this.pkInformacaoMusicalCadastroBanda = pkInformacaoMusicalCadastroBanda; }

	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	
	public int getQuantidade() { return quantidade; }
	public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Banda getBanda() { return banda; }
	public void setBanda(Banda banda) { this.banda = banda; }
}