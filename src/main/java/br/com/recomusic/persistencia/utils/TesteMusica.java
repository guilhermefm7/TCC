package br.com.recomusic.persistencia.utils;

import java.util.List;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;

public class TesteMusica
{
	 /*public static void procurarMusica()
	 {
		 try
			{
				EchoNestAPI en = new EchoNestAPI("9QB1EM63CLM2RR5V3");
				SongParams p = new SongParams();
				p.setArtist("Nirvana");
			    p.addIDSpace("rdio-US");
			    p.add("title", "come as you are");
			    p.add("results", 3);	
			    
			    List<Song> songs = en.searchSongs(p);
			    for (Song song : songs)
			    {
			        dumpSong(song);
			        System.out.println();
			    }
			}
			catch  (Exception e)
			{
				e.printStackTrace();
			}
	 }
	 
	 *//**
	  * T�tulo da M�sica
	  * Artista/Banda
	  * Dura��o da m�sica em segundos
	  * BPM da m�sica (quantas batidas por minuto)
	  * Localiza��o do artista
	  * Quanto dan�ante a m�sica �
	  * Quanto en�rgica a m�sica �
	  * @param song
	  * @throws EchoNestException
	  *//*
	 public static void dumpSong(Song song) throws EchoNestException 
	 {
	        System.out.printf("%s\n", song.getTitle());
	        System.out.printf("   Artist: %s\n", song.getArtistName());
	        System.out.printf("   Dur   : %.3f\n", song.getDuration());
	        System.out.printf("   BPM   : %.3f\n", song.getTempo());
	        System.out.printf("   A loc : %s\n", song.getArtistLocation());
	        System.out.printf("   Danc : %.3f\n", song.getDanceability());
	        System.out.printf("   Ener : %.3f\n", song.getEnergy());
	 }

	public static void main(String[] args)
	{
		procurarMusica();
	}*/
}
