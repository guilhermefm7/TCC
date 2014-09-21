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
			 
			 String manipulaMusica[] = new String[10];
			 manipulaMusica = nomeMusica.split("-"); 
				
			 String url;
			 String urlDeezer;
			 
			 if(manipulaMusica.length>1)
			 {
				 url = "http://developer.echonest.com/api/v4/song/search?api_key=9QB1EM63CLM2RR5V3&format=json&title=" + manipulaMusica[0].replace(" ", "%") + "&bucket=id:deezer&bucket=tracks&limit=true&results=100&artist=" + manipulaMusica[1].replace(" ", "%");
				 
		         String json = IOUtils.toString(new URL(url));
		         String jsonDeezer = "";
		         String pegaAlbum = "";
		         String pegaUrl = "";
		         
		         JSONObject obj = new JSONObject(json);
		         JSONObject objDeezer = null;
		         JSONObject objDeezerAux = null;
		       
		         JSONArray jsonMusicas = (JSONArray) ((JSONObject)obj.get("response")).get("songs");
		         
		         int contador = 1;
		         
		         for (int i = 0; i < jsonMusicas.length(); i++)
		         {
		    	     mIM = new MusicaIM();
	                 JSONObject dado = jsonMusicas.getJSONObject(i);
	                 mIM.setQtd(contador);
	                 contador++;
	                 mIM.setIdMusica((String)dado.get("id"));
	                 mIM.setNomeMusica((String)dado.get("title"));
	                 mIM.setNomeArtista((String)dado.get("artist_name"));
	                 JSONArray pegaIdDeezer =(JSONArray) dado.get("tracks");
	                 
	                 for (int x = 0; x < pegaIdDeezer.length(); x++)
	      	       	 {
			  	    	   JSONObject deezer = pegaIdDeezer.getJSONObject(x);
			  	    	   String pegaIDTrack = (String)deezer.get("foreign_id");
			  	    	   String array[] = new String[3];
				    	   array = pegaIDTrack.split(":"); 
			  	    	   mIM.setIdDeezer(array[2].toString());
			  	    	   mIM.setAlbumMusica("");
			  	    	   urlDeezer = "http://api.deezer.com/2.0/track/" + mIM.getIdDeezer() + "?callback=?";
			  	    	   jsonDeezer = IOUtils.toString(new URL(urlDeezer));
			  	    	   objDeezer = new JSONObject(jsonDeezer);
			  	    	   if(objDeezer!=null && objDeezer.length()>0)
			  	    	   {
			  	    		   if(objDeezer.toString().contains("album"))
			  	    		   {
			  	    			   objDeezerAux = (JSONObject)objDeezer.get("album");
			  	    			   
			  	    			   if(objDeezerAux!=null && objDeezerAux.length()>0)
			  	    			   {
			  	    				   pegaAlbum = (String)objDeezerAux.get("title");
			  	    				   pegaUrl = (String)objDeezerAux.get("cover");
			  	    				   
			  	    				   if(pegaAlbum.length()>0)
			  	    				   {
			  	    					   mIM.setAlbumMusica(pegaAlbum);
			  	    				   }
			  	    				   
			  	    				   if(pegaUrl.length()>0)
			  	    				   {
			  	    					   mIM.setUrlMusica(pegaUrl);
			  	    				   }
			  	    			   }
			  	    		   }
			  	    	   }
	      	         }
	                 
	                 listaMusicas.add(mIM);
		         }
			 }
			 else
			 {
				 url = "http://developer.echonest.com/api/v4/song/search?api_key=9QB1EM63CLM2RR5V3&format=json&title=" + manipulaMusica[0].replace(" ", "%") + "&bucket=id:deezer&bucket=tracks&limit=true&results=50";
				 
		         String json = IOUtils.toString(new URL(url));
		         String jsonDeezer = "";
		         String pegaAlbum = "";
		         String pegaUrl = "";
		         
		         JSONObject obj = new JSONObject(json);
		         JSONObject objDeezer = null;
		         JSONObject objDeezerAux = null;
		       
		         JSONArray jsonMusicas = (JSONArray) ((JSONObject)obj.get("response")).get("songs");
		         
		         int contador = 1;
		         
		         for (int i = 0; i < jsonMusicas.length(); i++)
		         {
		    	     mIM = new MusicaIM();
	                 JSONObject dado = jsonMusicas.getJSONObject(i);
	                 mIM.setQtd(contador);
	                 contador++;
	                 mIM.setIdMusica((String)dado.get("id"));
	                 mIM.setNomeMusica((String)dado.get("title"));
	                 mIM.setNomeArtista((String)dado.get("artist_name"));
	                 JSONArray pegaIdDeezer =(JSONArray) dado.get("tracks");
	                 
	                 for (int x = 0; x < pegaIdDeezer.length(); x++)
	      	       	 {
			  	    	   JSONObject deezer = pegaIdDeezer.getJSONObject(x);
			  	    	   String pegaIDTrack = (String)deezer.get("foreign_id");
			  	    	   String array[] = new String[3];
				    	   array = pegaIDTrack.split(":"); 
			  	    	   mIM.setIdDeezer(array[2].toString());
			  	    	   mIM.setAlbumMusica("");
			  	    	   urlDeezer = "http://api.deezer.com/2.0/track/" + mIM.getIdDeezer() + "?callback=?";
			  	    	   jsonDeezer = IOUtils.toString(new URL(urlDeezer));
			  	    	   objDeezer = new JSONObject(jsonDeezer);
			  	    	   if(objDeezer!=null && objDeezer.length()>0)
			  	    	   {
			  	    		   if(objDeezer.toString().contains("album"))
			  	    		   {
			  	    			   objDeezerAux = (JSONObject)objDeezer.get("album");
			  	    			   
			  	    			   if(objDeezerAux!=null && objDeezerAux.length()>0)
			  	    			   {
			  	    				   pegaAlbum = (String)objDeezerAux.get("title");
			  	    				   pegaUrl = (String)objDeezerAux.get("cover");
			  	    				   
			  	    				   if(pegaAlbum.length()>0)
			  	    				   {
			  	    					   mIM.setAlbumMusica(pegaAlbum);
			  	    				   }
			  	    				   
			  	    				   if(pegaUrl.length()>0)
			  	    				   {
			  	    					   mIM.setUrlMusica(pegaUrl);
			  	    				   }
			  	    			   }
			  	    		   }
			  	    	   }
	      	         }
	                 
	                 listaMusicas.add(mIM);
		         }
		         
		         url = "http://developer.echonest.com/api/v4/song/search?api_key=9QB1EM63CLM2RR5V3&format=json&artist=" + manipulaMusica[0].replace(" ", "%") + "&bucket=id:deezer&bucket=tracks&limit=true&results=50";
		         
		         json = IOUtils.toString(new URL(url));
		         jsonDeezer = "";
		         pegaAlbum = "";
		         pegaUrl = "";
		         
		         obj = new JSONObject(json);
		         objDeezer = null;
		         objDeezerAux = null;
		       
		         jsonMusicas = (JSONArray) ((JSONObject)obj.get("response")).get("songs");
		         
		         contador = 1;
		         
		         for (int i = 0; i < jsonMusicas.length(); i++)
		         {
		    	     mIM = new MusicaIM();
	                 JSONObject dado = jsonMusicas.getJSONObject(i);
	                 mIM.setQtd(contador);
	                 contador++;
	                 mIM.setIdMusica((String)dado.get("id"));
	                 mIM.setNomeMusica((String)dado.get("title"));
	                 mIM.setNomeArtista((String)dado.get("artist_name"));
	                 JSONArray pegaIdDeezer =(JSONArray) dado.get("tracks");
	                 
	                 for (int x = 0; x < pegaIdDeezer.length(); x++)
	      	       	 {
			  	    	   JSONObject deezer = pegaIdDeezer.getJSONObject(x);
			  	    	   String pegaIDTrack = (String)deezer.get("foreign_id");
			  	    	   String array[] = new String[3];
				    	   array = pegaIDTrack.split(":"); 
			  	    	   mIM.setIdDeezer(array[2].toString());
			  	    	   mIM.setAlbumMusica("");
			  	    	   urlDeezer = "http://api.deezer.com/2.0/track/" + mIM.getIdDeezer() + "?callback=?";
			  	    	   jsonDeezer = IOUtils.toString(new URL(urlDeezer));
			  	    	   objDeezer = new JSONObject(jsonDeezer);
			  	    	   if(objDeezer!=null && objDeezer.length()>0)
			  	    	   {
			  	    		   if(objDeezer.toString().contains("album"))
			  	    		   {
			  	    			   objDeezerAux = (JSONObject)objDeezer.get("album");
			  	    			   
			  	    			   if(objDeezerAux!=null && objDeezerAux.length()>0)
			  	    			   {
			  	    				   pegaAlbum = (String)objDeezerAux.get("title");
			  	    				   pegaUrl = (String)objDeezerAux.get("cover");
			  	    				   
			  	    				   if(pegaAlbum.length()>0)
			  	    				   {
			  	    					   mIM.setAlbumMusica(pegaAlbum);
			  	    				   }
			  	    				   
			  	    				   if(pegaUrl.length()>0)
			  	    				   {
			  	    					   mIM.setUrlMusica(pegaUrl);
			  	    				   }
			  	    			   }
			  	    		   }
			  	    	   }
	      	         }
	                 
	                 listaMusicas.add(mIM);
		         }
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
			 Banda banda = null;
			 Params p = new Params();
		     p.set("id", idMusica);
		     p.add("results", 1);	
			    
		    List<Song> songs = en.getSongs(p);
		    for (Song song : songs)
		    {
		    	musica = new Musica();
		    	musica.setTitulo(song.getTitle());
		    	musica.setIdMusica(song.getID());
		    	musica.setBPMMUsica(song.getTempo());
		    	musica.setEnergMusica(song.getEnergy());
		    	musica.setQuantidadeAvaliacoes(0);
		    	musica.setMediaAvaliacoes(0D);
		    	///pesquisa idDeezer
		    	///pesquisa Album
		    	
		    	banda = new Banda();
		    	banda.setNome(song.getArtistName());
		    	banda.setIdBanda(song.getArtistID());
		    	musica.setBanda(banda);
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
