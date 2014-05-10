package br.com.mresolucoes.atta.modulos.frota.persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.com.mresolucoes.atta.modulos.atta.utils.BDConstantesAtta;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class InformacaoMusicalCadastroMusica implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkInformacaoMusicalCadastroMusica;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkInformacaoMusicalCadastro")
	private InformacaoMusicalCadastro informacaoMusicalCadastro;

	@ManyToOne @JoinColumn(name="fkMusica")
	private Musica musica;


	/*-*-*-* Construtores *-*-*-*/
	public InformacaoMusicalCadastroMusica() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkInformacaoMusicalCadastroMusica() { return pkInformacaoMusicalCadastroMusica; }
	public void setPkInformacaoMusicalCadastroMusica(long pkInformacaoMusicalCadastroMusica) { this.pkInformacaoMusicalCadastroMusica = pkInformacaoMusicalCadastroMusica; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public InformacaoMusicalCadastro getInformacaoMusicalCadastro() { return informacaoMusicalCadastro; }
	public void setInformacaoMusicalCadastro(InformacaoMusicalCadastro informacaoMusicalCadastro) { this.informacaoMusicalCadastro = informacaoMusicalCadastro; }

	public Musica getMusica() { return musica; }
	public void setMusica(Musica musica) { this.musica = musica; }
}