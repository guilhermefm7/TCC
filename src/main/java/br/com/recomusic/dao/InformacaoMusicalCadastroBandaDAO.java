package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Banda;
import br.com.recomusic.om.InformacaoMusicalCadastroBanda;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class InformacaoMusicalCadastroBandaDAO extends GenericDAO<Long, InformacaoMusicalCadastroBanda>
{
    public InformacaoMusicalCadastroBandaDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * @author Guilherme
     * Salva uma InformacaoMusicalCadastroBanda no banco de dados
     * @param InformacaoMusicalCadastroBanda
     * @throws Exception
     */
    public void salvarBandasCadastro(InformacaoMusicalCadastroBanda imcb) throws Exception
    {
    	try
    	{
    		InformacaoMusicalCadastroBandaDAO.this.save(imcb);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
    
    /**
     * @author Guilherme
     * Pesquisa se a InformacaoMusicalCadastroBanda existe através da Banda e Usuario passados como parâmetros
     * @param Usuario
     * @param Banda
     * @return  InformacaoMusicalCadastroBanda caso exista ou null caso não exista
     * @throws Exception
     */
    public InformacaoMusicalCadastroBanda pesquisarIMCB(Usuario usuario, Banda banda) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.InformacaoMusicalCadastroBanda as imcBanda where imcBanda.usuario.pkUsuario = :pk_usuario AND imcBanda.banda.pkBanda = :pk_banda"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		query.setParameter("pk_banda", banda.getPkBanda());
    		InformacaoMusicalCadastroBanda imcb = (InformacaoMusicalCadastroBanda) query.getSingleResult();
    	    return imcb;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	} 
    }
    
    public List<Banda> procurarBandasUsuario(Usuario usuario) throws Exception
    {
    	try
    	{
    		List<Banda> listaBandas = new ArrayList<Banda>();
    		//Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.InformacaoMusicalCadastroBanda as imcb where imcb.fkUsuario = :usuario_pkUsuario"));
    		//query.setParameter("usuario_pkUsuario", usuario.getPkUsuario());
    		List<InformacaoMusicalCadastroBanda> imcb = findAll();
    		
    		if(imcb!=null && imcb.size()>0)
    		{
	    		for (InformacaoMusicalCadastroBanda informacaoMusicalCadastroBanda : imcb)
	    		{
	    			if(informacaoMusicalCadastroBanda.getUsuario()!=null && informacaoMusicalCadastroBanda.getUsuario().getPkUsuario()>0 && informacaoMusicalCadastroBanda.getUsuario().getPkUsuario()==usuario.getPkUsuario())
	    			{
	    				listaBandas.add(informacaoMusicalCadastroBanda.getBanda());
	    			}
				}
    		}
    		
    		return listaBandas;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	}  
    }
}