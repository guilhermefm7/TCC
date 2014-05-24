package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.Date;
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
	private long pkUsuario;
	private String emailUsuario;
	private String login;
	private String senha;
	private String nome;
	private String sobrenome;
	private Date lancamento;
	private Date dataNascimento;
	private int sexo;
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
			Usuario usuario = usuarioDAO.validarUsuario(emailUsuario, senha);
			if(usuario!=null && usuario.getPkUsuario()>0)
			{
				this.logado = true;
			}
			else
			{
				this.logado = false;
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
			if(emailUsuario.contains("@"))
			{
				this.logado = true;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

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

	public Date getLancamento() {
		return lancamento;
	}

	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public int getSexo() {
		return sexo;
	}

	public void setSexo(int sexo) {
		this.sexo = sexo;
	}
}