package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Banda;
import br.com.recomusic.om.BandaGenero;
import br.com.recomusic.om.Genero;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class BandaGeneroDAO extends GenericDAO<Long, BandaGenero>
{
    public BandaGeneroDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * @author Guilherme
     * Pesquisa se a BandaGenero existe atrav�s da Banda e Genero passados como par�metros
     * @param Banda
     * @param genero
     * @return  BandaGenero caso exista ou null caso n�o exista
     * @throws Exception
     */
    public BandaGenero pesquisarBandaGenero(Banda banda, Genero genero) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.BandaGenero as bg where bg.banda.pkBanda = :pk_banda AND bg.genero.pkGenero = :pk_genero"));
    		query.setParameter("pk_banda", banda.getPkBanda());
    		query.setParameter("pk_genero", genero.getPkGenero());
    	    BandaGenero bandaGenero = (BandaGenero) query.getSingleResult();
    	    return bandaGenero;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	} 
    }
    
    
    /**
     * @author Guilherme
     * Salva uma BandaGenero no banco de dados
     * @param BandaGenero
     * @throws Exception
     */
    public void salvarBandaGenero(BandaGenero bg) throws Exception
    {
    	try
    	{
    		BandaGeneroDAO.this.save(bg);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
}