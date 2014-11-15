package br.com.recomusic.singleton;

import java.util.concurrent.Semaphore;

public final class ControleUploadMusica
{
	private static Semaphore uploadMusicaControle = null;
	
	private ControleUploadMusica(){	}
	
	public static synchronized Semaphore getControle()
	{
		if(uploadMusicaControle == null)
		{
			uploadMusicaControle = new Semaphore(1);
		}
		return uploadMusicaControle;
	}
}