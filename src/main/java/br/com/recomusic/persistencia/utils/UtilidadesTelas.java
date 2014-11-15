package br.com.recomusic.persistencia.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.com.recomusic.bean.UsuarioBean;
import br.com.recomusic.dao.InformacaoMusicalCadastroBandaDAO;
import br.com.recomusic.im.BandaGeneroIM;
import br.com.recomusic.om.Banda;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.TrocarFoto;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;

import com.echonest.api.v4.EchoNestAPI;

@ManagedBean(name = "GlobalBean")
@SessionScoped
public class UtilidadesTelas {
	private static Usuario usuarioGlobal;
	private boolean curtiuMusica = false;
	private boolean naoCurtiuMusica = false;
	private static boolean ckMusica;
	private static boolean ckBanda;
	private static String campoNomeMusicaAux;
	private boolean ckMusicaAux;
	private boolean ckBandaAux;
	private String campoNomeMusica;

	protected static final String FILE_PATH = "C://Users//Guilherme//FT";

	protected String FILE_PREFIX = "";

	protected Boolean enableMessage = Boolean.TRUE;
	protected String tokenFacebook;
	EchoNestAPI en = new EchoNestAPI("9QB1EM63CLM2RR5V3");
	private InformacaoMusicalCadastroBandaDAO informacaoMusicalCadastroBandaDAO = new InformacaoMusicalCadastroBandaDAO(
			ConectaBanco.getInstance().getEntityManager());

	public UtilidadesTelas() {
	}

	/**
	 * Verifica a sess�o corrente
	 */
	public static boolean verificarSessao() {
		try {
			if (((UsuarioBean) getBean("UsuarioBean")).getUsuario() != null
					&& ((UsuarioBean) getBean("UsuarioBean")).getUsuario()
							.getPkUsuario() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Verifica se foi uma conex�o Facebook
	 */
	public static String verificarSessaoFacebook() {
		try {
			if (((UsuarioBean) getBean("UsuarioBean")).getGuardaToken() != null
					&& ((UsuarioBean) getBean("UsuarioBean")).getGuardaToken()
							.length() > 0) {
				return ((UsuarioBean) getBean("UsuarioBean")).getGuardaToken();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Responsavel por retornar um Bean
	 */
	public static Object getBean(String ref) {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		ELContext elContext = facesContext.getELContext();
		ExpressionFactory factory = facesContext.getApplication()
				.getExpressionFactory();
		return factory.createValueExpression(elContext, "#{" + ref + "}",
				Object.class).getValue(elContext);
	}

	/**
	 * Responsavel por encerrar a sessao
	 */
	public static void encerrarSessao() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("http://localhost:8080/RecoMusic/index.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Responsavel por enerrar a sessao
	 */
	public static void redirecionarPerfil() {
		try {
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"http://localhost:8080/RecoMusic/perfil/index.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Respons�vel por procurar se a lista de bandas curtidas pelo usu�rio
	 * existem e se elas j� est�o cadastradas nas informa��es de perfil do
	 * usu�rio
	 */

	/**
	 * @author Guilherme Respons�vel por procurar se a lista de bandas curtidas
	 *         pelo usu�rio existem e se elas j� est�o cadastradas nas
	 *         informa��es de perfil do usu�rio
	 * @param listaMusicas
	 *            - lista de BANDAS, existe - se o usu�rio j� existe no sistema
	 * @throws Exception
	 *             return List<BandaGeneroIM>
	 */
	public List<BandaGeneroIM> pesquisaBanda(List<String> listaMusicas,
			boolean existe) throws Exception {
		Banda banda = null;
		// List<Banda> listaBandas = new ArrayList<Banda>();
		List<BandaGeneroIM> listaBGIM = new ArrayList<BandaGeneroIM>();
		BandaGeneroIM bgIM;

		if (existe) {
			List<Banda> listaBandasAux = new ArrayList<Banda>();
			List<Banda> listaBandasAuxUsuario = new ArrayList<Banda>();
			for (String string : listaMusicas) {
				banda = PesquisaMusica.procuraArtista(en, string);
				if (banda != null) {
					listaBandasAux.add(banda);
				}
			}

			listaBandasAuxUsuario = informacaoMusicalCadastroBandaDAO
					.procurarBandasUsuario(getUsuarioGlobal());

			boolean existeBanda = false;
			for (Banda bandaAux : listaBandasAux) {
				for (Banda bandaCadastrada : listaBandasAuxUsuario) {
					if (bandaAux.getIdBanda().equals(
							bandaCadastrada.getIdBanda())) {
						existeBanda = true;
					}
				}

				if (!existeBanda) {
					bgIM = new BandaGeneroIM();
					bgIM.setListaGeneros(PesquisaMusica
							.requisitarGeneroBanda(bandaAux.getIdBanda()));
					bgIM.setBanda(bandaAux);
					listaBGIM.add(bgIM);
					// listaBandas.add(bandaAux);
				}
			}
		} else {
			for (String string : listaMusicas) {
				banda = PesquisaMusica.procuraArtista(en, string);
				if (banda != null) {
					bgIM = new BandaGeneroIM();
					bgIM.setListaGeneros(PesquisaMusica
							.requisitarGeneroBanda(banda.getIdBanda()));
					bgIM.setBanda(banda);
					listaBGIM.add(bgIM);
					// listaBandas.add(banda);
				}
			}
		}

		return listaBGIM;
	}

	/*
	 * Pesquisa uma m�sica na API atrav�s do seu id passado como par�metro
	 * String idMusica return M�sica
	 */
	public Musica pesquisaMusica(String idMusica) throws Exception {
		Musica musica = new Musica();
		musica = PesquisaMusica.procuraMusica(en, idMusica);
		return musica;
	}

	/*
	 * Pesquisa na API (atrav�s do JSON) todos os g�neros de uma determinada
	 * banda String idBanda return List<String> listaGeneros
	 */
	public static List<String> requisitarAPIGeneroBanda(String idBanda)
			throws Exception {
		return PesquisaMusica.requisitarGeneroBanda(idBanda);
	}

	public void redirecionarErro() throws Exception {
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"http://localhost:8080/RecoMusic/resources/ajax/page_500.html");
	}

	public void addMessage(String summary, Severity severityInfo) {
		if (enableMessage) {
			FacesMessage message = new FacesMessage(severityInfo, summary, "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void redirecionaProcuraMusica() {
		try {
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"http://localhost:8080/RecoMusic/procurarMusica/index.xhtml?t="
									+ campoNomeMusica);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Usuario getUsuarioGlobal() {
		return usuarioGlobal;
	}

	public static void setUsuarioGlobal(Usuario usuarioGlobal) {
		UtilidadesTelas.usuarioGlobal = usuarioGlobal;
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

	public Boolean getEnableMessage() {
		return enableMessage;
	}

	public void setEnableMessage(Boolean enableMessage) {
		this.enableMessage = enableMessage;
	}

	public String getTokenFacebook() {
		return tokenFacebook;
	}

	public void setTokenFacebook(String tokenFacebook) {
		this.tokenFacebook = tokenFacebook;
	}

	public void changeCheckBoxMusica() {
		this.ckMusica = this.ckMusicaAux;
	}

	public void changeCheckBoxBanda() {
		this.ckBanda = this.ckBandaAux;
	}

	public static boolean isCkMusica() {
		return ckMusica;
	}

	public static void setCkMusica(boolean ckMusica) {
		ckMusica = ckMusica;
	}

	public static boolean isCkBanda() {
		return ckBanda;
	}

	public static void setCkBanda(boolean ckBanda) {
		ckBanda = ckBanda;
	}

	public boolean isCkMusicaAux() {
		return ckMusicaAux;
	}

	public void setCkMusicaAux(boolean ckMusicaAux) {
		this.ckMusicaAux = ckMusicaAux;
	}

	public boolean isCkBandaAux() {
		return ckBandaAux;
	}

	public void setCkBandaAux(boolean ckBandaAux) {
		this.ckBandaAux = ckBandaAux;
	}

	public String getCampoNomeMusica() {
		return campoNomeMusica;
	}

	public void setCampoNomeMusica(String campoNomeMusica) {
		this.campoNomeMusica = campoNomeMusica;
		setCampoNomeMusicaAux(campoNomeMusicaAux);
	}

	public String getFileName(String fileOriginalName, String mimetype) {

		long id = System.currentTimeMillis();

		int index = fileOriginalName.lastIndexOf(".");
		fileOriginalName = fileOriginalName.substring(0, index);

		String type = "";

		if (mimetype.trim().equals("image/jpeg"))
			type = ".jpg";

		if (mimetype.trim().equals("image/png"))
			type = ".png";

		if (mimetype.trim().equals("image/gif"))
			type = ".gif";

		StringBuffer fileName = new StringBuffer();
		fileName.append(FILE_PREFIX).append(fileOriginalName).append(id)
				.append(type);
		return fileName.toString();
	}
	
	public boolean uploadFile(TrocarFoto trocarFoto) {

		boolean sucess = false;
		File targetFolder = null;

		FacesContext ctx = FacesContext.getCurrentInstance();

		ServletContext servletContext = (ServletContext) ctx
				.getExternalContext().getContext();

		try {

			if (trocarFoto != null
					&& !trocarFoto.getNome().isEmpty()) {

				targetFolder = new File(FILE_PATH);
				InputStream inputStream = trocarFoto.getInputStream();
				File file = new File(targetFolder,trocarFoto.getNome());
				OutputStream out = new FileOutputStream(file);

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}

				inputStream.close();
				out.flush();
				out.close();

				int index = trocarFoto.getNome().lastIndexOf(
						".");
				String thumbName = trocarFoto.getNome()
						.substring(0, index - 1);
				thumbName = thumbName.concat("_similar");

				File thumbFile = ThumbnailGenerator
						.generateThumbnail(file, thumbName,
								trocarFoto.getTipo());

				trocarFoto.setPathFotoImagem(thumbFile.getName());
				trocarFoto.setPathFoto(file.getName());

				sucess = true;

			}
		} catch (Exception e) {

			sucess = false;
		}

		return sucess;
	}

	public static String isCampoNomeMusicaAux() {
		return campoNomeMusicaAux;
	}

	public static void setCampoNomeMusicaAux(String campoNomeMusicaAux) {
		UtilidadesTelas.campoNomeMusicaAux = campoNomeMusicaAux;
	}
	
	public static void deleteFoto(final String fileName, String imagePath) {

		// Decode the file name (might contain spaces and on) and prepare file
		// object.
		File image = new File(imagePath, fileName);
		if (image != null && image.exists()) {
			image.delete();
		}
	}
}
