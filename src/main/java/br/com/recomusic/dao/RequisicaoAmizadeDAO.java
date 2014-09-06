package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.RequisicaoAmizade;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class RequisicaoAmizadeDAO extends GenericDAO<Long, RequisicaoAmizade>
{
    public RequisicaoAmizadeDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * Procura os usuários que requisitaram amizade do usuário passado como parâmetro
     * @param pkUsuario
     * @return List<Usuario>
     * @throws Exception
     */
    public List<Usuario> getRequisicoesAmizadeUsuario(long pkUsuario) throws Exception
    {
    	try
    	{
    		List<Usuario> listaUsuario = null;
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.RequisicaoAmizade as ra where ra.usuarioRequisitado.pkUsuario = :usuario_pkUsuario AND ra.resposta IS NULL ORDER BY ra.lancamento DESC"));
    		query.setParameter("usuario_pkUsuario", pkUsuario);
    		List<RequisicaoAmizade> listaRA =  (List<RequisicaoAmizade>) query.getResultList();
    		
    		if(listaRA!=null && listaRA.size()>0)
    		{
    			listaUsuario = new ArrayList<Usuario>();
    			for (RequisicaoAmizade ra : listaRA)
    			{
    				listaUsuario.add(ra.getUsuarioRequisitante());
				}
    		}
    		
    		return listaUsuario;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    /**
     * Salva a resposta da requisicao de amizade
     * @param pkUsuario
     * @return Caso tenha sido salvo com sucesso returna true senão retorna null
     * @throws Exception
     */
    public Boolean salvaRespostaRequisicao(Usuario usuarioRequisitado, Usuario usuarioRequisitou, boolean adicionou) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.RequisicaoAmizade as ra where ra.usuarioRequisitado.pkUsuario = :usuario_requisitado AND ra.usuarioRequisitante.pkUsuario = :usuario_requisitante"));
    		query.setParameter("usuario_requisitado", usuarioRequisitado.getPkUsuario());
    		query.setParameter("usuario_requisitante", usuarioRequisitou.getPkUsuario());
    		RequisicaoAmizade ra =  (RequisicaoAmizade) query.getSingleResult();
    	
    		if(ra!=null && ra.getPkRequisicaoAmizade()>0)
    		{
    			ra.setResposta(adicionou);
    		}
    		
    		this.salvaRequisicaoBD(ra);
    		
    		return true;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
    
    /**
     * Salva a resposta da requisicao de amizade no BD
     * @param RequisicaoAmizade ra
     * @return
     * @throws Exception
     */
    public void salvaRequisicaoBD(RequisicaoAmizade ra) throws Exception
    {
    	try
    	{
    		this.save(ra);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
}