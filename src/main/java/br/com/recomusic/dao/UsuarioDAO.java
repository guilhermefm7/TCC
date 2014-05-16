package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
 

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
    
    public boolean validarUsuario(String email, String senha) throws Exception
    {
    	//Object usuario = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM USUARIO")).getFirstResult();
    	return true;
    }
}