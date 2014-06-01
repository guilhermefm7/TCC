package br.com.recomusic.singleton;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

public final class ConectaFacebook
{
	private static FacebookClient facebookClient;
	
	private ConectaFacebook()
	{
        	getInstance();
	}
	
	public static synchronized FacebookClient getInstance()
	{
		if(facebookClient==null)
		{
			facebookClient = new DefaultFacebookClient("577169682402524", "d25b5d2c24c2c27c922f3354fd3c1db5");
		}
		return facebookClient;
	}
	
}