package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.AvaliarMusica;
import br.com.recomusic.om.Genero;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.MusicaGenero;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class MusicaGeneroDAO extends GenericDAO<Long, MusicaGenero>
{
    public MusicaGeneroDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
	/**
	 * Autor: Guilherme
	 * Dado uma lista das músicas avalidas pelo usuário com nota maior que 3, busca as que são do gênero passado como parâmetro
	 * Genero genero
	 * List<AvaliarMusica> caso exista senão retorna null
	 */
	public List<Musica> pesquisaGeneroMusicasAvaliadas(List<AvaliarMusica> am, Genero genero) throws Exception
	{
		try
		{
			List<Musica> listaM = new ArrayList<Musica>();
			MusicaGenero mg;
			for (AvaliarMusica avaliarMusica : am)
			{
				Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.MusicaGenero as mg where mg.musica.pkMusica = :pk_musica AND mg.genero.pkGenero =:pk_genero"));
				query.setParameter("pk_musica", avaliarMusica.getPkAvaliarMusica());
				query.setParameter("pk_genero", genero.getPkGenero());
				mg = new MusicaGenero();
				mg = (MusicaGenero) query.getSingleResult();
			}
			return listaM;
		}
		catch ( NoResultException nre )
		{  
			return null;  
		}  
	}
}