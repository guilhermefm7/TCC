package br.com.recomusic.dao;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.recomusic.om.Banda;
import br.com.recomusic.om.InformacaoMusicalCadastroBanda;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.persistencia.utils.Constantes;
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
    
    public void salvarBandasCadastro(List<Banda> listaBandas, Usuario usuario) throws Exception
    {
    	try
    	{
    		InformacaoMusicalCadastroBanda imcb;
    		for (Banda banda : listaBandas)
    		{
    			imcb = new InformacaoMusicalCadastroBanda();
    			imcb.setBanda(banda);
    			imcb.setUsuario(usuario);
    			imcb.setStatus(Constantes.TIPO_STATUS_ATIVO);
    			InformacaoMusicalCadastroBandaDAO.this.save(imcb);
			}
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
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
    		
    		for (InformacaoMusicalCadastroBanda informacaoMusicalCadastroBanda : imcb)
    		{
    			if(informacaoMusicalCadastroBanda.getUsuario()!=null && informacaoMusicalCadastroBanda.getUsuario().getPkUsuario()>0 && informacaoMusicalCadastroBanda.getUsuario().getPkUsuario()==usuario.getPkUsuario())
    			{
    				listaBandas.add(informacaoMusicalCadastroBanda.getBanda());
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