/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock;

import javax.websocket.server.ServerEndpoint;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.ObjectMessage;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import net.tenplanets.websock.processing.Processable;
import net.tenplanets.websock.processing.ProcessorFactory;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
@ServerEndpoint(value = "/datainp", decoders = {JsonDecoder.class}, encoders = {JsonEncoder.class})
public class InSock  {

    private static final Logger logger = Logger.getLogger(InSock.class.getName());

    private static final Queue<Session> peers = new ConcurrentLinkedQueue<Session>();

    private static final ProcessorFactory processorFactory = new ProcessorFactory();

    /*       
     @Resource(lookup = "jms/websockConnectionFactory")
     private  ConnectionFactory connectionFactory;
     @Resource(lookup = "jms/websockTopic")
     private Topic topic;
     */
    

    protected void closePeers() {
        Iterator<Session> it = peers.iterator();
        while (it.hasNext()) {
            Session session = it.next();
            if (session.isOpen()) {
                try {
                    session.close();
                    it.remove();
                } catch (Exception e) {

                }
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
    
        //Glassfish has a bug which doesn't affect the work of websocket. This bug clutter the log. Need to set loglevel of org.glassfish.tyrus.servlet.TyrusHttpUpgradeHandler to SEVERE
        logger.log(Level.FINE, "WebSocket connection opened: {0}", session.getId());
       

        if (!peers.contains(session)) {//Bug in Glassfish. Can be calld twice
             peers.add(session);           
          
        }

    }

    @OnMessage
    public void message(Session session, JSONObject msg) {

        try {
            if (session.isOpen()) {

                try (JMSContext context = ApplicationListener.getConnectionFactory().createContext();) {

                    Processable procesor = processorFactory.getDummyProcessor();
                    JSONObject result = procesor.process(msg);

                    msg.put("message", "Ok");
                    msg.put("result", 0);
                    session.getAsyncRemote().sendObject(msg);

                    ObjectMessage obj = context.createObjectMessage(result);
                    context.createProducer().send(ApplicationListener.getTopic(), obj);

                } catch (Exception ex) {

                    msg.put("message", ex.getMessage());
                    msg.put("result", -1);
                    session.getAsyncRemote().sendObject(msg);
                    logger.log(Level.FINE, "WebSocket error processing message '{2}' in session {0} with error: {1}", new Object[]{session.getId(), ex.getMessage(), msg.toString()});
                }

            }

        } catch (Exception ex) {
            logger.log(Level.WARNING, "WebSocket error in session {0} with error: {1}", new Object[]{session.getId(), ex.getMessage()});
        }

    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        peers.remove(session);
        logger.log(Level.FINE, "WebSocket connection closed: {0}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable t) {
    
        if (t instanceof IllegalStateException && t.getMessage().equals("Text MessageHandler already registered.")) {
            return;
        }

        logger.log(Level.WARNING, "WebSocket error in session {0} with message: {1}", new Object[]{session.getId(), t.getMessage()});

    }
}
