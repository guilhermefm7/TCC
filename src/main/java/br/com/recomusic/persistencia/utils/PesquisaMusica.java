package br.com.recomusic.persistencia.utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.recomusic.im.MusicaIM;
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
	 
	 public static List<String> requisitarGeneroBanda(String idBanda)
	 {
		 try
		 {
			 List<String> listaGeneros = new ArrayList<String>();
			 
			 String url = "http://developer.echonest.com/api/v4/artist/profile?api_key=9QB1EM63CLM2RR5V3&id=" +idBanda + "&bucket=genre&format=json";
		     String stringJson = IOUtils.toString(new URL(url));
		     JSONObject json = new JSONObject(stringJson);
		     System.out.println(json.toString());
		     
		     
		     JSONArray jsonGeneros = (JSONArray) ((JSONObject)((JSONObject)json.get("response")).get("artist")).get("genres");
	       
	        for (int i = 0; i < jsonGeneros.length(); i++)
	        {
                JSONObject dado = jsonGeneros.getJSONObject(i);
               //System.out.println(dado.get("name"));
                listaGeneros.add((String)dado.get("name"));
	        }
		     
			 return listaGeneros;

		 }
		 catch  (JSONException e)
		 {
			 e.printStackTrace();
			 return null;
		 }
		 catch  (IOException io)
		 {
			 io.printStackTrace();
			 return null;
		 }
	 }
	 
	 public static List<MusicaIM> requisitarMusicaJson(String nomeMusica)
	 {
		 try
		 {
			 List<MusicaIM> listaMusicas = new ArrayList<MusicaIM>();
			 MusicaIM mIM;
	         String url = "http://developer.echonest.com/api/v4/song/search?api_key=9QB1EM63CLM2RR5V3&format=json&title=" + nomeMusica + "&bucket=id:deezer&bucket=tracks&limit=true&";
	         String json = IOUtils.toString(new URL(url));
	         JSONObject obj = new JSONObject(json);
	       
	         JSONArray jsonMusicas = (JSONArray) ((JSONObject)obj.get("response")).get("songs");
	       
	         for (int i = 0; i < jsonMusicas.length(); i++)
	         {
	    	     mIM = new MusicaIM();
                 JSONObject dado = jsonMusicas.getJSONObject(i);
                 mIM.setIdMusica((String)dado.get("id"));
                 mIM.setNomeMusica((String)dado.get("title"));
                 mIM.setNomeArtista((String)dado.get("artist_name"));
                 listaMusicas.add(mIM);
	         }
			 
			 return listaMusicas;
		 }
		 catch  (JSONException e)
		 {
			 e.printStackTrace();
			 return null;
		 }
		 catch  (IOException io)
		 {
			 io.printStackTrace();
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
		    	musica.setBPMMUsica(song.getTempo());
		    	musica.setEnergMusica(song.getEnergy());
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
