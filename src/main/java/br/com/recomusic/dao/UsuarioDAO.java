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
    
    public Usuario validarUsuarioEmail(String email, String senha) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where UPPER(u.emailUsuario) = UPPER(:usuario_email) and u.senha = :usuario_senha "));
    		query.setParameter("usuario_email", email.trim());
    		query.setParameter("usuario_senha", senha);
    		Usuario usuario =  (Usuario) query.getSingleResult();
    		return usuario;
    	}
    	catch ( NoResultException nre )
    	{  
            return null;  
        }  
    }
    
    public Usuario validarUsuarioLogin(String login, String senha) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where UPPER(u.login) = UPPER(:usuario_login) and u.senha = :usuario_senha "));
    		query.setParameter("usuario_login", login.trim());
    		query.setParameter("usuario_senha", senha);
    		Usuario usuario =  (Usuario) query.getSingleResult();
    		return usuario;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    public Usuario validarEmail(String email) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where UPPER(u.emailUsuario) = UPPER(:usuario_email)"));
    		query.setParameter("usuario_email", email.trim());
    		Usuario usuario =  (Usuario) query.getSingleResult();
    		return usuario;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    public Usuario validarID(String idFacebook) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where u.idFacebook = :usuario_idFacebook"));
    		query.setParameter("usuario_idFacebook", idFacebook);
    		Usuario usuario =  (Usuario) query.getSingleResult();
    		return usuario;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    public Usuario validarLogin(String login) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where UPPER(u.login) = UPPER(:usuario_login)"));
    		query.setParameter("usuario_login", login.trim());
    		Usuario usuario =  (Usuario) query.getSingleResult();
    		return usuario;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
}