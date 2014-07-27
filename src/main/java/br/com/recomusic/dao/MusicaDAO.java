package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Musica;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class MusicaDAO extends GenericDAO<Long, Musica>
{
    public MusicaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * Salva uma m�sica no banco de dados
     * @param musica
     * @throws Exception
     */
    public void salvarMusica(Musica musica) throws Exception
    {
    	try
    	{
    		MusicaDAO.this.save(musica);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
    
    /**
     * Procura uma m�sica atrav�s de seu idDeezer passado como par�metro
     * @param String idMusicaDeezer
     * return a m�sica ou nulo caso n�o ache nada
     * @throws Exception
     */
    public Musica procuraMusicaByID(String idMusicaDeezer) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Musica as m where m.idDeezer = :musica_id"));
    		query.setParameter("musica_id", idMusicaDeezer);
    		Musica musica =  (Musica) query.getSingleResult();
    		return musica;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
}