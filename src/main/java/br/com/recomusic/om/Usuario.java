package br.com.recomusic.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.recomusic.persistencia.utils.Constantes;

@Entity
public class Usuario implements Serializable {
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pkUsuario;
	private String emailUsuario;
	private String login;
	private String senha;
	private String nome;
	private String sobrenome;
	@Type(type = "timestamp")
	private Date lancamento;
	private int sexo;
	private String idFacebook;
	private Integer status = Constantes.TIPO_STATUS_ATIVO;

	@OneToMany(mappedBy = "usuarioRequisitante")
	private List<RequisicaoAmizade> requisicaoAmizadesUsuarioRequisitante;

	@OneToMany(mappedBy = "usuarioRequisitado")
	private List<RequisicaoAmizade> requisicaoAmizadesUsuarioRequisitado;

	@OneToMany(mappedBy = "usuario")
	private List<AmigosUsuario> amigosUsuariosUsuario;

	@OneToMany(mappedBy = "amigo")
	private List<AmigosUsuario> amigosUsuariosAmigo;

	@OneToMany(mappedBy = "usuario")
	private List<Playlist> playlists;

	@OneToMany(mappedBy = "usuario")
	private List<AvaliarMusica> avaliarMusicas;

	@OneToMany(mappedBy = "usuario")
	private List<MediaUsuarioGenero> mediaUsuarioGenero;
	
	@OneToMany(mappedBy = "usuario")
	private List<PossivelAvaliacaoMusica> possivelAvaliacaoMusica;

	/*-*-*-* Construtores *-*-*-*/
	public Usuario() {
	}

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkUsuario() {
		return pkUsuario;
	}

	public void setPkUsuario(long pkUsuario) {
		this.pkUsuario = pkUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getSexo() {
		return sexo;
	}

	public void setSexo(int sexo) {
		this.sexo = sexo;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Date getLancamento() {
		return lancamento;
	}

	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIdFacebook() {
		return idFacebook;
	}

	public void setIdFacebook(String idFacebook) {
		this.idFacebook = idFacebook;
	}

	public List<RequisicaoAmizade> getRequisicaoAmizadesUsuarioRequisitante() {
		if (requisicaoAmizadesUsuarioRequisitante == null) {
			requisicaoAmizadesUsuarioRequisitante = new ArrayList<RequisicaoAmizade>();
		}
		return requisicaoAmizadesUsuarioRequisitante;
	}

	public void setRequisicaoAmizadesUsuarioRequisitante(
			List<RequisicaoAmizade> requisicaoAmizadesUsuarioRequisitante) {
		this.requisicaoAmizadesUsuarioRequisitante = requisicaoAmizadesUsuarioRequisitante;
	}

	public List<RequisicaoAmizade> getRequisicaoAmizadesUsuarioRequisitado() {
		if (requisicaoAmizadesUsuarioRequisitado == null) {
			requisicaoAmizadesUsuarioRequisitado = new ArrayList<RequisicaoAmizade>();
		}
		return requisicaoAmizadesUsuarioRequisitado;
	}

	public void setRequisicaoAmizadesUsuarioRequisitado(
			List<RequisicaoAmizade> requisicaoAmizadesUsuarioRequisitado) {
		this.requisicaoAmizadesUsuarioRequisitado = requisicaoAmizadesUsuarioRequisitado;
	}

	public List<AmigosUsuario> getAmigosUsuariosUsuario() {
		if (amigosUsuariosUsuario == null) {
			amigosUsuariosUsuario = new ArrayList<AmigosUsuario>();
		}
		return amigosUsuariosUsuario;
	}

	public void setAmigosUsuariosUsuario(
			List<AmigosUsuario> amigosUsuariosUsuario) {
		this.amigosUsuariosUsuario = amigosUsuariosUsuario;
	}

	public List<AmigosUsuario> getAmigosUsuariosAmigo() {
		if (amigosUsuariosAmigo == null) {
			amigosUsuariosAmigo = new ArrayList<AmigosUsuario>();
		}
		return amigosUsuariosAmigo;
	}

	public void setAmigosUsuariosAmigo(List<AmigosUsuario> amigosUsuariosAmigo) {
		this.amigosUsuariosAmigo = amigosUsuariosAmigo;
	}

	public List<Playlist> getPlaylists() {
		if (playlists == null) {
			playlists = new ArrayList<Playlist>();
		}
		return playlists;
	}

	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}

	public List<AvaliarMusica> getAvaliarMusicas() {
		if (avaliarMusicas == null) {
			avaliarMusicas = new ArrayList<AvaliarMusica>();
		}
		return avaliarMusicas;
	}

	public void setAvaliarMusicas(List<AvaliarMusica> avaliarMusicas) {
		this.avaliarMusicas = avaliarMusicas;
	}

	public List<MediaUsuarioGenero> getMediaUsuarioGenero() {
		return mediaUsuarioGenero;
	}

	public void setMediaUsuarioGenero(
			List<MediaUsuarioGenero> mediaUsuarioGenero) {
		this.mediaUsuarioGenero = mediaUsuarioGenero;
	}

	public List<PossivelAvaliacaoMusica> getPossivelAvaliacaoMusica() {
		return possivelAvaliacaoMusica;
	}

	public void setPossivelAvaliacaoMusica(
			List<PossivelAvaliacaoMusica> possivelAvaliacaoMusica) {
		this.possivelAvaliacaoMusica = possivelAvaliacaoMusica;
	}
	
	
}