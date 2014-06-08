package br.com.recomusic.singleton;

import java.util.HashMap;

public final class GuardaAlteracoesSenhas
{
	private static HashMap<String, Long> hashTokens = null;
	
	private GuardaAlteracoesSenhas(){	}
	
	public static synchronized HashMap<String, Long> getTokensAlteracaoSenhaUsuario()
	{
		if(hashTokens == null)
		{
			hashTokens = new HashMap<String, Long>();
		}
		return hashTokens;
	}
}