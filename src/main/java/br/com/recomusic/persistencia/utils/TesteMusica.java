package br.com.recomusic.persistencia.utils;

import java.util.List;

import br.com.recomusic.om.Musica;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.Biography;
import com.echonest.api.v4.Blog;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Image;
import com.echonest.api.v4.News;
import com.echonest.api.v4.Params;
import com.echonest.api.v4.Review;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;
import com.echonest.api.v4.Video;
import com.echonest.api.v4.examples.SearchSongsExample;

public class TesteMusica
{
	/*
	 public static void procuraArtista(String artista)
	 {
		 try
		 {
			EchoNestAPI en = new EchoNestAPI("9QB1EM63CLM2RR5V3");
		    
			Params p = new Params();
	        p.add("name", artista);
	        p.add("results", 1);

	        List<Artist> artists = en.searchArtists(p);
	        for (Artist artist : artists) {
	        	 System.out.printf("%s\n", artist.getName());
	             System.out.printf("   hottt %.3f\n", artist.getHotttnesss());
	             System.out.printf("   fam   %.3f\n", artist.getFamiliarity());
	             //System.out.printf("   fam   %.3f\n", artist.getBiographies().get(0).getText());
	             //System.out.printf("   fam   %.3f\n", artist.getBiographies().get(0).getSite());
	             //System.out.printf("   fam   %.3f\n", artist.getNews().get(0).getDateFound());
	            // System.out.printf("   fam   %.3f\n", artist.getYearsActive());
	        }
		 }
		 catch  (Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 public static void procurarMusica()
	 {
		 try
			{
				EchoNestAPI en = new EchoNestAPI("9QB1EM63CLM2RR5V3");
				SongParams p = new SongParams();
				p.setArtist("Nirvana");
			    p.addIDSpace("deezer");
			    p.addIDSpace("deezer");
			    p.add("title", "come as you are");
			    p.add("results", 1);	
			    
			    List<Song> songs = en.searchSongs(p);
			    for (Song song : songs)
			    {
			        //dumpSong(song);
			        System.out.println(song.getID());
			    }
			}
			catch  (Exception e)
			{
				e.printStackTrace();
			}
	 }
	 
	 public static void procuraArtista()
	 {
		 try
		 {
			EchoNestAPI en = new EchoNestAPI("9QB1EM63CLM2RR5V3");
		    
			Params p = new Params();
	        p.add("name", "Nirvana");
	        
	        p.add("results", 1);

	        List<Artist> artists = en.searchArtists(p);
	        for (Artist artist : artists) {
	        	 System.out.printf("%s\n", artist.getName());
	        	 System.out.printf("%s\n", artist.getID());
	        	 
	             //System.out.printf("   fam   %.3f\n", artist.getBiographies().get(0).getText());
	             //System.out.printf("   fam   %.3f\n", artist.getBiographies().get(0).getSite());
	             //System.out.printf("   fam   %.3f\n", artist.getNews().get(0).getDateFound());
	            // System.out.printf("   fam   %.3f\n", artist.getYearsActive());
	             //dumpArtist(artist);
	        }
		 }
		 catch  (Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 
	 public static void procuraMusicaa()
	 {
		 try
		 {
			 EchoNestAPI en = new EchoNestAPI("9QB1EM63CLM2RR5V3");
			 Musica musica = null;
			 Params p = new Params();
		     p.set("id", "SOMIVIZ12C0DD05759");
			 //p.setID("SOMSJMX13770F238EB");
		     //p.addIDSpace("deezer");
		     p.add("results", 1);	
		     List<Song> songs = en.getSongs(p);
		     for (Song song : songs)
		     {
		    	 musica = new Musica();
		    	 musica.setTitulo(song.getTitle());
		    	 musica.setIdMUsica(song.getID());
		    	 System.out.println(song.getTitle());
		    	 System.out.println(song.getID());
		     }
		 }
		 catch  (Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 
	 public static void dumpArtist(Artist artist) throws EchoNestException {
	        System.out.printf("%s\n", artist.getName());
	        System.out.printf("   hottt %.3f\n", artist.getHotttnesss());
	        System.out.printf("   fam   %.3f\n", artist.getFamiliarity());

	        System.out.println(" =========  urls ======== ");
	        for (String key : artist.getUrls().keySet()) {
	            System.out.printf("   %10s %s\n", key, artist.getUrls().get(key));
	        }


	        System.out.println(" =========  bios ======== ");
	        List<Biography> bios = artist.getBiographies();
	        for (int i = 0; i < bios.size(); i++) {
	            Biography bio = bios.get(i);
	            bio.dump();
	        }

	        System.out.println(" =========  blogs ======== ");
	        List<Blog> blogs = artist.getBlogs();
	        for (int i = 0; i < blogs.size(); i++) {
	            Blog blog = blogs.get(i);
	            blog.dump();
	        }

	        System.out.println(" =========  images ======== ");
	        List<Image> images = artist.getImages();
	        for (int i = 0; i < images.size(); i++) {
	            Image image = images.get(i);
	            image.dump();
	        }

	        System.out.println(" =========  news ======== ");
	        List<News> newsList = artist.getNews();
	        for (int i = 0; i < newsList.size(); i++) {
	            News news = newsList.get(i);
	            news.dump();
	        }

	        System.out.println(" =========  reviews ======== ");
	        List<Review> reviews = artist.getReviews();
	        for (int i = 0; i < reviews.size(); i++) {
	            Review review = reviews.get(i);
	            review.dump();
	        }

	        System.out.println(" =========  videos ======== ");
	        List<Video> videos = artist.getVideos();
	        for (int i = 0; i < videos.size(); i++) {
	            Video video = videos.get(i);
	            video.dump();
	        }
	    }
	 
	 *//**
	  * Título da Música
	  * Artista/Banda
	  * Duração da música em segundos
	  * BPM da música (quantas batidas por minuto)
	  * Localização do artista
	  * Quanto dançante a música é
	  * Quanto enérgica a música é
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
		procuraMusicaa();
	}*/
}
