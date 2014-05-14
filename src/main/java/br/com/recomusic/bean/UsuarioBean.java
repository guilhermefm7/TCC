package br.com.recomusic.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
 
@ManagedBean(name="UsuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String senha;
	
	private boolean logado;
	
	public UsuarioBean()
	{
		logado = false;
	}
	
	public void logar()
	{
		boolean validacaoUsuario = new UsuarioDAO().validarUsuario(this.email, this.senha);
		this.logado = validacaoUsuario;
	}
	
	public void deslogar()
	{
		this.logado = false;
	}
	
	
	// 		Getters and Setters			//
	
	public boolean getLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	
}