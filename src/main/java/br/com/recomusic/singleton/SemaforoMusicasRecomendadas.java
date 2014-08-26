package br.com.recomusic.singleton;

import java.util.concurrent.Semaphore;

public final class SemaforoMusicasRecomendadas
{
	private static Semaphore semaforoMusicasRecomendadas = null;
	
	private SemaforoMusicasRecomendadas(){	}
	
	public static synchronized Semaphore getSemaforo()
	{
		if(semaforoMusicasRecomendadas == null)
		{
			semaforoMusicasRecomendadas = new Semaphore(1);
		}
		return semaforoMusicasRecomendadas;
	}
}