package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Musica;
import br.com.recomusic.om.PossivelAvaliacaoMusica;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class PossivelAvaliacaoMusicaDAO extends GenericDAO<Long, PossivelAvaliacaoMusica>
{
    public PossivelAvaliacaoMusicaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * Pega todas as MUsicas de uma Playlist
     * @param String pkPlaylist
     * @throws Exception
     */
    public PossivelAvaliacaoMusica getMusicasPlaylist(Usuario usuario, Musica musica) throws Exception
    {
    	try
    	{
    		PossivelAvaliacaoMusica possivelAvaliacaoMusica = null;
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.PossivelAvaliacaoMusica as possivelAvaliacaoMusica where possivelAvaliacaoMusica.musica.pkMusica = :pk_musica AND possivelAvaliacaoMusica.usuario.pkUsuario = :pk_usuario"));
    		query.setParameter("pk_usuario", Long.valueOf(usuario.getPkUsuario()));
    		query.setParameter("pk_musica", Long.valueOf(musica.getPkMusica()));
    		possivelAvaliacaoMusica = (PossivelAvaliacaoMusica)query.getSingleResult();
    		
    		return possivelAvaliacaoMusica;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;
    	}  
    }
}