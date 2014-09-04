package br.com.recomusic.persistencia.utils;

import br.com.recomusic.singleton.GuardaMusicasRecomendadas;
import br.com.recomusic.singleton.SemaforoMusicasRecomendadas;

public class ThreadProcuraMusica implements Runnable {
	public ThreadProcuraMusica() {
	}

	public static int myCount = 0;

	/**
	 * Método responsável por gerar o HashMap das músicas recomendadas e zerar ele a cada 20 minutos
	 */
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		GuardaMusicasRecomendadas.getTokensExisteMusica().clear();
		while (myCount<2)
		{
			try
			{
				//Thread.sleep(20000);
				Thread.sleep(1200000);
			}
			catch (InterruptedException iex)
			{
				System.out.println("Exception in thread: " + iex.getMessage());
			}
			
			try
			{
				SemaforoMusicasRecomendadas.getSemaforo().acquire();
				
				System.out.println("Acordei: ");
				GuardaMusicasRecomendadas.getTokensExisteMusica().clear();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				SemaforoMusicasRecomendadas.getSemaforo().release();
			}
		}
	}
}
