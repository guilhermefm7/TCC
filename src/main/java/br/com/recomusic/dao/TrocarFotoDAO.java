package br.com.recomusic.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.recomusic.om.TrocarFoto;
import br.com.recomusic.om.Usuario;
import br.com.recomusic.singleton.ConectaBanco;

/**
 * @author Guilherme
 *
 *
 */
public class TrocarFotoDAO extends GenericDAO<Long, TrocarFoto> {
	public TrocarFotoDAO(EntityManager entityManager) {
		super(entityManager);
	}

	/**
	 * Autor: Guilherme Procura o objeto TrocarFoto do Usuario passado como parâmetro Usuario usuario lista com as Playlists
     * @param Usuario usuario
     * @throws Exception
     * return TrocarFoto em caso ele tenha sido achado ou null caso não exista
	 */
	public TrocarFoto getTrocarFotoUsuario(Usuario usuario) throws Exception {
		try {
			TrocarFoto trocarFoto = null;
			Query query = ConectaBanco
					.getInstance()
					.getEntityManager()
					.createQuery(
							("FROM br.com.recomusic.om.TrocarFoto as t where t.usuario.pkUsuario = :pk_usuario"));
			query.setParameter("pk_usuario", usuario.getPkUsuario());
			trocarFoto = (TrocarFoto) query.getSingleResult();

			return trocarFoto;
		} catch (NoResultException nre) {
			return null;
		}
	}
}