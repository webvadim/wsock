/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.data;



import java.util.List;
import javax.persistence.EntityExistsException;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 * 
 * This class should be implemented using JPA EntityManager functions.
 */

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
   

    public void create(T entity) throws EntityExistsException  {   
         throw new UnsupportedOperationException("Not supported yet.");
    }

    public void edit(T entity) {  
         throw new UnsupportedOperationException("Not supported yet.");
    }

    public void remove(T entity) {   
         throw new UnsupportedOperationException("Not supported yet.");
    }
 

    public T find(Object id) {
            throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<T> findAll() {        
         throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<T> findRange(int[] range) {
          throw new UnsupportedOperationException("Not supported yet.");
    }

    public int count() {
       throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<T> findAll(List<?> ids) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
