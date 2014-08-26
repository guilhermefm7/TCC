package br.com.recomusic.singleton;

import java.util.HashMap;

public final class GuardaMusicasRecomendadas
{
	private static HashMap<Long, String> existeMusica = null;
	
	private GuardaMusicasRecomendadas(){	}
	
	public static synchronized HashMap<Long, String> getTokensExisteMusica()
	{
		if(existeMusica == null)
		{
			existeMusica = new HashMap<Long, String>();
		}
		return existeMusica;
	}
}