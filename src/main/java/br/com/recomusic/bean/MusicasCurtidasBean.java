package br.com.recomusic.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.recomusic.dao.AvaliarMusicaDAO;
import br.com.recomusic.dao.MusicaDAO;
import br.com.recomusic.im.MusicaAvaliadaIM;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;

@ManagedBean(name = "MusicasCurtidasBean")
@ViewScoped
public class MusicasCurtidasBean extends UtilidadesTelas implements
		Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> listaNomesMusica = null;
	private List<MusicaAvaliadaIM> listaMusicas = null;
	private MusicaDAO musicaDAO = new MusicaDAO(ConectaBanco.getInstance()
			.getEntityManager());
	private AvaliarMusicaDAO avaliarMusicaDAO = new AvaliarMusicaDAO(
			ConectaBanco.getInstance().getEntityManager());
	private int qtdFaixas = 0;

	public MusicasCurtidasBean() {
	}

	public void iniciar() {
		try {
			if (UtilidadesTelas.verificarSessao()) {
				setUsuarioGlobal(getUsuarioGlobal());
				listaMusicas = null;
				listaMusicas = avaliarMusicaDAO
						.getAvaliacoesUsuario(getUsuarioGlobal());
				if (listaMusicas != null && listaMusicas.size() > 0) {
					this.qtdFaixas = listaMusicas.get(listaMusicas.size() - 1)
							.getQtd();
				}
				else
				{
					this.qtdFaixas = 0;
				}
			} else {
				encerrarSessao();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public void redirecionaPaginaMusica(String idMusica, String nomeMusica,
			String artistaBandaMusica, String album, String idEcho) {
		try {
			if (album != null && album.length() > 0) {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ idMusica + "&m=" + nomeMusica + "&a="
										+ artistaBandaMusica + "&i=" + idEcho
										+ "&n=" + album);
			} else {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								"http://localhost:8080/RecoMusic/musica/index.xhtml?t="
										+ idMusica + "&m=" + nomeMusica + "&a="
										+ artistaBandaMusica + "&i=" + idEcho);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public List<String> getListaNomesMusica() {
		return listaNomesMusica;
	}

	public void setListaNomesMusica(List<String> listaNomesMusica) {
		this.listaNomesMusica = listaNomesMusica;
	}

	public int getQtdFaixas() {
		return qtdFaixas;
	}

	public void setQtdFaixas(int qtdFaixas) {
		this.qtdFaixas = qtdFaixas;
	}

	public MusicaDAO getMusicaDAO() {
		return musicaDAO;
	}

	public void setMusicaDAO(MusicaDAO musicaDAO) {
		this.musicaDAO = musicaDAO;
	}

	public AvaliarMusicaDAO getAvaliarMusicaDAO() {
		return avaliarMusicaDAO;
	}

	public void setAvaliarMusicaDAO(AvaliarMusicaDAO avaliarMusicaDAO) {
		this.avaliarMusicaDAO = avaliarMusicaDAO;
	}

	public List<MusicaAvaliadaIM> getListaMusicas() {
		return listaMusicas;
	}

	public void setListaMusicas(List<MusicaAvaliadaIM> listaMusicas) {
		this.listaMusicas = listaMusicas;
	}
}