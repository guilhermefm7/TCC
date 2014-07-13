package br.com.recomusic.im;

import java.util.List;

import br.com.recomusic.om.Banda;

public class BandaGeneroIM
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private Banda Banda;
	private List<String> listaGeneros;

	/*-*-*-* Construtores *-*-*-*/
	public BandaGeneroIM() { }

	public Banda getBanda() { return Banda; }
	public void setBanda(Banda Banda) { this.Banda = Banda; }

	public List<String> getListaGeneros() { return listaGeneros; }
	public void setListaGeneros(List<String> listaGeneros) { this.listaGeneros = listaGeneros; }
}