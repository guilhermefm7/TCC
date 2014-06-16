package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
}