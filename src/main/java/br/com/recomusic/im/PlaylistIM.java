package br.com.recomusic.im;

import java.util.Date;


public class PlaylistIM
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private int qtd;
	private String nomePlaylist;
	private Date dataLancamento;
	private int qtdFaixas;

	/*-*-*-* Construtores *-*-*-*/
	public PlaylistIM() { }
	
	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public String getNomePlaylist() {
		return nomePlaylist;
	}

	public void setNomePlaylist(String nomePlaylist) {
		this.nomePlaylist = nomePlaylist;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public int getQtdFaixas() {
		return qtdFaixas;
	}

	public void setQtdFaixas(int qtdFaixas) {
		this.qtdFaixas = qtdFaixas;
	}
}