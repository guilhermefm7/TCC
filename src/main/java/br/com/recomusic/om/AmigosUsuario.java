package br.com.recomusic.om;

import javax.persistence.*;

import java.beans.Transient;
import java.io.Serializable;

import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Constantes;

@Entity
public class AmigosUsuario implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkAmigosUsuario;
	private int status = Constantes.TIPO_STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkAmigo")
	private Usuario amigo;


	/*-*-*-* Construtores *-*-*-*/
	public AmigosUsuario() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkAmigosUsuario() { return pkAmigosUsuario; }
	public void setPkAmigosUsuario(long pkAmigosUsuario) { this.pkAmigosUsuario = pkAmigosUsuario; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Usuario getAmigo() { return amigo; }
	public void setAmigo(Usuario amigo) { this.amigo = amigo; }
}