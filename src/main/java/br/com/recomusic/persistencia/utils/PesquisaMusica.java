package br.com.recomusic.persistencia.utils;

import java.util.List;

import br.com.recomusic.om.Banda;
import br.com.recomusic.om.Musica;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.Params;
import com.echonest.api.v4.Song;

public class PesquisaMusica
{
	
	 public static Banda procuraArtista(EchoNestAPI en, String artista)
	 {
		 try
		 {
			Banda banda = null;
			Params p = new Params();
	        p.add("name", artista);
	        p.add("results", 1);

	        List<Artist> artists = en.searchArtists(p);
	        for (Artist artist : artists)
	        {
	        	banda = new Banda();
	        	banda.setNome(artist.getName());
	        	banda.setIdBanda(artist.getID());
	        }
	        return banda;
		 }
		 catch  (Exception e)
		 {
			 e.printStackTrace();
			 return null;
		 }
	 }
	 
	 public static Musica procuraMusica(EchoNestAPI en, String idMusica)
	 {
		 try
		 {
			 Musica musica = null;
			 Params p = new Params();
		     p.set("id", idMusica);
		     p.add("results", 1);	
			    
		    List<Song> songs = en.getSongs(p);
		    for (Song song : songs)
		    {
		    	musica = new Musica();
		    	musica.setTitulo(song.getTitle());
		    	musica.setIdMUsica(song.getID());
		    }
			 return musica;
		 }
		 catch  (Exception e)
		 {
			 e.printStackTrace();
			 return null;
		 }
	 }
}
