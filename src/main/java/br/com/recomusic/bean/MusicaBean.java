package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.MusicaDAO;
import br.com.recomusic.dao.UsuarioDAO;
import br.com.recomusic.om.AvaliarMusica;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;


@ManagedBean(name="MusicaBean")
@ViewScoped
public class MusicaBean extends UtilidadesTelas implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO = new UsuarioDAO( ConectaBanco.getInstance().getEntityManager());
	private MusicaDAO musicaDAO = new MusicaDAO( ConectaBanco.getInstance().getEntityManager());
	private AvaliarMusicaDAO avaliarMusicaDAO = new AvaliarMusicaDAO( ConectaBanco.getInstance().getEntityManager());
	private Usuario usuario = null;
	private String valorIdMusica = null;
	private boolean curtiuMusica = false;
	private boolean naoCurtiuMusica = false;
	AvaliarMusica avaliarMusicaPrincipal = null;
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
	
	public void avaliarMusicaPositivamente()
	{
		try
		{
			Musica musica = pesquisaMusica(valorIdMusica);
			if(musica!=null)
			{
				ConectaBanco.getInstance().beginTransaction();
				musicaDAO.save(musica);
				ConectaBanco.getInstance().commit();
			}
			
			ConectaBanco.getInstance().beginTransaction();
			AvaliarMusica am = avaliarMusicaDAO.getAvaliacaoUsuario(musica, getUsuarioGlobal(), 1);
			ConectaBanco.getInstance().commit();
			avaliarMusicaPrincipal = am;
			curtiuMusica = !curtiuMusica;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void avaliarMusicaNegativamente()
	{
		try
		{
			Musica musica = pesquisaMusica(valorIdMusica);
			if(musica!=null)
			{
				ConectaBanco.getInstance().beginTransaction();
				musicaDAO.save(musica);
				ConectaBanco.getInstance().commit();
			}
			
			ConectaBanco.getInstance().beginTransaction();
			AvaliarMusica am = avaliarMusicaDAO.getAvaliacaoUsuario(musica, getUsuarioGlobal(), 2);
			ConectaBanco.getInstance().commit();
			avaliarMusicaPrincipal = am;
			naoCurtiuMusica = !naoCurtiuMusica;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void pesquisaCurtiu()
	{
		try
		{
			Boolean resposta = avaliarMusicaDAO.pesquisaAvaliacaoUsuario(valorIdMusica, getUsuarioGlobal());
			if(resposta!=null)
			{
				if(resposta)
				{
					avaliarMusicaPrincipal.setResposta(true);
					curtiuMusica = true;
					naoCurtiuMusica = false;
				}
				else if(!resposta)
				{
					curtiuMusica = false;
					naoCurtiuMusica = true;
				}
			}
			else
			{
				curtiuMusica = false;
				naoCurtiuMusica = false;
			}
		}
		catch(Exception e)
		{
			ConectaBanco.getInstance().rollBack();
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

	public boolean isCurtiuMusica() {
		return curtiuMusica;
	}

	public void setCurtiuMusica(boolean curtiuMusica) {
		this.curtiuMusica = curtiuMusica;
	}

	public boolean isNaoCurtiuMusica() {
		return naoCurtiuMusica;
	}

	public void setNaoCurtiuMusica(boolean naoCurtiuMusica) {
		this.naoCurtiuMusica = naoCurtiuMusica;
	}

	public AvaliarMusica getAvaliarMusicaPrincipal() {
		return avaliarMusicaPrincipal;
	}

	public void setAvaliarMusicaPrincipal(AvaliarMusica avaliarMusicaPrincipal) {
		this.avaliarMusicaPrincipal = avaliarMusicaPrincipal;
	}
}