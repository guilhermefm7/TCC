package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Genero;
import br.com.recomusic.om.MediaUsuarioGenero;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class MediaUsuarioGeneroDAO extends GenericDAO<Long, MediaUsuarioGenero>
{
    public MediaUsuarioGeneroDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * @author Guilherme
     * Pesquisa ,através do usuario e genero, se existe um MediaUsuarioGenero
     * @param BandaGeneroIM
     * @throws Exception
     * return MediaUsuarioGenero em caso ele tenha sido achado ou null caso não exista
     */
    public MediaUsuarioGenero pesquisarExiste(Usuario usuario, Genero genero) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.MediaUsuarioGenero as mug where mug.genero.pkGenero = :genero_nomeGenero AND mug.usuario.pkUsuario = :usuario_pkUsuario"));
        	query.setParameter("genero_nomeGenero", genero.getPkGenero());
        	query.setParameter("usuario_pkUsuario", usuario.getPkUsuario());
        	MediaUsuarioGenero mUG = (MediaUsuarioGenero) query.getSingleResult();
        	return mUG;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}   
    }
    
    /**
     * @author Guilherme
     * Salva um MediaUsuarioGenero
     * @param MediaUsuarioGenero
     * @throws Exception
     * return Genero em caso ele tenha sido achado ou null caso não exista
     */
    public void salvaMediaUsuarioGenero(MediaUsuarioGenero mug) throws Exception
    {
    	try
    	{
    		this.save(mug);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}   
    }
}