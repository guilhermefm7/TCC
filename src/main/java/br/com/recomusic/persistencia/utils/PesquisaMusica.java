package br.com.recomusic.persistencia.utils;

import java.util.List;

import br.com.recomusic.om.Banda;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.Params;

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
}
