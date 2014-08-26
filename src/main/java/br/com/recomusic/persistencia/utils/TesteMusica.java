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
import com.echonest.api.v4.Segment;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.TrackAnalysis;
import com.echonest.api.v4.Video;
import com.echonest.api.v4.examples.SearchSongsExample;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class TesteMusica
{
	
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
	        	 System.out.printf("%s\n", artist.getForeignID("deezer"));
	             dumpArtist(artist);
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
		    	// musica = new Musica();
		    	 //musica.setTitulo(song.getTitle());
		    	// musica.setIdMUsica(song.getID());
		    	 System.out.println(song.getTitle());
		    	 System.out.println(song.getID());
		    	 verifySong(song);
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
	 
	 @Test
	 public static void procurarMusica() throws EchoNestException
	 {

				EchoNestAPI en = new EchoNestAPI("9QB1EM63CLM2RR5V3");
				SongParams p = new SongParams();
				p.setArtist("Nirvana");
			    p.addIDSpace("deezer");
			    p.add("title", "Come as you are");
			    p.add("results", 1);	
			    p.includeTracks();
			    List<Song> songs = en.searchSongs(p);
			    Map data = new HashMap();
			    data.put("id", "deezer");
			    Map<String, Track> trackMap = new HashMap<String, Track>();
			    Track track = trackMap.get("deezer");
			    //trackFromID();
			    for (Song song : songs)
			    {
			    	Track track1 = song.getTrack("deezer");
			    	//System.out.printf("Spotify FID %s\n", track.getForeignID());
			    	 //verifyAnalysis(song.getAnalysis());
			    	//track.showAll();
			    	System.out.println("Track " + track1.getForeignID());
			    	//verifyTrack(track);
			        //dumpSong(song);
			    }

	 }
	    private static void verifyTrack(Track track) throws EchoNestException {
	        assertTrue("status check", track.getStatus().equals(
	                Track.AnalysisStatus.COMPLETE));
	        track.showAll();
	    }
	    
	    private static void verifyTrack1(Track track) throws EchoNestException {
	        track.showAll();
	        assertTrue("has analysis url", track.getAnalysisURL() != null);
	        verifyAnalysis1(track.getAnalysis(), false);
	        System.out.println("ds" +track.getAnalysisURL());
	        System.out.println(track.getAudioUrl());
	    }
	    
	    private static void verifyAnalysis1(TrackAnalysis analysis, boolean full) {
	        System.out.println("num samples : " + analysis.getNumSamples());
	        System.out.println("sample md5  : " + analysis.getMD5());
	        System.out.println("num channels: " + analysis.getNumChannels());
	        System.out.println("duration    : " + analysis.getDuration());

	        if (full) {

	            System.out.println(" Sections ");
	            List<TimedEvent> sections = analysis.getSections();
	            for (TimedEvent e : sections) {
	                System.out.println(e);
	            }

	            System.out.println(" Bars ");
	            List<TimedEvent> bars = analysis.getBars();
	            for (TimedEvent e : bars) {
	                System.out.println(e);
	            }

	            System.out.println(" Beats ");
	            List<TimedEvent> beats = analysis.getBeats();
	            for (TimedEvent e : beats) {
	                System.out.println(e);
	            }

	            System.out.println(" Tatums ");
	            List<TimedEvent> tatums = analysis.getTatums();
	            for (TimedEvent e : tatums) {
	                System.out.println(e);
	            }

	            System.out.println(" Segments ");
	            List<Segment> segments = analysis.getSegments();
	            for (Segment e : segments) {
	                System.out.println(e);
	            }
	        }
	    }
	    
	    private static void verifyAnalysis(TrackAnalysis analysis) {
	        System.out.println("num samples : " + analysis.getNumSamples());
	        System.out.println("sample md5  : " + analysis.getMD5());
	        System.out.println("num channels: " + analysis.getNumChannels());
	        System.out.println("duration    : " + analysis.getDuration());

	        System.out.println(" Sections ");
	        List<TimedEvent> sections = analysis.getSections();
	        for (TimedEvent e : sections) {
	            System.out.println(e);
	        }

	        System.out.println(" Bars ");
	        List<TimedEvent> bars = analysis.getBars();
	        for (TimedEvent e : bars) {
	            System.out.println(e);
	        }

	        System.out.println(" Beats ");
	        List<TimedEvent> beats = analysis.getBeats();
	        for (TimedEvent e : beats) {
	            System.out.println(e);
	        }

	        System.out.println(" Tatums ");
	        List<TimedEvent> tatums = analysis.getTatums();
	        for (TimedEvent e : tatums) {
	            System.out.println(e);
	        }

	        System.out.println(" Segments ");
	        List<Segment> segments = analysis.getSegments();
	        for (Segment e : segments) {
	            System.out.println(e);
	        }
	    }

	    private static void verifySong(Song song) throws EchoNestException {
	        song.showAll();
	    }
	    
	    public static void trackFromID() throws EchoNestException {
	    	EchoNestAPI en1;
	    	en1 = new EchoNestAPI("9QB1EM63CLM2RR5V3");
	    	en1.setTraceSends(false);
	    	en1.setTraceRecvs(false);
	        String id = "TRWXVPA1296187FC15";
	        Track track = en1.newTrackByID(id);
	        assertTrue("track status",
	                track.getStatus() == Track.AnalysisStatus.COMPLETE);
	        assertTrue("track id", track.getID().equals(id));
	        verifyTrack1(track);
	    }
	    
	 /**
	  * Título da Música
	  * Artista/Banda
	  * Duração da música em segundos
	  * BPM da música (quantas batidas por minuto)
	  * Localização do artista
	  * Quanto dançante a música é
	  * Quanto enérgica a música é
	  * @param song
	  * @throws EchoNestException
	  */
	 public static void dumpSong(Song song) throws EchoNestException 
	 {
	        System.out.printf("%s\n", song.getTitle());
	        System.out.printf("   Banda : %s\n", song.getArtistName());
	        System.out.printf("   Duração (segundos)   : %.3f\n", song.getDuration());
	        System.out.printf("   BPM (Batidas por minuto)  : %.3f\n", song.getTempo());
	        System.out.printf("   Localização da banda : %s\n", song.getArtistLocation());
	        System.out.printf("   Danc : %.3f\n", song.getDanceability());
	        System.out.printf("   Ener : %.3f\n", song.getEnergy());
	 }

	public static void main(String[] args)
	{
		
		//procuraMusicaa();
		
		//procurarMusica();
		procuraArtista();
	}
}
