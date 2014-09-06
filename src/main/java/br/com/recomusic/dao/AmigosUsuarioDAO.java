package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.AmigosUsuario;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class AmigosUsuarioDAO extends GenericDAO<Long, AmigosUsuario>
{
    public AmigosUsuarioDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * Procura todos os amigos do usuário passado como parâmetro
     * @param pkUsuario
     * @return List<Usuario>
     * @throws Exception
     */
    public List<Usuario> getAmigosUsuario(long pkUsuario) throws Exception
    {
    	try
    	{
    		List<Usuario> listaUsuario = null;
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AmigosUsuario as au where au.usuario.pkUsuario = :usuario_pkUsuario ORDER BY au.usuario.nome ASC"));
    		query.setParameter("usuario_pkUsuario", pkUsuario);
    		List<AmigosUsuario> listaAU =  (List<AmigosUsuario>) query.getResultList();
    		
    		if(listaAU!=null && listaAU.size()>0)
    		{
    			listaUsuario = new ArrayList<Usuario>();
    			for (AmigosUsuario amigosUsuario : listaAU)
    			{
					listaUsuario.add(amigosUsuario.getAmigo());
				}
    		}
    		
    		return listaUsuario;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    /**
     * Procura se o usuário passado como parâmetro é amigo do outro usuário passado como parâmetro
     * @param pkUsuarioPrincipal
     * @param pkUsuarioSecundario
     * @return true caso exista e false ou null caso não exista
     * @throws Exception
     */
    public Boolean getAmigosUsuario(long pkUsuarioPrincipal, long pkUsuarioSecundario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AmigosUsuario as au where au.usuario.pkUsuario = :pkUsuarioPrincipal AND au.amigo.pkUsuario = :pkUsuarioSecundario"));
    		query.setParameter("pkUsuarioPrincipal", pkUsuarioPrincipal);
    		query.setParameter("pkUsuarioSecundario", pkUsuarioSecundario);
    		AmigosUsuario au =  (AmigosUsuario) query.getSingleResult();
    		
    		if(au!=null && au.getPkAmigosUsuario()>0)
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    /**
     * Procura se o usuário passado como parâmetro é amigo do outro usuário passado como parâmetro retornando o objeto 
     * @param pkUsuarioPrincipal
     * @param pkUsuarioSecundario
     * @return AmigosUsuario caso exista e null caso não exista
     * @throws Exception
     */
    public AmigosUsuario getOneAmigosUsuario(long pkUsuarioPrincipal, long pkUsuarioSecundario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AmigosUsuario as au where au.usuario.pkUsuario = :pkUsuarioPrincipal AND au.amigo.pkUsuario = :pkUsuarioSecundario"));
    		query.setParameter("pkUsuarioPrincipal", pkUsuarioPrincipal);
    		query.setParameter("pkUsuarioSecundario", pkUsuarioSecundario);
    		AmigosUsuario au =  (AmigosUsuario) query.getSingleResult();
    		
    		return au;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    /**
     * Remover um objeto AmigosUsuario passado como parâmetro
     * @param am
     * @throws Exception
     */
    public void removerAmigosUsuario(AmigosUsuario am) throws Exception
    {
    	try
    	{
    		this.delete(am);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
    
    /**
     * Salva no banco de dados os dois AmigosUsuario passados como parâmetro
     * @param AmigosUsuario am1
     * @param AmigosUsuario am2
     * @return
     * @throws Exception
     */
    public void salvarAmigos(AmigosUsuario am1, AmigosUsuario am2) throws Exception
    {
    	try
    	{
    		this.save(am1);
    		this.save(am2);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
}