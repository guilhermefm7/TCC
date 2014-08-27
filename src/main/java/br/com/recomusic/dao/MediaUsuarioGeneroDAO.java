package br.com.recomusic.dao;
 
import java.util.List;

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
     * @param Usuario usuario
     * @param Genero genero
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
     * Pesquisa todas as MediaUsuarioGenero do Usuário
     * @param usuario
     * @throws Exception
     * return List<MediaUsuarioGenero> do usuário ou null caso não exista
     */
    public List<MediaUsuarioGenero> pesquisaGenerosUsuario(Usuario usuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.MediaUsuarioGenero as mug where mug.usuario.pkUsuario = :pk_usuario AND mug.mediaAvaliacoes >=3 ORDER BY mug.quantidadeMusicas DESC"));
        	query.setParameter("pk_usuario", usuario.getPkUsuario());
        	List<MediaUsuarioGenero> lista = (List<MediaUsuarioGenero>) query.getResultList();
        	return lista;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}   
    }
    
    /**
     * @author Guilherme
     * Pesquisa todas as MediaUsuarioGenero do Gênero passado como parâmetro, exceto o do usuário passado como parâmetro
     * @param Genero genero
     * @param Usuario usuario
     * @throws Exception
     * return List<MediaUsuarioGenero> do gênero ou null caso não exista
     */
    public List<MediaUsuarioGenero> pesquisaAllByGenero(Genero genero, Usuario usuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.MediaUsuarioGenero as mug where mug.genero.pkGenero = :genero_pkGenero AND mug.usuario.pkUsuario <> :pk_usuario AND mug.mediaAvaliacoes >= 3 ORDER By mug.media ASC"));
    		query.setParameter("genero_pkGenero", genero.getPkGenero());
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		List<MediaUsuarioGenero> lista = (List<MediaUsuarioGenero>) query.getResultList();
    		return lista;
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