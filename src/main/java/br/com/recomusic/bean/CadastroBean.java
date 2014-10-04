package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Criptografia;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="CadastroBean")
@ViewScoped
public class CadastroBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private String falhaAtualizarCadastro = null;
	private String senhaDigitada = null;
	private String senhaDigitadaNovamente = null;
	private String tokenRecebido = null;
	private Usuario usuarioAux = null;

	public CadastroBean() { }
	
	/**
	 * Responsavel por iniciar a pagina desktop.xhtml, carregando tdo oq ela precisa,
	 * verificando sessao, etc.
	 */
	public void iniciar()
	{
		try
		{
			if(UtilidadesTelas.verificarSessao())
			{
				setUsuarioAux(getUsuarioGlobal());
			}
			else
			{
				encerrarSessao();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void atualizarCadastro()
	{
		try
		{
			falhaAtualizarCadastro = null;
			falhaAtualizarCadastro = verificaConsistencia();
			addMessage(verificaConsistencia(), FacesMessage.SEVERITY_ERROR);
			if(falhaAtualizarCadastro=="" || falhaAtualizarCadastro==null)
			{
				Usuario pegaUsuario = usuarioDAO.getUsuarioPk(getUsuarioGlobal().getPkUsuario());
				if(pegaUsuario!=null && pegaUsuario.getPkUsuario()>0)
				{
					pegaUsuario.setLogin(usuarioAux.getLogin());
					pegaUsuario.setNome(usuarioAux.getNome());
					pegaUsuario.setSobrenome(usuarioAux.getSobrenome());
					
					String cript = Criptografia.md5(senhaDigitada);
					
					pegaUsuario.setSenha(cript);
					
					save(pegaUsuario);
					((UsuarioBean) getBean("UsuarioBean")).setUsuario(pegaUsuario);
					setUsuarioGlobal(pegaUsuario);
					FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/perfil/index.xhtml");
				}
			}
			else
			{
				setUsuarioAux(null);
				setUsuarioAux(getUsuarioGlobal());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String verificaConsistencia() throws Exception
	{
		List<String> erros = new ArrayList<String>();
		
		if(usuarioAux.getNome()==null || usuarioAux.getNome().length()==0) { erros.add("Nome");  }
		if(usuarioAux.getSobrenome()==null || usuarioAux.getSobrenome().length()==0) { erros.add("Sobrenome");  }
		if((senhaDigitada==null || senhaDigitada.length()==0) || 
				(senhaDigitadaNovamente==null || senhaDigitadaNovamente.length()==0)) { erros.add("Senha");  }
		
		if(erros.size()>1) 																{ return "Os campos " + erros.toString() + " são requeridos." ;	}
		else if(erros.size()==1) 														{ return "O campo " + erros.toString() + " é requerido."; 		}
		
		if(!(senhaDigitada.equals(senhaDigitadaNovamente)))								{ return "Senhas diferentes" ; 									}	
		if(senhaDigitada.length()<6 && senhaDigitadaNovamente.length()<6)				{ return "Senha deve possuir no mínimo 6 caracteres" ; 			}
		
		return "";
	}
	
	public void cancelar()
	{
		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/perfil/index.xhtml");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
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

	public String getFalhaAtualizarCadastro() {
		return falhaAtualizarCadastro;
	}

	public void setFalhaAtualizarCadastro(String falhaAtualizarCadastro) {
		this.falhaAtualizarCadastro = falhaAtualizarCadastro;
	}

	public String getSenhaDigitada() {
		return senhaDigitada;
	}

	public void setSenhaDigitada(String senhaDigitada) {
		this.senhaDigitada = senhaDigitada;
	}

	public String getSenhaDigitadaNovamente() {
		return senhaDigitadaNovamente;
	}

	public void setSenhaDigitadaNovamente(String senhaDigitadaNovamente) {
		this.senhaDigitadaNovamente = senhaDigitadaNovamente;
	}
	
	public String getTokenRecebido() {
		return tokenRecebido;
	}

	public void setTokenRecebido(String tokenRecebido) {
		this.tokenRecebido = tokenRecebido;
	}

	public Usuario getUsuarioAux() {
		return usuarioAux;
	}

	public void setUsuarioAux(Usuario usuarioAux) {
		this.usuarioAux = usuarioAux;
	}
}