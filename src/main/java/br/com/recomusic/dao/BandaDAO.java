package br.com.recomusic.dao;
 
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.recomusic.om.Banda;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class BandaDAO extends GenericDAO<Long, Banda>
{
    public BandaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    public void salvarListaBanda(List<Banda> listaBandas) throws Exception
    {
    	try
    	{
    		for (Banda banda : listaBandas)
    		{
    			BandaDAO.this.save(banda);
			}
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
    
    public void pesquisaBandaUsuario(List<Banda> listaBandas) throws Exception
    {
    	try
    	{
    		for (Banda banda : listaBandas)
    		{
    			BandaDAO.this.save(banda);
    		}
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
}