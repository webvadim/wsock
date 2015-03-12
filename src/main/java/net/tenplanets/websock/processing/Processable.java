/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.processing;

import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public interface Processable {
   
    public JSONObject process (JSONObject msg) throws ProcessingException;
        
    
}
