package br.com.recomusic.dao;
 
import java.util.List;

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
    
    /**
     * Procura um usuário no sistema através de uma string passada como parâmetro, que pode ser nome, email ou login
     * @param email
     * @return List<Usuario>
     * @throws Exception
     */
    public List<Usuario> procurarUsuarios(String emailLogin) throws Exception
    {
    	try
    	{
    		String EL = emailLogin.trim();
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where UPPER(u.emailUsuario) LIKE :login_email OR UPPER(u.login) LIKE :login_email OR UPPER(u.nome) LIKE :login_email ORDER by u.login ASC"));
    		query.setParameter("login_email", "%"+EL.toUpperCase()+"%");
    		List<Usuario> listaUsuarios =  (List<Usuario>) query.getResultList();
    		return listaUsuarios;
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
    		List<Usuario> usuario =  (List<Usuario>) query.getResultList();
    		
    		if(usuario!=null && usuario.size()>0)
    		{
    			return usuario.get(0);
    		}
    		
    		return null;
    		
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
    
    /**
     * Procurar um usuário pelo ID
     * @param idFacebook
     * @return
     * @throws Exception
     */
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
    
    public Usuario getUsuarioPk(long pkUsuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where u.pkUsuario = :usuario_pkUsuario"));
    		query.setParameter("usuario_pkUsuario", pkUsuario);
    		Usuario usuario =  (Usuario) query.getSingleResult();
    		return usuario;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    public boolean existeLogin(String login) throws Exception
    {
    	try
    	{
    		Usuario usuario = null;
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Usuario as u where UPPER(u.login) = UPPER(:usuario_login)"));
    		query.setParameter("usuario_login", login.trim());
    		usuario =  (Usuario) query.getSingleResult();
    		if(usuario!=null && usuario.getPkUsuario()>0)
    		{
    			return true;
    		}
    		return false;
    	}
    	catch ( NoResultException nre )
    	{  
    		return false;
    	}  
    }
    
    public void salvarUsuario(Usuario usuario) throws Exception
    {
    	try
    	{
    		UsuarioDAO.this.save(usuario);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
}