/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class JsonDecoder implements Decoder.Text<JSONObject> {

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public JSONObject decode(String string) throws DecodeException {

        try {
            JSONObject content = new JSONObject(string);

            return content;

        } catch (JSONException ex) {
            throw new DecodeException(string, ex.getMessage());
        }

    }

    @Override
    public boolean willDecode(String string) {
        string = string.trim();
        return (string.startsWith("{") && string.endsWith("}"));
    }
}
