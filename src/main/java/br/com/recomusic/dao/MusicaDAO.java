package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Musica;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class MusicaDAO extends GenericDAO<Long, Musica>
{
    public MusicaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * Salva uma música no banco de dados
     * @param musica
     * @throws Exception
     */
    public void salvarMusica(Musica musica) throws Exception
    {
    	try
    	{
    		MusicaDAO.this.save(musica);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
    
    /**
     * Procura uma música através de seu idDeezer passado como parâmetro
     * @param String idMusicaDeezer
     * return a música ou nulo caso não ache nada
     * @throws Exception
     */
    public Musica procuraMusicaByID(String idMusicaDeezer) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Musica as m where m.idDeezer = :musica_id"));
    		query.setParameter("musica_id", idMusicaDeezer);
    		Musica musica =  (Musica) query.getSingleResult();
    		return musica;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    /**
     * Procura as musicas mais bem avaliadas do sistemas de acordo com a quantidade passada como pârametro
     * @param int quantidade
     * return List<Musica> listaMusicas
     * @throws Exception
     */
    public List<Musica> pesquisaMelhoresAvaliadas() throws Exception
    {
    	try
    	{
    		List<Musica> musicaAux = new ArrayList<Musica>();
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.Musica as m where 1 = 1 order by m.mediaAvaliacoes DESC, m.quantidadeAvaliacoes DESC"));
    		List<Musica> musica = (List<Musica>) query.getResultList();
    		
    		for (Musica musica2 : musica) {
    			
    			if(musicaAux.size()==15)
    			{
    				for (int i = musicaAux.size()-1; i >= 0 ; i--) {
						if((musica2.getMediaAvaliacoes()*musica2.getQuantidadeAvaliacoes()) > (musicaAux.get(i).getMediaAvaliacoes()*musicaAux.get(i).getQuantidadeAvaliacoes()))
						{
							musicaAux.set(i, musica2);
							break;
						}
					}
    			}
    			else
    			{
    				musicaAux.add(musica2);
    			}
				
			}
    		
    		return musicaAux;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
}