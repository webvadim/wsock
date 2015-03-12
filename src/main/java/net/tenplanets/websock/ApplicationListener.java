/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Topic;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class ApplicationListener implements ServletContextListener {

    @Resource(lookup = "jms/websockConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/websockTopic")
    private Topic topic;

    private static Topic _topic;
    private static ConnectionFactory _connectionFactory;
    private static final List<ExecutorService> _executors = new ArrayList<ExecutorService>();
    private static volatile boolean _destroing;

    public static ConnectionFactory getConnectionFactory() {
        return _connectionFactory;
    }

    public static Topic getTopic() {
        return _topic;
    }

    public static void registerExecutorService(ExecutorService es) {
        _executors.add(es);
    }

    public static boolean contextDestroying() {
        return _destroing;
    }

   
    @Override
    public void contextInitialized(ServletContextEvent sce) {       
         
        _topic = topic;
        _connectionFactory = connectionFactory;
        
         Logger.getLogger(ApplicationListener.class.getName()).log(Level.INFO,  "WebSocket application started.");
        
          
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        _destroing = true;
        for (ExecutorService es : _executors) {
            es.shutdown(); // Disable new tasks from being submitted
        }
        for (ExecutorService es : _executors) {
            awaitTermination(es);
        }
    }

    void awaitTermination(ExecutorService pool) {
        if (!pool.isTerminated()) {
            try {
                // Wait a while for existing tasks to terminate
                if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                pool.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }

}
