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
     * Pesquisa os g�neros de uma banda
     * @param Banda
     * @param genero
     * @return  BandaGenero caso exista ou null caso n�o exista
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
    		
    		if(bandaGenero!=null && bandaGenero.size()>0)
    		{
	    		for (BandaGenero bg : bandaGenero)
	    		{
	    			listaGeneros.add(bg.getGenero());
				}
    		}
    		else
    		{
    			return null;  
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
     * Pesquisa as Bandas do G�nero passado como par�metro
     * @param Genero genero
     * @return  List<Banda>  caso exista ou null caso n�o exista
     * @throws Exception
     */
    public List<Banda> pesquisarBandas(Genero genero) throws Exception
    {
    	try
    	{
    		List<Banda> listaBandas = new ArrayList<Banda>();
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.BandaGenero as bg where bg.genero.pkGenero = :pk_genero"));
    		query.setParameter("pk_genero", genero.getPkGenero());
    		List<BandaGenero> bandaGenero = (List<BandaGenero>) query.getResultList();
    		
    		if(bandaGenero!=null && bandaGenero.size()>0)
    		{
	    		for (BandaGenero bg : bandaGenero)
	    		{
	    			listaBandas.add(bg.getBanda());
	    		}
    		}
    		else
    		{
    			return null; 
    		}
    		
    		return listaBandas;
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