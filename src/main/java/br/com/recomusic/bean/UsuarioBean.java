package br.com.recomusic.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Constantes;
import br.com.recomusic.persistencia.utils.Criptografia;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.User;


@ManagedBean(name="UsuarioBean")
@SessionScoped
public class UsuarioBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private Usuario usuario = new Usuario();
	private String emailLogin = null;
	private String email = null;
	private String login = null;
	private String nome = null;
	private String sobrenome = null;
	private String senha = null;
	private String sexo = null;
	private String token;
	private boolean logado = false;
	private String mensagemErroLogin = null;
	private String mensagemErroAtualizarCadastro = null;
	private boolean atualizarCadastro = false;
	private String nomeMusica = null;

	public UsuarioBean() { }

	public void logar()
	{
		try
		{
			Usuario usuarioRecebido = null;
			if(emailLogin!=null && emailLogin.contains("@") && senha!=null && senha.length()>=6)
			{
				String cript = Criptografia.md5(senha);
				usuarioRecebido = usuarioDAO.validarUsuarioEmail(emailLogin, cript);
			}
			
			if(usuarioRecebido!=null && usuarioRecebido.getPkUsuario()>0)
			{
				this.logado = true;
			}
			
			if(!this.logado)
			{
				usuarioRecebido = null;
				if(emailLogin!=null && emailLogin.length()>=4 && senha!=null && senha.length()>=6)
				{
					String cript = Criptografia.md5(senha);
					usuarioRecebido = usuarioDAO.validarUsuarioLogin(emailLogin, cript);
					
					if(usuarioRecebido!=null && usuarioRecebido.getPkUsuario()>0)
					{
						this.logado = true;
					}
				}
			}
			usuario = new Usuario();
			usuario = usuarioRecebido;
			setUsuarioGlobal(usuario);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void logarTeste()
	{
		try
		{
			 FacebookClient facebookClient = new DefaultFacebookClient(token);
		     User facebookUser = facebookClient.fetchObject("me", User.class);
		     
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
				    	 Usuario usuarioFacebook = usuarioDAO.validarID(facebookUser.getId());
				    	 if(usuarioFacebook!=null && usuarioFacebook.getPkUsuario()>0)
				    	 {
							 usuario = new Usuario();
							 usuario = usuarioFacebook;
							 setUsuarioGlobal(usuarioFacebook);
				    		 this.emailLogin = facebookUser.getEmail();
				    		 this.logado = true;
				    	 }
				    	 else
				    	 {
				    		 if(facebookUser.getEmail()!=null && facebookUser.getEmail().length()>0)
				    		 {
				    			 usuarioFacebook = usuarioDAO.validarEmail(facebookUser.getEmail());
				    			 if(usuarioFacebook!=null && usuarioFacebook.getPkUsuario()>0)
				    			 {
				    				 mensagemErroLogin = "Este email já está cadastrado em outro usuário";
				    			 }
				    			 else
				    			 {
					    			 usuarioFacebook = new Usuario();
					    			 usuarioFacebook.setEmailUsuario(facebookUser.getEmail());
					    			 usuarioFacebook.setIdFacebook(facebookUser.getId());
					    			 usuarioFacebook.setNome(facebookUser.getFirstName());
					    			 usuarioFacebook.setSobrenome(facebookUser.getLastName());
					    			 usuarioFacebook.setLancamento(new Date());
					    			 if(facebookUser.getGender()!=null && facebookUser.getGender().length()>0 && (facebookUser.getGender().equals("male")))
					    			 {
					    				 usuarioFacebook.setSexo(Constantes.TIPO_GENERO_MASCULINO);
					    			 }
					    			 else if(facebookUser.getGender()!=null && facebookUser.getGender().length()>0 && (facebookUser.getGender().equals("female")))
					    			 {
					    				 usuarioFacebook.setSexo(Constantes.TIPO_GENERO_FEMININO);
					    			 }
					    			 save(usuarioFacebook);
					    			 this.emailLogin = facebookUser.getEmail();
									 usuario = new Usuario();
									 usuario = usuarioFacebook;
									 setUsuarioGlobal(usuario);
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
				if(email!=null)
				{
					validaEmail = usuarioDAO.validarEmail(email);
				}
				
				if(!(validaEmail!=null && validaEmail.getPkUsuario()>0))
				{
					validaEmail = null;
					if(login!=null)
					{
						validaEmail = usuarioDAO.validarLogin(login);
						if(!((validaEmail!=null && validaEmail.getPkUsuario()>0)))
						{
							Usuario usuarioSalvo = new Usuario();
							
							usuarioSalvo.setLogin(login);
							usuarioSalvo.setEmailUsuario(email);
							usuarioSalvo.setNome(nome);
							String cript = Criptografia.md5(senha);
							usuarioSalvo.setSenha(cript);
							usuarioSalvo.setNome(nome);
							usuarioSalvo.setSexo(Integer.valueOf(this.sexo));
							usuarioSalvo.setStatus(Constantes.TIPO_STATUS_ATIVO);
							usuarioSalvo.setLancamento(new Date());
							save(usuarioSalvo);
							usuario = new Usuario();
							usuario = usuarioSalvo;
							setUsuarioGlobal(usuario);
							this.emailLogin = usuario.getLogin();
							this.logado = true;
						}
						else
						{
							mensagemErroLogin = "Login informado já existe";
						}
					}
				}
				else
				{
					mensagemErroLogin = "Email informado já foi cadastrado";
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void atualizar()
	{
		try
		{
			mensagemErroAtualizarCadastro = "Senha";
			atualizarCadastro = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void logarAtualizacaoSenha()
	{
		try
		{
			setUsuarioGlobal(usuario);
			this.emailLogin = usuario.getEmailUsuario();
			this.logado = true;
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/index.xhtml");
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
		
		if(login==null || login.length()==0) { erros.add("Login");  }
		if(nome==null || nome.length()==0) { erros.add("Nome");  }
		if(sobrenome==null || sobrenome.length()==0) { erros.add("Sobrenome");  }
		if(email==null || email.length()==0) { erros.add("Email");  }
		if(senha==null || senha.length()==0) { erros.add("Senha");  }
		if(sexo==null || sexo.length()==0) { erros.add("Género");  }
		
		if(erros.size()>1) 								{ return "Os campos " + erros.toString() + " são requeridos." ;	}
		else if(erros.size()==1) 						{ return "O campo " + erros.toString() + " é requerido."; 		}
		
		if(!(email.contains("@")))	{ return "Email informado inválido" ; 							}	
		if(login.length()<4)				{ return "Login deve possuir no mínimo 4 caracteres" ; 			}	
		if(senha.length()<6)				{ return "Senha deve possuir no mínimo 6 caracteres" ; 			}	
		
		usuario = new Usuario();
		
		return "";
	}
	
	public String mudarPagina()
	{
		try
		{
/*			Spotify sp = new Spotify();
			Results<Track> results = sp.searchTrack("Nirvana",  "Come as You Are");
		    for (Track track : results.getItems()) { 
		       System.out.printf("Artista " + track.getArtistName() + "Album" + track.getAlbum() + "Musica " + track.getId()) ;
		    }
		    Results<Artist> artista = sp.searchArtist("Nirvana");
		    System.out.println(artista.getItems().get(0).getName());
		    System.out.println(artista.getItems().get(0).getId());*/
			return "logado?faces-redirect=true";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
		return "";
	}
	
	public void redirecionaEsqueceuSuaSenha()
	{
		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/redefinirSenha/index.xhtml?t=1");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void procurarMusica()
	{
		try
		{
			nomeMusica = null;
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/logado.xhtml");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void redirecionarAlterarCadastro()
	{
		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/cadastro/index.xhtml?t=1");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void deslogar()
	{
		try
		{
			//encerrarSessao();
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/index.xhtml");
			this.logado = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getMensagemErroAtualizarCadastro() {
		return mensagemErroAtualizarCadastro;
	}

	public void setMensagemErroAtualizarCadastro(
			String mensagemErroAtualizarCadastro) {
		this.mensagemErroAtualizarCadastro = mensagemErroAtualizarCadastro;
	}

	public boolean getAtualizarCadastro() {
		return atualizarCadastro;
	}

	public void setAtualizarCadastro(boolean atualizarCadastro) {
		this.atualizarCadastro = atualizarCadastro;
	}

	public String getNomeMusica() {
		return nomeMusica;
	}

	public void setNomeMusica(String nomeMusica) {
		this.nomeMusica = nomeMusica;
	}
}