package br.com.recomusic.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

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
	private Part arquivoPart;
	
	public TrocarFotoBean() {
		this.FILE_PREFIX = "DOCUMENTO_";
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

	public void uploadFoto(FileUploadEvent event) throws FileNotFoundException, IOException{
		try {

			ConectaBanco.getInstance().beginTransaction();
			TrocarFoto trocarFoto = new TrocarFoto();
			TrocarFoto trocarFotoRemove = null;
			
			trocarFotoRemove = trocarFotoDAO
					.getTrocarFotoUsuario(getUsuarioGlobal());
			
			if(trocarFotoRemove!=null && trocarFotoRemove.getPkTrocarFoto()>0)
			{
				deleteFoto(trocarFotoRemove.getNome(), FILE_PATH);
				deleteFoto(trocarFotoRemove.getPathFotoImagem(), FILE_PATH);
				trocarFotoDAO.delete(trocarFotoRemove);
			}
			
			String finalname = getFileName(event.getFile().getFileName(), event
					.getFile().getContentType());

			trocarFoto.setNome(finalname);
			trocarFoto.setPathFoto(FILE_PATH + finalname);

			trocarFoto.setTipo(event.getFile().getContentType());
			trocarFoto.setTamanho(String.valueOf(event.getFile().getSize()));
			trocarFoto.setInputStream(event.getFile().getInputstream());
			trocarFoto.setUsuario(getUsuarioGlobal());
			uploadFile(trocarFoto);
			trocarFotoDAO.save(trocarFoto);
			ConectaBanco.getInstance().commit();
			

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


	public Part getArquivoPart() {
		return arquivoPart;
	}


	public void setArquivoPart(Part arquivoPart) {
		this.arquivoPart = arquivoPart;
	}
	
	
}