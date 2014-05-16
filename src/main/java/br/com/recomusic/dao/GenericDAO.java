package br.com.recomusic.dao;
 
import java.lang.reflect.ParameterizedType;
import java.util.List;
 
import javax.persistence.EntityManager;
 
/**
 * @author Guilherme
 * 
 *
 */
 
@SuppressWarnings("unchecked")
public class GenericDAO<PK, T>
{
    private EntityManager entityManager;
 
    public GenericDAO(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }
 
    public T getById(PK pk)
    {
        return (T) entityManager.find(getTypeClass(), pk);
    }
 
    public void save(T entity)
    {
        entityManager.persist(entity);
    }
 
    public void update(T entity)
    {
        entityManager.merge(entity);
    }
 
    public void delete(T entity)
    {
        entityManager.remove(entity);
    }
 
    public List<T> findAll()
    {
        return entityManager.createQuery(("FROM " + getTypeClass().getName()))
                .getResultList();
    }
 
    private Class<?> getTypeClass()
    {
        Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
        return clazz;
    }
}