package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.InformacaoMusicalCadastroMusica;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class InformacaoMusicalCadastroMusicaDAO extends GenericDAO<Long, InformacaoMusicalCadastroMusica>
{
    public InformacaoMusicalCadastroMusicaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * @author Guilherme
     * Salva uma InformacaoMusicalCadastroMusica no banco de dados
     * @param InformacaoMusicalCadastroMusica
     * @throws Exception
     */
    public void salvarMusicaCadastro(InformacaoMusicalCadastroMusica imcm) throws Exception
    {
    	try
    	{
    		InformacaoMusicalCadastroMusicaDAO.this.save(imcm);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
    
    /**
     * @author Guilherme
     * Pesquisa se a InformacaoMusicalCadastroMusica existe através da Musica e Usuario passados como parâmetros
     * @param Musica
     * @param Banda
     * @return  InformacaoMusicalCadastroMusica caso exista ou null caso não exista
     * @throws Exception
     */
    public InformacaoMusicalCadastroMusica pesquisarIMCM(Usuario usuario, Musica musica) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.InformacaoMusicalCadastroMusica as imcMusica where imcMusica.usuario.pkUsuario = :pk_usuario AND imcMusica.musica.pkMusica = :pk_musica"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		query.setParameter("pk_musica", musica.getPkMusica());
    		InformacaoMusicalCadastroMusica imcm = (InformacaoMusicalCadastroMusica) query.getSingleResult();
    	    return imcm;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	} 
    }
}