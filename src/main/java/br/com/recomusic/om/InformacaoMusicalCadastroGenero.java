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
public class InformacaoMusicalCadastroGenero implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkInformacaoMusicalCadastroGenero;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkInformacaoMusicalCadastro")
	private InformacaoMusicalCadastro informacaoMusicalCadastro;

	@ManyToOne @JoinColumn(name="fkGenero")
	private Genero genero;


	/*-*-*-* Construtores *-*-*-*/
	public InformacaoMusicalCadastroGenero() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkInformacaoMusicalCadastroGenero() { return pkInformacaoMusicalCadastroGenero; }
	public void setPkInformacaoMusicalCadastroGenero(long pkInformacaoMusicalCadastroGenero) { this.pkInformacaoMusicalCadastroGenero = pkInformacaoMusicalCadastroGenero; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public InformacaoMusicalCadastro getInformacaoMusicalCadastro() { return informacaoMusicalCadastro; }
	public void setInformacaoMusicalCadastro(InformacaoMusicalCadastro informacaoMusicalCadastro) { this.informacaoMusicalCadastro = informacaoMusicalCadastro; }

	public Genero getGenero() { return genero; }
	public void setGenero(Genero genero) { this.genero = genero; }
}