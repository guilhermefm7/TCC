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
    
    public Musica procuraMusicaByID(String idMusica) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Musica as m where m.idMusica = :musica_id"));
    		query.setParameter("musica_id", idMusica);
    		Musica musica =  (Musica) query.getSingleResult();
    		return musica;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
}