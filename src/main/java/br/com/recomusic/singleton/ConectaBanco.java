package br.com.recomusic.singleton;

import br.com.recomusic.gerente.SimpleEntityManager;

public final class ConectaBanco
{
	private static SimpleEntityManager simpleEntityManager;
	
	private ConectaBanco()
	{
        	getInstance();
	}
	
	public static synchronized SimpleEntityManager getInstance()
	{
		if(simpleEntityManager==null)
		{
			String persistenceUnitName = "recomusic";
			simpleEntityManager = new SimpleEntityManager(persistenceUnitName);
		}
		return simpleEntityManager;
	}
	
}