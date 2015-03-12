/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.processing;

import javax.ejb.ApplicationException;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */

@ApplicationException
public class ProcessingException extends Exception {
    
   public ProcessingException(String msg) {
        super(msg);
    }
   
    public ProcessingException(Throwable t) {
        super(t);
    }
    
 
}
