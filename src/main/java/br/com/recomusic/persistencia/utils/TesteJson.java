package br.com.recomusic.persistencia.utils;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
public class TesteJson
{
	public static void main(String[] args) throws JSONException , IOException
	{
		
/*		 String url = "http://developer.echonest.com/api/v4/song/search?api_key=9QB1EM63CLM2RR5V3&format=json&results=5&title=come as you are&bucket=id:deezer&bucket=tracks&limit=true&artist=Nirvana";
	     String json = IOUtils.toString(new URL(url));
	       
	     JSONObject my_obj = new JSONObject(json);
	     System.out.println(my_obj.toString());*/
		
		String s1 = "come as you are - ds";
		String array1[] = new String[10];
		array1 = s1.split("-"); 
		
		
 	   	//System.out.println("Array = " + array1[0].toString() + " dsds " + array1[1] );

		System.out.println("dsds "  + array1.length);
		

 	   	
 	   	System.out.println("Replace" + array1[0].replace(" ", "%"));
 	   	
		//Pegar Gênero da música
		
	       String url = "http://developer.echonest.com/api/v4/song/search?api_key=9QB1EM63CLM2RR5V3&format=json&title=come&bucket=id:deezer&bucket=tracks&limit=true&";
	       String json = IOUtils.toString(new URL(url));
	       JSONObject my_obj = new JSONObject(json);
	       JSONObject ob = (JSONObject)my_obj.get("response");
	      // JSONObject ob1 = (JSONObject)ob.get("songs");
	       System.out.println(ob);
	       String id = "";
	       JSONArray horarios =(JSONArray) ob.get("songs");
	       System.out.println(horarios);
	       for (int i = 0; i < horarios.length(); i++)
	       {
               JSONObject dado = horarios.getJSONObject(i);

               System.out.println(dado.get("id"));
               System.out.println(dado.get("title"));
               System.out.println(dado.get("artist_name"));
               
               JSONArray horarios1 =(JSONArray) dado.get("tracks");
               System.out.println("dfdsfsddfs " + horarios1);
    	       for (int x = 0; x < horarios1.length(); x++)
    	       {
    	    	   JSONObject dado1 = horarios1.getJSONObject(x);
    	    	   System.out.println("dsds" + dado1);
    	    	   System.out.println("IDDDDDD" + dado1.get("foreign_id"));
    	    	   String s = (String)dado1.get("foreign_id");
    	    	   String array[] = new String[3];
    	    	   array = s.split(":"); 
    	    	   System.out.println(array[2]);
    	    	   id = array[2].toString();
    	       }
               
               
               System.out.println();
               //System.out.println(horarios1);
               System.out.println();
	       }
	       
           System.out.println();
           //System.out.println(horarios1);
           System.out.println();
           System.out.println();
           //System.out.println(horarios1);
           System.out.println();
           System.out.println();
           //System.out.println(horarios1);
           System.out.println();
           System.out.println("IDDDDDDDD " + id);
           String url1 = "http://api.deezer.com/2.0/track/" + id + "?callback=?";
           String json1 = IOUtils.toString(new URL(url1));
           System.out.println(json1);
           JSONObject my_obj1 = new JSONObject(json1);
           System.out.println("ALBUMMM "  + ((JSONObject)my_obj1.get("album")).get("title"));
           System.out.println("ALBUMMM "  + ((JSONObject)my_obj1.get("album")).get("cover"));
/*           JSONObject ob11 = (JSONObject)my_obj1.get("album");
           System.out.println("Album " + ob11.get("title"));*/

           
	       /*JSONObject ob1 = (JSONObject)ob.get("artist");
	       JSONArray horarios =(JSONArray) ob1.get("genres");
	       
	       for (int i = 0; i < horarios.length(); i++)
	       {
               JSONObject dado = horarios.getJSONObject(i);
               System.out.println(dado.get("name"));
	       }*/
	       

	}
	
	/**
     * Handle an Object. Consume the first token which is BEGIN_OBJECT. Within
     * the Object there could be array or non array tokens. We write handler
     * methods for both. Noe the peek() method. It is used to find out the type
     * of the next token without actually consuming it.
     * 
     * @param reader
     * @throws IOException
     */
    private static void handleObject(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.BEGIN_ARRAY))
                handleArray(reader);
            else if (token.equals(JsonToken.END_ARRAY)) {
                reader.endObject();
                return;
            } else
                handleNonArrayToken(reader, token);
        }
 
    }
 
    /**
     * Handle a json array. The first token would be JsonToken.BEGIN_ARRAY.
     * Arrays may contain objects or primitives.
     * 
     * @param reader
     * @throws IOException
     */
    public static void handleArray(JsonReader reader) throws IOException {
        reader.beginArray();
        while (true) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
                break;
            } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
                handleObject(reader);
            } else
                handleNonArrayToken(reader, token);
        }
    }
 
    /**
     * Handle non array non object tokens
     * 
     * @param reader
     * @param token
     * @throws IOException
     */
    public static void handleNonArrayToken(JsonReader reader, JsonToken token) throws IOException {
        if (token.equals(JsonToken.NAME))
            System.out.println(reader.nextName());
        else if (token.equals(JsonToken.STRING))
            System.out.println(reader.nextString());
        else if (token.equals(JsonToken.NUMBER))
            System.out.println(reader.nextDouble());
        else
            reader.skipValue();
    }
}
