package br.com.recomusic.im;

import br.com.recomusic.om.Genero;

public class MusicasRecomendadasIM {
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private Genero genero;
	MusicaIM musicaIM;

	/*-*-*-* Construtores *-*-*-*/
	public MusicasRecomendadasIM() {
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public MusicaIM getMusicaIM() {
		return musicaIM;
	}

	public void setMusicaIM(MusicaIM musicaIM) {
		this.musicaIM = musicaIM;
	}
}