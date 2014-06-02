package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Constantes;
import br.com.recomusic.singleton.ConectaBanco;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.User;


@ManagedBean(name="UsuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private Usuario usuario = new Usuario();
	private String emailLogin = null;
	private String token;
	private boolean logado = false;
	private boolean emailCorreto = false;
	private String mensagemErroLogin = null;
	private String sexo = null;

	public UsuarioBean() { }

	public void logar()
	{
		try
		{
			Usuario usuarioRecebido = null;
			if(emailLogin!=null && emailLogin.contains("@") && usuario.getSenha()!=null && usuario.getSenha().length()>=6)
			{
				usuarioRecebido = usuarioDAO.validarUsuarioEmail(emailLogin, usuario.getSenha());
			}
			
			if(usuarioRecebido!=null && usuarioRecebido.getPkUsuario()>0)
			{
				this.logado = true;
			}
			
			if(!this.logado)
			{
				usuarioRecebido = null;
				if(emailLogin!=null && emailLogin.length()>=4 && usuario.getSenha()!=null && usuario.getSenha().length()>=6)
				{
					usuarioRecebido = usuarioDAO.validarUsuarioLogin(emailLogin, usuario.getSenha());
					
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
	
	public void logarComFacebook()
	{
		try
		{
			if(token!=null && token.length()>0)
			{
				FacesContext facesContext = FacesContext.getCurrentInstance();
				if (!facesContext.isPostback() && !facesContext.isValidationFailed())
				{
					 FacebookClient facebookClient = new DefaultFacebookClient(token);
				     User facebookUser = facebookClient.fetchObject("me", User.class);
				     if(facebookUser!=null && facebookUser.getId().length()>0)
				     {
				    	 Usuario usuario = usuarioDAO.validarID(facebookUser.getId());
				    	 if(usuario!=null && usuario.getPkUsuario()>0)
				    	 {
				    		 this.emailLogin = facebookUser.getEmail();
				    		 this.logado = true;
				    	 }
				    	 else
				    	 {
				    		 if(facebookUser.getEmail()!=null && facebookUser.getEmail().length()>0)
				    		 {
				    			 usuario = usuarioDAO.validarEmail(facebookUser.getEmail());
				    			 if(usuario!=null && usuario.getPkUsuario()>0)
				    			 {
				    				 mensagemErroLogin = "Este email j� est� cadastrado em outro usu�rio";
				    			 }
				    			 else
				    			 {
					    			 usuario = new Usuario();
					    			 usuario.setEmailUsuario(facebookUser.getEmail());
					    			 usuario.setIdFacebook(facebookUser.getId());
					    			 usuario.setNome(facebookUser.getFirstName());
					    			 usuario.setSobrenome(facebookUser.getLastName());
					    			 usuario.setLancamento(new Date());
					    			 if(facebookUser.getGender()!=null && facebookUser.getGender().length()>0 && (facebookUser.getGender().equals("male")))
					    			 {
					    				 usuario.setSexo(Constantes.TIPO_GENERO_MASCULINO);
					    			 }
					    			 else if(facebookUser.getGender()!=null && facebookUser.getGender().length()>0 && (facebookUser.getGender().equals("female")))
					    			 {
					    				 usuario.setSexo(Constantes.TIPO_GENERO_FEMININO);
					    			 }
					    			 save(usuario);
					    			 this.emailLogin = facebookUser.getEmail();
					    			 this.logado = true;
				    			 }
				    		 }
				    	 }
				     }
				     
				     System.out.println(facebookUser.getFirstName() + " " + facebookUser.getEmail() + " " + facebookUser.getId() + "" + facebookUser.getGender());
					 Connection<NamedFacebookType> musics = facebookClient.fetchConnection("me/music", NamedFacebookType.class);
					 if(musics.getData()!=null && musics.getData().size()>0)
					 {
						 for (int i = 0; i < musics.getData().size(); i++)
						 {
							System.out.println(musics.getData().get(i).getName());
						 }
					 }
				}
			}
			token = null;
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
			mensagemErroLogin = verificaConsistencia();
			
			if(mensagemErroLogin=="")
			{
				Usuario validaEmail = null;
				if(usuario.getEmailUsuario()!=null)
				{
					validaEmail = usuarioDAO.validarEmail(usuario.getEmailUsuario());
				}
				
				if(!(validaEmail!=null && validaEmail.getPkUsuario()>0))
				{
					validaEmail = null;
					if(usuario.getLogin()!=null)
					{
						validaEmail = usuarioDAO.validarLogin(usuario.getLogin());
						if(!((validaEmail!=null && validaEmail.getPkUsuario()>0)))
						{
							usuario.setSexo(Integer.valueOf(this.sexo));
							usuario.setStatus(Constantes.TIPO_STATUS_ATIVO);
							usuario.setLancamento(new Date());
							save(usuario);
							this.emailLogin = usuario.getLogin();
							this.logado = true;
						}
						else
						{
							mensagemErroLogin = "Login informado j� existe";
						}
					}
				}
				else
				{
					mensagemErroLogin = "Email informado j� foi cadastrado";
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	
	public String verificaConsistencia() throws Exception
	{
		List<String> erros = new ArrayList<String>();
		
		if(usuario.getLogin()==null || usuario.getLogin().length()==0) { erros.add("Login");  }
		if(usuario.getNome()==null || usuario.getNome().length()==0) { erros.add("Nome");  }
		if(usuario.getSobrenome()==null || usuario.getSobrenome().length()==0) { erros.add("Sobrenome");  }
		if(usuario.getEmailUsuario()==null || usuario.getEmailUsuario().length()==0) { erros.add("Email");  }
		if(usuario.getSenha()==null || usuario.getSenha().length()==0) { erros.add("Senha");  }
		if(this.sexo==null || this.sexo.length()==0) { erros.add("G�nero");  }
		
		if(erros.size()>1) 								{ return "Os campos " + erros.toString() + " s�o requeridos." ;	}
		else if(erros.size()==1) 						{ return "O campo " + erros.toString() + " � requerido."; 		}
		
		if(!(usuario.getEmailUsuario().contains("@")))	{ return "Email informado inv�lido" ; 							}	
		if(usuario.getLogin().length()<4)				{ return "Login deve possuir no m�nimo 4 caracteres" ; 			}	
		if(usuario.getSenha().length()<6)				{ return "Senha deve possuir no m�nimo 6 caracteres" ; 			}	
		
		return "";
	}
	
	public String mudarPagina()
	{
		try
		{
			return "logado?faces-redirect=true";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
		return "";
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
	
	public void setEmailCorreto(boolean emailCorreto) {
		this.emailCorreto = emailCorreto;
	} 
	
	public String getEmailLogin() {
		return emailLogin;
	}

	public void setEmailLogin(String emailLogin) {
		this.emailLogin = emailLogin;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getMensagemErroLogin() {
		return mensagemErroLogin;
	}

	public void setMensagemErroLogin(String mensagemErroLogin) {
		this.mensagemErroLogin = mensagemErroLogin;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}