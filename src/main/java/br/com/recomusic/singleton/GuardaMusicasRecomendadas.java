package br.com.recomusic.singleton;

import java.util.HashMap;
import java.util.List;

public final class GuardaMusicasRecomendadas
{
	private static HashMap<Long, List<Long>> existeMusica = null;
	
	private GuardaMusicasRecomendadas(){	}
	
	public static synchronized HashMap<Long, List<Long>> getTokensExisteMusica()
	{
		if(existeMusica == null)
		{
			existeMusica = new HashMap<Long, List<Long>>();
		}
		return existeMusica;
	}
}