package br.com.recomusic.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.recomusic.dao.TrocarFotoDAO;
import br.com.recomusic.om.TrocarFoto;
import br.com.recomusic.persistencia.utils.UtilidadesTelas;
import br.com.recomusic.singleton.ConectaBanco;

@ManagedBean(name = "TrocarFotoBean")
@RequestScoped
public class TrocarFotoBean extends UtilidadesTelas implements Serializable {
	private static final long serialVersionUID = 1L;
	private TrocarFoto trocarFoto = null;
	private String path = "../resources/imagens/img/eleven.png";
	private TrocarFotoDAO trocarFotoDAO = new TrocarFotoDAO(ConectaBanco
			.getInstance().getEntityManager());
	private UploadedFile file;

	public TrocarFotoBean() {
	}


	/**
	 */
	public void iniciar() {
		try {
			if (UtilidadesTelas.verificarSessao()) {
				trocarFoto = trocarFotoDAO
						.getTrocarFotoUsuario(getUsuarioGlobal());
				if (trocarFoto != null && trocarFoto.getPkTrocarFoto() > 0) {
					path = trocarFoto.getPathFoto();
				}
			} else {
				encerrarSessao();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadFoto(FileUploadEvent event) {
		try {

			TrocarFoto trocarFoto = new TrocarFoto();

			String finalname = getFileName(event.getFile().getFileName(), event
					.getFile().getContentType());

			trocarFoto.setNome(FILE_PATH + finalname);

			trocarFoto.setTipo(event.getFile().getContentType());
			trocarFoto.setTamanho(String.valueOf(event.getFile().getSize()));
			trocarFoto.setInputStream(event.getFile().getInputstream());
			trocarFoto.setUsuario(getUsuarioGlobal());

		} catch (Exception e) {
			e.printStackTrace();
			addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}
	public void uploadFoto2() {
		try {

			
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void cancelar() {
		try {
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"http://localhost:8080/RecoMusic/perfil/index.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}

	public TrocarFoto getTrocarFoto() {
		return trocarFoto;
	}

	public void setTrocarFoto(TrocarFoto trocarFoto) {
		this.trocarFoto = trocarFoto;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}