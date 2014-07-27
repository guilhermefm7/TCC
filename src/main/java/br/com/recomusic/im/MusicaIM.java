package br.com.recomusic.im;


public class MusicaIM
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private int qtd;
	private String idMusica;
	private String nomeMusica;
	private String nomeArtista;
	private String albumMusica;
	private String idDeezer;

	/*-*-*-* Construtores *-*-*-*/
	public MusicaIM() { }

	public MusicaIM(int qtd, String nomeMusica, String nomeArtista, String albumMusica, String idDeezer)
	{
		this.qtd = qtd;
		this.nomeMusica = nomeMusica;
		this.nomeArtista = nomeArtista;
		this.albumMusica = albumMusica;
		this.idDeezer = idDeezer;
	}
	
	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public String getIdMusica() {
		return idMusica;
	}

	public void setIdMusica(String idMusica) {
		this.idMusica = idMusica;
	}

	public String getNomeMusica() {
		return nomeMusica;
	}

	public void setNomeMusica(String nomeMusica) {
		this.nomeMusica = nomeMusica;
	}

	public String getNomeArtista() {
		return nomeArtista;
	}

	public void setNomeArtista(String nomeArtista) {
		this.nomeArtista = nomeArtista;
	}

	public String getAlbumMusica() {
		return albumMusica;
	}

	public void setAlbumMusica(String albumMusica) {
		this.albumMusica = albumMusica;
	}

	public String getIdDeezer() {
		return idDeezer;
	}

	public void setIdDeezer(String idDeezer) {
		this.idDeezer = idDeezer;
	}
}