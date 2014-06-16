package br.com.recomusic.dao;
 
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.recomusic.om.AvaliarMusica;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class AvaliarMusicaDAO extends GenericDAO<Long, AvaliarMusica>
{
    public AvaliarMusicaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    public void salvarAvaliacao(AvaliarMusica avaliarMusica) throws Exception
    {
    	try
    	{
    		AvaliarMusicaDAO.this.save(avaliarMusica);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
    
    public AvaliarMusica getAvaliacaoUsuario(Musica musica, Usuario usuario, int verificaCurtiu) throws Exception
    {
    	try
    	{
    		List<AvaliarMusica> listaAM = findAll();
    		AvaliarMusica am = null;
    		for (AvaliarMusica avaliarMusica : listaAM)
    		{
				if(avaliarMusica.getUsuario()!=null && avaliarMusica.getUsuario().getPkUsuario()>0 && 
						avaliarMusica.getMusica()!=null && avaliarMusica.getMusica().getPkMusica()>0 &&
						avaliarMusica.getUsuario().getPkUsuario()==usuario.getPkUsuario() && 
						avaliarMusica.getMusica().getPkMusica()==musica.getPkMusica())
				{
					am = avaliarMusica;
				}
			}
    		
    		if(am != null)
    		{
    			if(am.getResposta()!=null)
    			{
    				if(am.getResposta()==true)
    				{
    					if(verificaCurtiu==1)
    					{
    						am.setResposta(null);
    					}
    					else if(verificaCurtiu==2)
    					{
    						am.setResposta(false);
    					}
    				}
    				else if(am.getResposta()==false)
    				{
    					if(verificaCurtiu==1)
    					{
    						am.setResposta(true);
    					}
    					else if(verificaCurtiu==2)
    					{
    						am.setResposta(null);
    					}
    				}
    			}
    			else 
    			{
					if(verificaCurtiu==1)
					{
						am.setResposta(true);
					}
					else if(verificaCurtiu==2)
					{
						am.setResposta(false);
					}
    			}
    			
    			this.save(am);
    			return am;
    		}
    		else
    		{
    			am = new AvaliarMusica();
    			am.setUsuario(usuario);
    			am.setMusica(musica);
    			
				if(verificaCurtiu==1)
				{
					am.setResposta(true);
				}
				else if(verificaCurtiu==2)
				{
					am.setResposta(false);
				}
				
    			this.save(am);
    			return am;
    		}
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		return null;
    	}  
    }
    
    public Boolean pesquisaAvaliacaoUsuario(String valorIdMusic, Usuario usuario) throws Exception
    {
    	try
    	{
    		List<AvaliarMusica> listaAM = findAll();
    		
    		if(listaAM!=null)
    		{
    			for (AvaliarMusica avaliarMusica : listaAM)
    			{
    				if(avaliarMusica.getUsuario()!=null && avaliarMusica.getUsuario().getPkUsuario()>0 && 
    						avaliarMusica.getMusica()!=null && avaliarMusica.getMusica().getPkMusica()>0 &&
    						avaliarMusica.getUsuario().getPkUsuario()==usuario.getPkUsuario() && 
    						avaliarMusica.getMusica().getIdMUsica()==valorIdMusic)
    				{
    					return avaliarMusica.getResposta();
    				}
    			}
    		}
    		
    		return null;
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		return null;
    	}  
    }
}