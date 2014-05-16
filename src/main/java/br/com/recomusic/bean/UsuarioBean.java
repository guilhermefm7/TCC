package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;


 
@ManagedBean(name="UsuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO;
	private String email;
	private String senha;
	
	private boolean logado;
	
	public UsuarioBean()
	{
        usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
        logado = false;
    }
	
	public void logar()
	{
        try
        {
        	boolean validacaoUsuario = usuarioDAO.validarUsuario(email, senha);
			this.logado = validacaoUsuario;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ConectaBanco.getInstance().rollBack();
        }
	}
	
	public void deslogar()
	{
		this.logado = false;
	}
	
	 public void save(Usuario usuario)
	 {
	        try
	        {
	        	ConectaBanco.getInstance().beginTransaction();
	            usuarioDAO.save(usuario);
	            ConectaBanco.getInstance().commit();
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	            ConectaBanco.getInstance().rollBack();
	        }
	    }
	     
	    public List<Usuario> findAll()
	    {
	        return usuarioDAO.findAll();
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