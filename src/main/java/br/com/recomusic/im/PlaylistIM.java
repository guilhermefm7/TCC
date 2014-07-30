package br.com.recomusic.im;



public class PlaylistIM
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private String pkPlaylist;
	private int qtd;
	private String nomePlaylist;
	private String dataLancamento;
	private int qtdFaixas;

	/*-*-*-* Construtores *-*-*-*/
	public PlaylistIM() { }
	
	public String getPkPlaylist() {
		return pkPlaylist;
	}

	public void setPkPlaylist(String pkPlaylist) {
		this.pkPlaylist = pkPlaylist;
	}

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

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public int getQtdFaixas() {
		return qtdFaixas;
	}

	public void setQtdFaixas(int qtdFaixas) {
		this.qtdFaixas = qtdFaixas;
	}
}