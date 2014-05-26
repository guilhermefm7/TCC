package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Constantes;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="UsuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO;
	private Usuario usuario;
	private String emailLogin;
	private boolean logado;
	private boolean emailCorreto;

	public UsuarioBean()
	{
		this.usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
		this.logado = false;
		this.emailCorreto = false;
		usuario = new Usuario();
	}

	public void message(String message)
	{

        FacesContext instance = FacesContext.getCurrentInstance();
        instance.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

	public void logar()
	{
		try
		{
			Usuario usuarioRecebido = null;
			if(emailLogin!=null && emailLogin.contains("@") && usuario.getSenha()!=null && usuario.getSenha().length()>=6)
			{
				usuarioRecebido = usuarioDAO.validarUsuario(emailLogin, usuario.getSenha());
			}
			
			if(usuarioRecebido!=null && usuarioRecebido.getPkUsuario()>0)
			{
				this.logado = true;
			}
			
			if(!this.logado)
			{
				usuarioRecebido = null;
				if(emailLogin!=null && emailLogin.length()>=4)
				{
					usuarioRecebido = usuarioDAO.validarLogin(emailLogin);
					
					if(usuarioRecebido!=null && usuarioRecebido.getPkUsuario()>0)
					{
						this.logado = true;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void salvar()
	{
		try
		{
			Usuario validaEmail;
			if(usuario.getEmailUsuario().contains("@"))
			{
				validaEmail = null;
				if(usuario.getEmailUsuario()!=null)
				{
					validaEmail = usuarioDAO.validarEmail(usuario.getEmailUsuario());
				}

				if(validaEmail!=null && validaEmail.getPkUsuario()>0)
				{
					message("Endereço de email informado já está cadastrado!");
					validaEmail = null;
					if(usuario.getLogin()!=null && usuario.getLogin().length()>=4)
					{
						validaEmail = usuarioDAO.validarLogin(usuario.getLogin());
						
						if(validaEmail!=null && validaEmail.getPkUsuario()>0)
						{
							message("Login digitado já existe!");
						}
					}
					else
					{
						message("Login deve ter no mínimo 4 caracteres!");
					}
				}
				else
				{
					if(usuario.getSenha().length()>=6)
					{
						validaEmail = null;
						if(usuario.getLogin()!=null && usuario.getLogin().length()>=4)
						{
							validaEmail = usuarioDAO.validarLogin(usuario.getLogin());
							
							if(validaEmail!=null && validaEmail.getPkUsuario()>0)
							{
								message("Login digitado já existe!");
							}
							else
							{
								usuario.setStatus(Constantes.TIPO_STATUS_ATIVO);
								usuario.setLancamento(new Date());
								save(usuario);
								this.logado = true;
							}
						}
						else
						{
							message("Login deve ter no mínimo 4 caracteres!");
						}
					}
					else
					{
						message("Senha deve possuir no mínimo 6 caracteres!");
						validaEmail = null;
						if(usuario.getLogin()!=null && usuario.getLogin().length()>=4)
						{
							validaEmail = usuarioDAO.validarLogin(usuario.getLogin());
							
							if(validaEmail!=null && validaEmail.getPkUsuario()>0)
							{
								message("Login digitado já existe!");
							}
						}
						else
						{
							message("Login deve ter no mínimo 4 caracteres!");
						}
					}
				}
			}
			else
			{
				message("Email inválido!");
				if(usuario.getSenha().length()<6)
				{
					message("Senha deve possuir no mínimo 6 caracteres!");
				}
				
				validaEmail = null;
				if(usuario.getLogin()!=null && usuario.getLogin().length()>=4)
				{
					validaEmail = usuarioDAO.validarLogin(usuario.getLogin());
					
					if(validaEmail!=null && validaEmail.getPkUsuario()>0)
					{
						message("Login digitado já existe!");
					}
				}
				else
				{
					message("Login deve ter no mínimo 4 caracteres!");
				}
			}
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
	public boolean getEmailCorreto() {
		return emailCorreto;
	}
	
	public String getEmailLogin() {
		return emailLogin;
	}

	public void setEmailLogin(String emailLogin) {
		this.emailLogin = emailLogin;
	}
	
	public void setEmailCorreto(boolean emailCorreto) {
		this.emailCorreto = emailCorreto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}