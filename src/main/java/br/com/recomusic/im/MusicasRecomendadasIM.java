package br.com.recomusic.im;

import java.util.List;

import br.com.recomusic.om.Genero;
import br.com.recomusic.om.Musica;

public class MusicasRecomendadasIM {
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private Genero genero;
	List<Musica> listaMusica;

	/*-*-*-* Construtores *-*-*-*/
	public MusicasRecomendadasIM() {
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public List<Musica> getListaMusica() {
		return listaMusica;
	}

	public void setListaMusica(List<Musica> listaMusica) {
		this.listaMusica = listaMusica;
	}
}