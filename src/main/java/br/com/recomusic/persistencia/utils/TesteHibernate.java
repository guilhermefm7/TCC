package br.com.recomusic.persistencia.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.recomusic.dao.GeneroDAO;
import br.com.recomusic.singleton.ConectaBanco;
public class TesteHibernate
{
	
	/*public static void main(String[] args)
	{
		try
		{
			GeneroDAO generoDAO = new GeneroDAO( ConectaBanco.getInstance().getEntityManager());
			List<String> listaNomes = new ArrayList<String>();
			listaNomes.add("Rock");
			listaNomes.add("Pop");
			generoDAO.salvaListaGeneros(listaNomes);
			ConectaBanco.getInstance().commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ConectaBanco.getInstance().rollBack();
		}
	}*/
}
 
    
