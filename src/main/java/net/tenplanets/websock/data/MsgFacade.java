/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.data;



import javax.persistence.EntityExistsException;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */


    public class MsgFacade extends AbstractFacade<Msg>  {

    public MsgFacade() {
        super(Msg.class);
    }
  
      
    
    @Override
    public void create(Msg entity) throws EntityExistsException  {   
        //Just emulate db operation.
    }
    

}
