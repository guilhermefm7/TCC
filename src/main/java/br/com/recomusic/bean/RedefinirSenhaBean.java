package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.EnviarEmail;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;
import br.com.recomusic.singleton.GuardaAlteracoesSenhas;


@ManagedBean(name="RedefinirSenhaBean")
@ViewScoped
public class RedefinirSenhaBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private String email;
	private String falhaFormulario = null;
	private EnviarEmail ee = new EnviarEmail();
	private String tokenRecebido = null;
	public RedefinirSenhaBean() {	}

	public void iniciar()
	{
		try
		{
			if( !(tokenRecebido!=null && tokenRecebido.length()>0 && tokenRecebido.equals("2"))  )
			{
				email = null;
				falhaFormulario = null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public void enviarSenha()
	{
		try 
		{
			falhaFormulario = "";
			if(email!=null && email.length()>0)
			{
				if(email.contains("@"))
				{
					Usuario validaEmailUsuario = null;
					validaEmailUsuario = usuarioDAO.validarEmail(email);
					if(validaEmailUsuario!=null && validaEmailUsuario.getPkUsuario()>0)
					{
						String tokenSenha = Long.toString(((new Date()).getTime()));
						tokenSenha +=  Long.toString(validaEmailUsuario.getPkUsuario());
						
						//Insere o Token no HashMap
						GuardaAlteracoesSenhas.getTokensAlteracaoSenhaUsuario().put(tokenSenha, validaEmailUsuario.getPkUsuario());
						
						String getMsg = "http://localhost:8080/RecoMusic/alterarSenha/index.xhtml?t=" + tokenSenha;
						String assunto = "Solicitação de alteração de senha do usuário " + validaEmailUsuario.getLogin() + "";
						String mensagem = "<a href=\"" + getMsg + "\">Clique aqui</a> para redefinir sua senha";
						
						ee.enviaEmail(email, assunto, mensagem, validaEmailUsuario.getLogin());
						falhaFormulario = "Email de redefinição de senha foi enviado a sua caixa de email";
						FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/RecoMusic/redefinirSenha/index.xhtml?t=2");
					}
					else
					{
						falhaFormulario = "Email informado não está cadastrado em nosso banco de dados";
					}
				}
				else
				{
					falhaFormulario = "Informe um email correto";
				}
			}
			else
			{
				falhaFormulario = "Informe um email para que seja enviado a redefinição de senha";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}
	
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getFalhaFormulario()
	{
		return falhaFormulario;
	}
	public void setFalhaFormulario(String falhaFormulario)
	{
		this.falhaFormulario = falhaFormulario;
	}

	public String getTokenRecebido() {
		return tokenRecebido;
	}

	public void setTokenRecebido(String tokenRecebido) {
		this.tokenRecebido = tokenRecebido;
	}
}