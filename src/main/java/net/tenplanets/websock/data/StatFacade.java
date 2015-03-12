/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.data;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityExistsException;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class StatFacade extends AbstractFacade<Stat> {

    //Emulate database table     
    private static final Map<Date, Stat> map = Collections.synchronizedMap(new HashMap<Date, Stat>());

   
    
    public StatFacade() {
        super(Stat.class);
        
    }

    @Override
    public void create(Stat obj) throws EntityExistsException {
        synchronized (map) {
            if (map.containsKey(obj.getId())) {
                throw new EntityExistsException("Entry is already exists");
            }
            map.put(obj.getId(), obj);
        }

    }

    @Override
    public Stat find(Object id) {
        if (id instanceof Date) {
            return map.get(Stat.removeTime((Date) id));
        } else {
            return null;
        }
    }

    public Stat findOrCreate(Date date) {
        Stat stat = find(date);
        if (stat == null) {
            stat = new Stat(date);          
            try {
                create(stat);
            } catch (EntityExistsException e) {
                stat = find(date);
            }
        }
        return stat;
    }

}
