package br.com.recomusic.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.Genero;
import br.com.recomusic.om.InformacaoMusicalCadastroGenero;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;
 
/**
 * @author Guilherme
 *
 *
 */
public class InformacaoMusicalCadastroGeneroDAO extends GenericDAO<Long, InformacaoMusicalCadastroGenero>
{
    public InformacaoMusicalCadastroGeneroDAO(EntityManager entityManager)
    {
        super(entityManager);
    }
    
    /**
     * @author Guilherme
     * Pesquisa se a InformacaoMusicalCadastroGenero existe através do Usuario e Genero passados como parâmetros
     * @param Usuario
     * @param genero
     * @return  InformacaoMusicalCadastroGenero caso exista ou null caso não exista
     * @throws Exception
     */
    public InformacaoMusicalCadastroGenero pesquisarIMCG(Usuario usuario, Genero genero) throws Exception
    {
    	try
    	{
    		Query query = ConectaBanco.getInstance().getEntityManager().createQuery(("FROM br.com.recomusic.om.InformacaoMusicalCadastroGenero as imcGenero where imcGenero.usuario.pkUsuario = :pk_usuario AND imcGenero.genero.pkGenero = :pk_genero"));
    		query.setParameter("pk_usuario", usuario.getPkUsuario());
    		query.setParameter("pk_genero", genero.getPkGenero());
    		InformacaoMusicalCadastroGenero imcg = (InformacaoMusicalCadastroGenero) query.getSingleResult();
    	    return imcg;
    	}
    	catch ( NoResultException nre )
    	{  
    		return null;  
    	} 
    }
    
    
    /**
     * @author Guilherme
     * Salva uma InformacaoMusicalCadastroGenero no banco de dados
     * @param InformacaoMusicalCadastroGenero
     * @throws Exception
     */
    public void salvarIMCG(InformacaoMusicalCadastroGenero imcg) throws Exception
    {
    	try
    	{
    		InformacaoMusicalCadastroGeneroDAO.this.save(imcg);
    	}
    	catch ( NoResultException nre )
    	{  
    		nre.printStackTrace();
    		ConectaBanco.getInstance().rollBack(); 
    	}  
    }
}