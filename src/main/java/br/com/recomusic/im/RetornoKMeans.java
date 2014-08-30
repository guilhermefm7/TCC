package br.com.recomusic.im;

public class RetornoKMeans {
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private double[][] centroides;
	double[][] divisaoBlocos;
	int tamanho;

	/*-*-*-* Construtores *-*-*-*/
	public RetornoKMeans() {
	}

	public RetornoKMeans(double[][] centroides, double[][] divisaoBlocos, int tamanho) {
		this.centroides = centroides;
		this.divisaoBlocos = divisaoBlocos;
		this.tamanho = tamanho;
	}

	public double[][] getCentroides() {
		return centroides;
	}

	public void setCentroides(double[][] centroides) {
		this.centroides = centroides;
	}

	public double[][] getDivisaoBlocos() {
		return divisaoBlocos;
	}

	public void setDivisaoBlocos(double[][] divisaoBlocos) {
		this.divisaoBlocos = divisaoBlocos;
	}
	
	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
}