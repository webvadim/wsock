/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock;

import java.io.IOException;
import java.util.Date;
import javax.websocket.server.ServerEndpoint;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.HandshakeResponse;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import net.tenplanets.websock.OutSock.CustomConfigurator;
import net.tenplanets.websock.data.Stat;
import net.tenplanets.websock.data.StatFacade;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
@ServerEndpoint(value = "/dataout", configurator = CustomConfigurator.class, decoders = {JsonDecoder.class}, encoders = {JsonEncoder.class})
public class OutSock {

    private static final Logger logger = Logger.getLogger(OutSock.class.getName());

    private static final Queue<Session> peers = new ConcurrentLinkedQueue<Session>();

    private static JMSConsumer consumer;

    private static volatile boolean inited;

    public OutSock() {
        super();
        doinit();
    }

    public static class CustomConfigurator extends ServerEndpointConfig.Configurator {

        @Override
        public void modifyHandshake(ServerEndpointConfig conf, HandshakeRequest req, HandshakeResponse resp) {
            logger.log(Level.FINE, "WebSocket handshake on URI {0} from origin {1}", new Object[]{req.getRequestURI(), req.getHeaders().get("origin")});
        }
    }

    @PostConstruct   //Glassfish has a bug in processing annotations. @Resource ,@PostConstruct, @PostDestroy don't work...
    private void doinit() {
        if (!inited) {
            synchronized (this) {
                if (!inited) {
                    inited = true;
                    init();
                }
            }
        }
    }

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

    protected void init() {

        logger.log(Level.FINE, "WebSocket {0} initialization...", OutSock.class.getName());

        JMSContext context = ApplicationListener.getConnectionFactory().createContext();
        consumer = context.createConsumer(ApplicationListener.getTopic());

        ExecutorService es = Executors.newSingleThreadExecutor();

        ApplicationListener.registerExecutorService(es);  //Register for shutdown.
        es.execute(new Runnable() {
            @Override
            public void run() {

                while (!(Thread.currentThread().isInterrupted() || ApplicationListener.contextDestroying())) {

                    Message msg = null;
                    try {
                        msg = consumer.receive(15000);
                    } catch (Exception ex) {
                    }
                    if (msg != null) {

                        try {

                            Object obj = ((ObjectMessage) msg).getObject();

                            if (obj instanceof JSONObject) {

                                JSONObject o = (JSONObject) obj;

                                Iterator<Session> it = peers.iterator();

                                while (it.hasNext()) {
                                    Session session = it.next();
                                    if (session.isOpen()) {
                                        try {
                                            JSONObject trans = o.getJSONObject("trans");
                                            trans.put("msgType", 1);
                                            JSONObject stat = o.getJSONObject("stat");
                                            trans.put("counter", stat.getLong("counter"));

                                            if (!Thread.currentThread().isInterrupted()) {

                                                if (!session.getUserProperties().containsKey("init")) {

                                                    stat.put("msgType", 2);
                                                    session.getBasicRemote().sendObject(stat);
                                                    session.getBasicRemote().sendObject(trans);
                                                    session.getUserProperties().put("init", true);

                                                } else {
                                                    session.getAsyncRemote().sendObject(trans);
                                                }
                                            }

                                        } catch (Exception e) {
                                            logger.log(Level.WARNING, "WebSocket unknown error in message processing: " + e.getMessage(), e);
                                        }
                                    } else {
                                        it.remove();
                                    }
                                }

                            } else {
                                logger.log(Level.WARNING, "WebSocket unknown JMS message type: {0}.", obj.getClass().toString());
                            }

                        } catch (Exception ex) {

                            Object[] o = new Object[2];
                            o[0] = (ex.getCause() == null) ? ex.getMessage() : ex.getMessage() + " / " + ex.getCause().getMessage();
                            o[1] = ex.getClass().toString();

                            logger.log(Level.WARNING, "WebSocket error processing JMS message: {0},  Exception class: {1}", o);

                        }

                    }

                }
                if (ApplicationListener.contextDestroying()) {
                    destroy();
                }

            }
        });

    }

    protected void destroy() {
        closePeers();
        consumer.close();
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {//throws EncodeException {     

        if (!peers.contains(session)) {//Bug in Glassfish. Can be calld twice. This check is workaround for Windows only. 
            peers.add(session);
            logger.log(Level.FINE, "WebSocket connected: {0}", session.getId());

            // In order to populate graph by initial values I send a messsage. Since state of initial graph can cange before the client begin recieving messages,
            //this initial graph willbe redrawn by the values from the first JMS message in order to be consists with all transactions.
            StatFacade statFacade = new StatFacade();
            Stat stat = statFacade.find(new Date());

            if (stat != null) {
                try {
                    JSONObject obj = stat.toJSON();
                    obj.put("msgType", 2);
                    try {
                        session.getBasicRemote().sendObject(obj);
                    } catch (EncodeException ex) {
                    }

                } catch (IOException ex) {
                    logger.log(Level.FINE, "WebSocket connection error: {0}", session.getId());
                } catch (JSONException ex) {//Never thrown
                }

            }
        }

    }

    @OnMessage
    public void message(Session session, JSONObject msg) {
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        peers.remove(session);
        logger.log(Level.FINE, "WebSocket connection was closed: {0}, active connections count: {1}", new Object[]{session.getId(), session.getOpenSessions().size()});
    }

    @OnError
    public void onError(Session session, Throwable t) {
        //Glassfish has a bug. This bug clutter the log. 
        //Need to set loglevel of org.glassfish.tyrus.servlet.TyrusHttpUpgradeHandler to SEVERE      
        if (t instanceof IllegalStateException && t.getMessage().equals("Text MessageHandler already registered.")) {
            return;
        }

        logger.log(Level.WARNING, "WebSocket error in session {0} with message: {1}", new Object[]{session.getId(), t.getMessage()});

    }

}
