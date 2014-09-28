package br.com.recomusic.im;

import br.com.recomusic.om.Musica;

public class MusicaAvaliadaIM {
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private int qtd;
	private Musica musica;
	private int nota;

	/*-*-*-* Construtores *-*-*-*/
	public MusicaAvaliadaIM() {
	}

	public MusicaAvaliadaIM(int qtd, Musica musica, int nota) {
		this.qtd = qtd;
		this.musica = musica;
		this.nota = nota;
	}

	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public Musica getMusica() {
		return musica;
	}

	public void setMusica(Musica musica) {
		this.musica = musica;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}
}