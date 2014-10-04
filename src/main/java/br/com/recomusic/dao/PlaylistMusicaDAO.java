package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Musica;
import br.com.recomusic.om.Playlist;
import br.com.recomusic.om.PlaylistMusica;
import br.com.recomusic.persistencia.utils.Constantes;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class PlaylistMusicaDAO extends GenericDAO<Long, PlaylistMusica>
{
    public PlaylistMusicaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * Pega todas as MUsicas de uma Playlist
     * @param String pkPlaylist
     * @throws Exception
     */
    public List<Musica> getMusicasPlaylist(String pkPlaylist) throws Exception
    {
    	try
    	{
    		List<PlaylistMusica> listaPM = null;
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.PlaylistMusica as pm where pm.playlist.pkPlaylist = :pk_playlist AND pm.status = :status_ativo ORDER by pm.lancamento"));
    		query.setParameter("pk_playlist", Long.valueOf(pkPlaylist));
    		query.setParameter("status_ativo", Constantes.TIPO_STATUS_ATIVO);
    		listaPM = (List<PlaylistMusica>)query.getResultList();
    		
    		List<Musica> listaM = new ArrayList<Musica>();
    		
    		if(listaPM!=null && listaPM.size()>0)
    		{
    			for (PlaylistMusica pm : listaPM)
    			{
					if(pm!=null && pm.getPkPlaylistMusica()>0 && pm.getMusica()!=null && pm.getMusica().getPkMusica()>0)
					{
						listaM.add(pm.getMusica());
					}
				}
    		}
    		
    		return listaM;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;
    	}  
    }
    
    /**
     * Remove uma música de uma playlist 
     * @param String pkPlaylist
     * @param String idDeezer
     * @throws Exception
     */
    public boolean removeMusicaPlaylist(String pkPlaylist, String idDeezer) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.PlaylistMusica as pm where pm.playlist.pkPlaylist = :pk_playlist AND pm.status = :status_ativo AND pm.musica.idDeezer = :id_musica"));
    		query.setParameter("pk_playlist", Long.valueOf(pkPlaylist));
    		query.setParameter("status_ativo", Constantes.TIPO_STATUS_ATIVO);
    		query.setParameter("id_musica", idDeezer);
    		List<PlaylistMusica> pm = (List<PlaylistMusica>)query.getResultList();
    		
    		if(pm!=null && pm.size()>0)
    		{
    			delete(pm.get(0));
    			return true;
    		}
    		else
    		{
    			return false;	
    		}
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    		return false;
    	}  
    }
}