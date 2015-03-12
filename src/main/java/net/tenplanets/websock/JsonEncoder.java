/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock;


import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.codehaus.jettison.json.JSONObject; 

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class JsonEncoder implements Encoder.Text<JSONObject> {

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(JSONObject object) throws EncodeException {
      return  object.toString();
    }
}
   

   
