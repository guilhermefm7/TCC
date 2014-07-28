package br.com.recomusic.dao;
 
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Playlist;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class PlaylistDAO extends GenericDAO<Long, Playlist>
{
    public PlaylistDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * Salva um registro Playlist passado como parâmetro
     * @param Playlist
     * @throws Exception
     */
    public void salvarPlaylist(Playlist playlist) throws Exception
    {
    	try
    	{
    		PlaylistDAO.this.save(playlist);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
    
    /**
     * Autor: Guilherme
     * Procura todas as Playlist que o usuário passado como parâmetro criou
     * Usuario usuario
     * lista com as Playlists
     */
    public List<Playlist> getPlaylistsUsuario(Usuario usuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Playlist as o where o.usuario.pkUsuario = :pk_usuario"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		List<Playlist> listaP = (List<Playlist>) query.getResultList();
    		
    		if(listaP!=null && listaP.size()>0)
    		{
    			return listaP;
    		}
    		else
    		{
    			return null;
    		}
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
}