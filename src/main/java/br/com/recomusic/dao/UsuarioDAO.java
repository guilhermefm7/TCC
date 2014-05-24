package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class UsuarioDAO extends GenericDAO<Long, Usuario>
{
    public UsuarioDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    public Usuario validarUsuario(String email, String senha) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where u.emailUsuario = :usuario_email and u.senha = :usuario_senha "));
    		query.setParameter("usuario_email", email);
    		query.setParameter("usuario_senha", senha);
    		Usuario usuario =  (Usuario) query.getSingleResult();
    		return usuario;
    	}
    	catch ( NoResultException nre )
    	{  
            return null;  
        }  
    }
}