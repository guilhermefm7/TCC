package br.com.recomusic.dao;
 
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Banda;
import br.com.recomusic.singleton.ConectaBanco;
 
public class BandaDAO extends GenericDAO<Long, Banda>
{
    public BandaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * @author Guilherme
     * Salva uma lista de Bandas
     * @param BandaGeneroIM
     * @throws Exception
     */
    public void salvarBanda(Banda banda) throws Exception
    {
    	try
    	{
    		BandaDAO.this.save(banda);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}   
    }
    
    /**
     * @author Guilherme
     * Pesquisa se uma banda passada como parâmento existe
     * @param Banda
     * @throws Exception
     */
    public Banda pesquisarBandaExiste(String id) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Banda as b where b.idBanda = :id_banda"));
    		query.setParameter("id_banda", id);
    		Banda banda =  (Banda) query.getSingleResult();
    		return banda;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}   
    }
    
    /**
     * @author Guilherme
     * 
     * @param listaBandas
     * @throws Exception
     */
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