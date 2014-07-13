package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Genero;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class GeneroDAO extends GenericDAO<Long, Genero>
{
    public GeneroDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * @author Guilherme
     * Pesquisa ,através do nome, se o gênero passado como parâmetro existe
     * @param BandaGeneroIM
     * @throws Exception
     * return Genero em caso ele tenha sido achado ou null caso não exista
     */
    public Genero pesquisarGenero(String nome) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Genero as g where g.nomeGenero = :genero_nomeGenero"));
        	query.setParameter("genero_nomeGenero", nome);
        	Genero genero = (Genero) query.getSingleResult();
        	return genero;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}   
    }
    
    /**
     * @author Guilherme
     * Salva uma lista Genero
     * @param BandaGeneroIM
     * @throws Exception
     * return Genero em caso ele tenha sido achado ou null caso não exista
     */
    public Genero salvaListaGeneros(String nome) throws Exception
    {
    	try
    	{
    		Genero genero =  new Genero();
    		genero.setNomeGenero(nome);
    		this.save(genero);
    		return genero;
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    		return null;
    	}   
    }
}