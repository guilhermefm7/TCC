package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="MusicaBean")
@ViewScoped
public class MusicaBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private Usuario usuario = null;
	private String valorIdMusica = null;

	public MusicaBean() { }
	
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
				setUsuario(getUsuarioGlobal());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void avaliarMusica()
	{
		try
		{
			System.out.println(valorIdMusica);
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getValorIdMusica() {
		return valorIdMusica;
	}

	public void setValorIdMusica(String valorIdMusica){
		this.valorIdMusica = valorIdMusica;
	}
}