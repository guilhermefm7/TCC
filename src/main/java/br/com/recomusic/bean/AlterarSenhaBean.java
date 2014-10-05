package br.com.recomusic.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Criptografia;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;
import br.com.recomusic.singleton.GuardaAlteracoesSenhas;

@ManagedBean(name = "AlterarSenhaBean")
@ViewScoped
public class AlterarSenhaBean extends UtilidadesTelas implements Serializable {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private String senhaDigitada;
	private String senhaDigitadaNovamente;
	private String falhaSenha = null;
	private String tokenRecebido = null;
	private Boolean redefinicao = null;

	public AlterarSenhaBean() {
	}

	public void iniciar() {
		try {
			if (!UtilidadesTelas.verificarSessao()) {
				if (tokenRecebido != null && tokenRecebido.length() > 0) {
					if (getUsuarioGlobal() == null) {
						if (!procuraTokenRecebido()) {
							redefinicao = false;
							addMessage(
									"Página de redefinição de senha expirada. Favor pedir a redefinição de senha novamente",
									FacesMessage.SEVERITY_ERROR);
						} else {
							redefinicao = true;
						}
					} else if (getUsuarioGlobal() != null
							&& getUsuarioGlobal().getPkUsuario() > 0) {
						// Redirecionar para alguma página (Criar Lógica)
						redirecionarErro();
						tokenRecebido = null;
					}
				}
				else
				{
					redirecionarErro();
				}
			} else {
				redirecionarPerfil();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void alterarSenha() {
		try {
			if (procuraTokenRecebido()) {
				if (senhaDigitada != null && senhaDigitada.length() >= 6
						&& senhaDigitadaNovamente != null
						&& senhaDigitadaNovamente.length() >= 6) {
					if (senhaDigitada.equals(senhaDigitadaNovamente)) {
						ConectaBanco.getInstance().beginTransaction();

						Usuario usuarioRecente = usuarioDAO
								.getById(GuardaAlteracoesSenhas
										.getTokensAlteracaoSenhaUsuario().get(
												tokenRecebido));
						String cript = Criptografia.md5(senhaDigitada);
						usuarioRecente.setSenha(cript);
						usuarioDAO.salvarUsuario(usuarioRecente);
						ConectaBanco.getInstance().commit();
						falhaSenha = "Senha alterada com sucesso";
						addMessage("Senha alterada com sucesso",
								FacesMessage.SEVERITY_INFO);
						GuardaAlteracoesSenhas.getTokensAlteracaoSenhaUsuario()
								.remove(tokenRecebido);
						UsuarioBean usuarioBean = ((UsuarioBean) getBean("UsuarioBean"));
						usuarioBean.setUsuario(usuarioRecente);
						usuarioBean.logarAtualizacaoSenha();
					} else {
						falhaSenha = "As senhas informadas devem ser iguais";
						addMessage("As senhas informadas devem ser iguais",
								FacesMessage.SEVERITY_ERROR);
					}
				} else {
					falhaSenha = "Informe a sua nova senha. A senha deve possuir no minímo 6 caracteres";
					addMessage(
							"Informe a sua nova senha. A senha deve possuir no minímo 6 caracteres",
							FacesMessage.SEVERITY_ERROR);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public boolean procuraTokenRecebido() {
		try {
			if (GuardaAlteracoesSenhas.getTokensAlteracaoSenhaUsuario()
					.containsKey(tokenRecebido)) {
				return true;
			}

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
			return false;
		}
	}

	public Boolean getRedefinicao() {
		return redefinicao;
	}

	public void setRedefinicao(Boolean redefinicao) {
		this.redefinicao = redefinicao;
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