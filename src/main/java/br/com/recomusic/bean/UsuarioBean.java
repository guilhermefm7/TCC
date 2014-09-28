package br.com.recomusic.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.recomusic.dao.BandaDAO;
import br.com.recomusic.dao.BandaGeneroDAO;
import br.com.recomusic.dao.GeneroDAO;
import br.com.recomusic.dao.InformacaoMusicalCadastroBandaDAO;
import br.com.recomusic.dao.InformacaoMusicalCadastroGeneroDAO;
import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.im.BandaGeneroIM;
import br.com.recomusic.om.Banda;
import br.com.recomusic.om.BandaGenero;
import br.com.recomusic.om.Genero;
import br.com.recomusic.om.InformacaoMusicalCadastroBanda;
import br.com.recomusic.om.InformacaoMusicalCadastroGenero;
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
	private BandaDAO bandaDAO = new BandaDAO( ConectaBanco.getInstance().getEntityManager());
	private GeneroDAO generoDAO = new GeneroDAO( ConectaBanco.getInstance().getEntityManager());
	private BandaGeneroDAO bandaGeneroDAO = new BandaGeneroDAO( ConectaBanco.getInstance().getEntityManager());
	private InformacaoMusicalCadastroBandaDAO informacaoMusicalCadastroBandaDAO = new InformacaoMusicalCadastroBandaDAO( ConectaBanco.getInstance().getEntityManager());
	private InformacaoMusicalCadastroGeneroDAO informacaoMusicalCadastroGeneroDAO = new InformacaoMusicalCadastroGeneroDAO( ConectaBanco.getInstance().getEntityManager());
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
	private String guardaToken = null;

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
				usuario = new Usuario();
				usuario = usuarioRecebido;
				setUsuarioGlobal(usuario);
				this.logado = true;
				FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/recomendacao/index.xhtml");
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
						usuario = new Usuario();
						usuario = usuarioRecebido;
						setUsuarioGlobal(usuario);
						this.logado = true;
						FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/recomendacao/index.xhtml");
					}
				}
			}
			
			//FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/recomendacao/index.xhtml");
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
				setTokenFacebook(token);
				guardaToken = token;
				FacesContext facesContext = FacesContext.getCurrentInstance();
				if (!facesContext.isPostback() && !facesContext.isValidationFailed())
				{
					List<String> listaMusicas = new ArrayList<String>();
					 FacebookClient facebookClient = new DefaultFacebookClient(token);
				     User facebookUser = facebookClient.fetchObject("me", User.class);
				     if(facebookUser!=null && facebookUser.getId().length()>0)
				     {
				    	 Usuario usuarioFacebook = usuarioDAO.validarID(facebookUser.getId());
				    	 if(usuarioFacebook!=null && usuarioFacebook.getPkUsuario()>0)
				    	 {
				    		 Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
							 if(myFriends.getData()!=null && myFriends.getData().size()>0)
							 {
								 for (int i = 0; i < myFriends.getData().size(); i++)
								 {
									 System.out.println("Amigos " + myFriends.getData().get(i).getName());
									 System.out.println("Amigos " +  myFriends.getData().get(i).getId());
								 }
							 }
				    		 
							 Connection<NamedFacebookType> musics = facebookClient.fetchConnection("me/music", NamedFacebookType.class);
							 if(musics.getData()!=null && musics.getData().size()>0)
							 {
								 for (int i = 0; i < musics.getData().size(); i++)
								 {
									 listaMusicas.add(musics.getData().get(i).getName());
									 System.out.println(musics.getData().get(i).getName());
								 }
							 }
							 
							 //Setado para ser utilizado na pesquisa de Bandas
				    		 this.emailLogin = facebookUser.getEmail();
							 usuario = new Usuario();
							 usuario = usuarioFacebook;
							 setUsuarioGlobal(usuario);
							 
							 List<BandaGeneroIM> listaBGIM = null;
							 listaBGIM = pesquisaBanda(listaMusicas, true);
							 Genero getGenero;
							 Banda getBanda;
							 BandaGenero getBandaGenero;
							 InformacaoMusicalCadastroGenero imcg;
							 InformacaoMusicalCadastroBanda imcb;
							 
							 if(listaBGIM!=null && listaBGIM.size()>0)
							 {
								 ConectaBanco.getInstance().beginTransaction();
								 for (BandaGeneroIM bandaGeneroIM : listaBGIM)
								 {
									 getBanda = null;
									 getBanda = bandaDAO.pesquisarBandaExiste(bandaGeneroIM.getBanda().getIdBanda());
									 
									 if(getBanda==null)
									 {
										 getBanda = new Banda();
										 getBanda = bandaGeneroIM.getBanda();
										 bandaDAO.salvarBanda(getBanda);
									 }
									 
									 imcb = null;
									 imcb = informacaoMusicalCadastroBandaDAO.pesquisarIMCB(usuarioFacebook, getBanda);
									 
									 if(imcb == null )
									 {
										 imcb = new InformacaoMusicalCadastroBanda();
										 imcb.setBanda(getBanda);
										 imcb.setUsuario(usuarioFacebook);
										 imcb.setStatus(Constantes.STATUS_BANDA_CURTIDA_FACEBOOK);
										 informacaoMusicalCadastroBandaDAO.salvarBandasCadastro(imcb);
									 }
									 else
									 {
										 if(imcb.getStatus()!=Constantes.STATUS_BANDA_CURTIDA_FACEBOOK)
										 {
											 imcb.setStatus(Constantes.STATUS_BANDA_CURTIDA_FACEBOOK);
											 informacaoMusicalCadastroBandaDAO.salvarBandasCadastro(imcb);
										 }
									 }
									 
									 for (String nomeGenero : bandaGeneroIM.getListaGeneros())
									 {
										 getGenero = null;
										 getGenero = generoDAO.pesquisarGenero(nomeGenero);
										 
										 if(getGenero==null)
										 {
											 getGenero = new Genero();
											 getGenero.setNomeGenero(nomeGenero);
											 getGenero = generoDAO.salvaListaGeneros(nomeGenero);
										 }
										 
										 getBandaGenero = null;
										 getBandaGenero = bandaGeneroDAO.pesquisarBandaGenero(getBanda, getGenero);
										 
										 if(getBandaGenero==null)
										 {
											 getBandaGenero = new BandaGenero();
											 getBandaGenero.setBanda(getBanda);
											 getBandaGenero.setGenero(getGenero);
											 bandaGeneroDAO.salvarBandaGenero(getBandaGenero);
										 }
										 
										 imcg = null;
										 imcg = informacaoMusicalCadastroGeneroDAO.pesquisarIMCG(usuarioFacebook, getGenero);
										 if(imcg == null)
										 {
											 imcg = new InformacaoMusicalCadastroGenero();
											 imcg.setUsuario(usuarioFacebook);
											 imcg.setGenero(getGenero);
											 imcg.setStatus(Constantes.STATUS_GENERO_CURTIDO_FACEBOOK);
											 informacaoMusicalCadastroGeneroDAO.salvarIMCG(imcg);
										 }
										 else
										 {
											 if(imcg.getStatus()!=Constantes.STATUS_GENERO_CURTIDO_FACEBOOK)
											 {
												 imcg.setStatus(Constantes.STATUS_GENERO_CURTIDO_FACEBOOK);
												 informacaoMusicalCadastroGeneroDAO.salvarIMCG(imcg);
											 }
										 }
									 }
								 }
								 
								 ConectaBanco.getInstance().commit();
							 }
							 
				    		 this.logado = true;
				    		 FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/recomendacao/index.xhtml");
				    	 }
				    	 else
				    	 {
				    		 if(facebookUser.getEmail()!=null && facebookUser.getEmail().length()>0)
				    		 {
				    			 usuarioFacebook = usuarioDAO.validarEmail(facebookUser.getEmail());
				    			 if(usuarioFacebook!=null && usuarioFacebook.getPkUsuario()>0)
				    			 {
				    				 mensagemErroLogin = "Este email já está cadastrado em outro usuário";
				    				 addMessage("Este email já está cadastrado em outro usuário", FacesMessage.SEVERITY_ERROR);
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

					    			 
									 Connection<NamedFacebookType> musics = facebookClient.fetchConnection("me/music", NamedFacebookType.class);
									 if(musics.getData()!=null && musics.getData().size()>0)
									 {
										 for (int i = 0; i < musics.getData().size(); i++)
										 {
											 listaMusicas.add(musics.getData().get(i).getName());
											 System.out.println(musics.getData().get(i).getName());
										 }
									 }
									 
					    			 this.emailLogin = facebookUser.getEmail();
									 usuario = new Usuario();
									 usuario = usuarioFacebook;
									 setUsuarioGlobal(usuario);
									 
									 List<BandaGeneroIM> listaBGIM = null;
									 listaBGIM = pesquisaBanda(listaMusicas, false);
									 Genero getGenero;
									 Banda getBanda;
									 BandaGenero getBandaGenero;
									 InformacaoMusicalCadastroGenero imcg;
									 InformacaoMusicalCadastroBanda imcb;
									 
									 if(listaBGIM!=null && listaBGIM.size()>0)
									 {
										 ConectaBanco.getInstance().beginTransaction();
										 for (BandaGeneroIM bandaGeneroIM : listaBGIM)
										 {
											 getBanda = null;
											 getBanda = bandaDAO.pesquisarBandaExiste(bandaGeneroIM.getBanda().getIdBanda());
											 
											 if(getBanda==null)
											 {
												 getBanda = new Banda();
												 getBanda = bandaGeneroIM.getBanda();
												 bandaDAO.salvarBanda(getBanda);
											 }
											 
											 imcb = null;
											 imcb = informacaoMusicalCadastroBandaDAO.pesquisarIMCB(usuarioFacebook, getBanda);
											 
											 if(imcb == null )
											 {
												 imcb = new InformacaoMusicalCadastroBanda();
												 imcb.setBanda(getBanda);
												 imcb.setUsuario(usuarioFacebook);
												 imcb.setStatus(Constantes.STATUS_BANDA_CURTIDA_FACEBOOK);
												 informacaoMusicalCadastroBandaDAO.salvarBandasCadastro(imcb);
											 }
											 else
											 {
												 if(imcb.getStatus()!=Constantes.STATUS_BANDA_CURTIDA_FACEBOOK)
												 {
													 imcb.setStatus(Constantes.STATUS_BANDA_CURTIDA_FACEBOOK);
													 informacaoMusicalCadastroBandaDAO.salvarBandasCadastro(imcb);
												 }
											 }
											 
											 
											 for (String nomeGenero : bandaGeneroIM.getListaGeneros())
											 {
												 getGenero = null;
												 getGenero = generoDAO.pesquisarGenero(nomeGenero);
												 
												 if(getGenero==null)
												 {
													 getGenero = new Genero();
													 getGenero.setNomeGenero(nomeGenero);
													 getGenero = generoDAO.salvaListaGeneros(nomeGenero);
												 }
												 
												 getBandaGenero = null;
												 getBandaGenero = bandaGeneroDAO.pesquisarBandaGenero(getBanda, getGenero);
												 
												 if(getBandaGenero==null)
												 {
													 getBandaGenero = new BandaGenero();
													 getBandaGenero.setBanda(getBanda);
													 getBandaGenero.setGenero(getGenero);
													 bandaGeneroDAO.salvarBandaGenero(getBandaGenero);
												 }
												 
												 imcg = null;
												 imcg = informacaoMusicalCadastroGeneroDAO.pesquisarIMCG(usuarioFacebook, getGenero);
												 if(imcg == null)
												 {
													 imcg = new InformacaoMusicalCadastroGenero();
													 imcg.setUsuario(usuarioFacebook);
													 imcg.setGenero(getGenero);
													 imcg.setStatus(Constantes.STATUS_GENERO_CURTIDO_FACEBOOK);
													 informacaoMusicalCadastroGeneroDAO.salvarIMCG(imcg);
												 }
												 else
												 {
													 if(imcg.getStatus()!=Constantes.STATUS_GENERO_CURTIDO_FACEBOOK)
													 {
														 imcg.setStatus(Constantes.STATUS_GENERO_CURTIDO_FACEBOOK);
														 informacaoMusicalCadastroGeneroDAO.salvarIMCG(imcg);
													 }
												 }
											 }
										 }
										 ConectaBanco.getInstance().commit();
									 }
									 
					    			 this.logado = true;
					    			 FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/recomendacao/index.xhtml");
				    			 }
				    		 }
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

			addMessage(mensagemErroLogin, FacesMessage.SEVERITY_ERROR);
			
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
							usuarioSalvo.setSobrenome(sobrenome);
							usuarioSalvo.setSexo(Integer.valueOf(this.sexo));
							usuarioSalvo.setStatus(Constantes.TIPO_STATUS_ATIVO);
							usuarioSalvo.setLancamento(new Date());
							save(usuarioSalvo);
							usuario = new Usuario();
							usuario = usuarioSalvo;
							setUsuarioGlobal(usuario);
							this.emailLogin = usuario.getLogin();
							this.logado = true;
							
							FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/recomendacao/index.xhtml");
						}
						else
						{
							mensagemErroLogin = "Login informado já existe";
							addMessage("Login informado já existe", FacesMessage.SEVERITY_ERROR);
						}
					}
				}
				else
				{
					mensagemErroLogin = "Email informado já foi cadastrado";
					addMessage("Email informado já foi cadastrado", FacesMessage.SEVERITY_ERROR);
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
			addMessage("Senha", FacesMessage.SEVERITY_ERROR);
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
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/recomendacao/index.xhtml");
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
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/redefinirSenha/index.xhtml");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void redirecionaPerfil()
	{
		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/perfil/index.xhtml");
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
			String nomeMusicaEscolhida = nomeMusica;
			nomeMusica = null;
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/procurarMusica/index.xhtml");
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
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/cadastro/index.xhtml");
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
			setUsuario(null);
			setUsuarioGlobal(null);
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

	public String getGuardaToken() {
		return guardaToken;
	}

	public void setGuardaToken(String guardaToken) {
		this.guardaToken = guardaToken;
	}
}