package br.com.recomusic.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.recomusic.om.Usuario;

public class UsuarioDAO
{
	public boolean validarUsuario(String login, String senha)
	{
	    EntityManagerFactory factory = Persistence.createEntityManagerFactory("recomusic");
	    EntityManager manager = factory.createEntityManager();



	    Query query = manager.createQuery("FROM Usuario "+
	              "WHERE login = :usuario_login");
	    	query.setParameter("usuario_login", login);
	         Object usuario = query.getSingleResult();
	         if(usuario!=null)
	         {
	        	 Usuario gui = (Usuario) usuario;
	         }
	    manager.close();
		return true;
	}
}