package br.com.recomusic.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Criptografia;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;
import br.com.recomusic.singleton.GuardaAlteracoesSenhas;


@ManagedBean(name="AlterarSenhaBean")
@ViewScoped
public class AlterarSenhaBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private String senhaDigitada;
	private String senhaDigitadaNovamente;
	private String falhaSenha = null;
	private String tokenRecebido = null;
	public AlterarSenhaBean() {	}
	
	public void alterarSenha()
	{
		try 
		{
			if(procuraTokenRecebido())
			{
				if(senhaDigitada!=null && senhaDigitada.length()>=6 && senhaDigitadaNovamente!=null && senhaDigitadaNovamente.length()>=6)
				{
					if(senhaDigitada.equals(senhaDigitadaNovamente))
					{
						ConectaBanco.getInstance().beginTransaction();
						
						Usuario usuarioRecente = usuarioDAO.getById(GuardaAlteracoesSenhas.getTokensAlteracaoSenhaUsuario().get(tokenRecebido));
						String cript = Criptografia.md5(senhaDigitada);
						usuarioRecente.setSenha(cript);
						usuarioDAO.salvarUsuario(usuarioRecente);
						ConectaBanco.getInstance().commit();
						falhaSenha = "Senha alterada com sucesso";
						GuardaAlteracoesSenhas.getTokensAlteracaoSenhaUsuario().remove(tokenRecebido);
					}
					else
					{
						falhaSenha = "As senhas informadas devem ser iguais";
					}
				}
				else
				{
					falhaSenha = "Informe a sua nova senha. A senha deve possuir no min�mo 6 caracteres";
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public boolean procuraTokenRecebido()
	{
		try 
		{
			if(GuardaAlteracoesSenhas.getTokensAlteracaoSenhaUsuario().containsKey(tokenRecebido))
			{
				return true;
			}
			
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
			return false;
		}
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

	public String getFalhaSenha() {
		return falhaSenha;
	}

	public void setFalhaSenha(String falhaSenha) {
		this.falhaSenha = falhaSenha;
	}

	public String getTokenRecebido() {
		return tokenRecebido;
	}

	public void setTokenRecebido(String tokenRecebido) {
		this.tokenRecebido = tokenRecebido;
	}
}