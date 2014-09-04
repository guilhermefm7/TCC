package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

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
     * Pesquisa se a BandaGenero existe através da Banda e Genero passados como parâmetros
     * @param Banda
     * @param genero
     * @return  BandaGenero caso exista ou null caso não exista
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
     * Pesquisa os gêneros de uma banda
     * @param Banda
     * @param genero
     * @return  BandaGenero caso exista ou null caso não exista
     * @throws Exception
     */
    public List<Genero> pesquisarGenerosBanda(Banda banda) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.BandaGenero as bg where bg.banda.pkBanda = :pk_banda"));
    		query.setParameter("pk_banda", banda.getPkBanda());
    		List<BandaGenero> bandaGenero = (List<BandaGenero>) query.getResultList();
    		
    		List<Genero> listaGeneros = new ArrayList<Genero>();
    		
    		for (BandaGenero bg : bandaGenero)
    		{
    			listaGeneros.add(bg.getGenero());
			}
    		
    		return listaGeneros;
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