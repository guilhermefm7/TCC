package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.AvaliarMusica;
import br.com.recomusic.om.BandaGenero;
import br.com.recomusic.om.Genero;
import br.com.recomusic.om.Musica;
import br.com.recomusic.om.MusicaGenero;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Constantes;
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
    
    /**
     * Salva um registro AvaliarMusica passado como parâmetro
     * @param avaliarMusica
     * @throws Exception
     */
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
    
    /**
     * Autor: Guilherme
     * Procura a AvaliarMusica do Usuário e Musica Passados como Parâmetro
     * Parametro String idMusicaDeezer
     * Usuario usuario
     * True caso seja verdadeiro e false caso contrário
     */
    public AvaliarMusica pesquisaUsuarioAvaliouMusica(String idMusicaDeezer, Usuario usuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AvaliarMusica as am where am.usuario.pkUsuario = :pk_usuario AND am.musica.idDeezer = :id_deezer AND am.status = :status_ativo"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		query.setParameter("id_deezer", idMusicaDeezer);
    		query.setParameter("status_ativo", Constantes.TIPO_STATUS_ATIVO);
    		AvaliarMusica am =  (AvaliarMusica) query.getSingleResult();
    		return am;
    	}
    	catch ( NoResultException nre )
    	{  
            return null;  
        }  
    }
    
    /**
     * Autor: Guilherme
     * Procura se já existe uma AvaliarMusica da música e usuário passados como parâmetro
     * Musica musica
     * Usuario usuario
     * AvaliarMusica caso exista e null caso não exista
     */
    public AvaliarMusica pesquisaUsuarioAvaliouMusicaPelaMusica(Musica musica, Usuario usuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AvaliarMusica as am where am.usuario.pkUsuario = :pk_usuario AND am.musica.pkMusica = :pk_musica"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		query.setParameter("pk_musica", musica.getPkMusica());
    		AvaliarMusica am = (AvaliarMusica) query.getSingleResult();
    		return am;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
	/**
	 * Autor: Guilherme
	 * Pesquisa todas as músicas avalidas pelo um usuário passado como parâmetro, cuja nota seja maior que 3 e o gênero seja igual ao gênero passado como parâmetro
	 * Usuario usuario
	 * Genero genero
	 * List<AvaliarMusica> caso exista senão retorna null
	 */
	public List<AvaliarMusica> pesquisaAvaliacaoUsuarioMaior3(Usuario usuario, Genero genero) throws Exception
	{
		List<AvaliarMusica> listaAux;
		try
		{
			Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AvaliarMusica as am where am.usuario.pkUsuario = :pk_usuario AND am.nota >= 3 ORDER by am.nota DESC"));
			query.setParameter("pk_usuario", usuario.getPkUsuario());
			List<AvaliarMusica> am = (List<AvaliarMusica>) query.getResultList();
			
			listaAux = new ArrayList<AvaliarMusica>();
			
			for (AvaliarMusica avaliarMusica : am)
			{
				for (BandaGenero bg : avaliarMusica.getMusica().getBanda().getBandaGeneros())
				{
					if(genero.getPkGenero()==bg.getGenero().getPkGenero())
					{
						listaAux.add(avaliarMusica);
						break;
					}
				}
			}
			
			return listaAux;
		}
		catch ( NoResultException nre )
		{  
			return null;  
		}  
	}
	
	/**
	 * Autor: Guilherme
	 * Pesquisa todas as músicas avalidas pelo um usuário passado como parâmetro, cujo o gênero seja igual ao gênero passado como parâmetro
	 * Usuario usuario
	 * Genero genero
	 * List<Musica> caso exista senão retorna null
	 */
	public List<Musica> pesquisaAlMusicasUsuarioGenero(Usuario usuario, Genero genero) throws Exception
	{
		List<Musica> listaAux;
		try
		{
			Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AvaliarMusica as am where am.usuario.pkUsuario = :pk_usuario"));
			query.setParameter("pk_usuario", usuario.getPkUsuario());
			List<AvaliarMusica> am = (List<AvaliarMusica>) query.getResultList();
			
			listaAux = new ArrayList<Musica>();
			
			for (AvaliarMusica avaliarMusica : am)
			{
				for (BandaGenero bg : avaliarMusica.getMusica().getBanda().getBandaGeneros())
				{
					if(genero.getPkGenero()==bg.getGenero().getPkGenero())
					{
						listaAux.add(avaliarMusica.getMusica());
						break;
					}
				}
			}
			
			return listaAux;
		}
		catch ( NoResultException nre )
		{  
			return null;  
		}  
	}
    
    /**
     * Autor: Guilherme
     * Procura todas as música que o usuário passado como parâmetro curtiu
     * Usuario usuario
     * lista com as Musicas
     */
    public List<Musica> getAvaliacoesUsuario(Usuario usuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AvaliarMusica as am where am.usuario.pkUsuario = :pk_usuario AND am.resposta = true"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		List<AvaliarMusica> listaAM = (List<AvaliarMusica>) query.getResultList();
    		
    		if(listaAM!=null && listaAM.size()>0)
    		{
    			List<Musica> listaM = new ArrayList<Musica>();
    			
    			for (AvaliarMusica am : listaAM)
    			{
    				if(am.getResposta() && am.getMusica()!=null && am.getMusica().getPkMusica()>0)
    				{
    					listaM.add(am.getMusica());
    				}
    			}
    			
    			return listaM;
    		}
    		else
    		{
    			return null;
    		}
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    /**
     * Autor: Guilherme
     * Procura todas as música que o usuário passado como parâmetro curtiu ordenado pela data de avaliacao
     * Usuario usuario
     * lista com as Musicas
     */
    public List<Musica> getAvaliacoesUsuarioPorData(Usuario usuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AvaliarMusica as am where am.usuario.pkUsuario = :pk_usuario AND am.resposta = true ORDER BY am.lancamento DESC"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		List<AvaliarMusica> listaAM = (List<AvaliarMusica>) query.getResultList();
    		
    		if(listaAM!=null && listaAM.size()>0)
    		{
    			List<Musica> listaM = new ArrayList<Musica>();
    			
    			for (AvaliarMusica am : listaAM)
    			{
    				if(am.getResposta() && am.getMusica()!=null && am.getMusica().getPkMusica()>0)
    				{
    					listaM.add(am.getMusica());
    				}
    			}
    			
    			return listaM;
    		}
    		else
    		{
    			return null;
    		}
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    /**
     * Autor: Guilherme
     * Procura todas as música que o usuário avaliou
     * Usuario usuario
     * lista com as Musicas
     */
    public List<Musica> getAllAvaliacoesUsuario(Usuario usuario) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.AvaliarMusica as am where am.usuario.pkUsuario = :pk_usuario"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		List<AvaliarMusica> listaAM = (List<AvaliarMusica>) query.getResultList();
    		
    		if(listaAM!=null && listaAM.size()>0)
    		{
    			List<Musica> listaM = new ArrayList<Musica>();
    			
    			for (AvaliarMusica am : listaAM)
    			{
    				if(am.getResposta() && am.getMusica()!=null && am.getMusica().getPkMusica()>0)
    				{
    					listaM.add(am.getMusica());
    				}
    			}
    			
    			return listaM;
    		}
    		else
    		{
    			return null;
    		}
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
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
    						avaliarMusica.getMusica().getIdMusica().equals(valorIdMusic))
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