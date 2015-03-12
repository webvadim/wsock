/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.processing;

import java.text.ParseException;
import net.tenplanets.websock.data.Msg;
import net.tenplanets.websock.data.MsgFacade;
import net.tenplanets.websock.data.Stat;
import net.tenplanets.websock.data.StatEntry;
import net.tenplanets.websock.data.StatFacade;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class DummyProcessor implements Processable {

    @Override
    public JSONObject process(JSONObject obj) throws ProcessingException {

        try {

            Msg msg = new Msg(obj);
            if (msg.getAmountBuy() == null || msg.getAmountSell() == null) {
                throw new ProcessingException("Empty amout in currency transaction.");
            }

            if (msg.getCurrencyFrom().equals(msg.getCurrencyTo())) {
                throw new ProcessingException("Wrong currency pair.");
            }

            JSONObject result = new JSONObject();
            JSONObject copy = new JSONObject(obj.toString());
            result.put("trans", copy);

            Stat stat = writeMessage(msg);
            result.put("stat", stat.toJSON());
            return result;

        } catch (ParseException ex) {
            throw new ProcessingException(ex);
        } catch (JSONException ex) {
            throw new ProcessingException(ex);
        }

    }

    /**
     * This method should be wrapped in db transaction. Synchronization is only needed with db emulation.
     * In order to have consistent state of Stat object (Stat shoul reflects the state, such as
     * just after when a new message was processed), transaction shoul block
     * the Stat during this operation. ('select for update', for example)
     * Because of db operations can be finished in different order than the
     * resulting messages will come to the JMS topic, I use "counter" field in the
     * graphical presentation of statistics to the user: I draw the initial
     * statistic graph only once in the presentation view, and then update this
     * view by new transactions(msgs) which have counter greater than in the
     * initial graph.
     *
     * The db transaction can be less strict, providing that increasing of
     * values in the Stats will be atomic - for example if a Stat will be stored
     * in the mongoDb database.    
     */
    public synchronized Stat writeMessage(Msg msg) {

        StatFacade statFacade = new StatFacade();

        Stat stat = statFacade.findOrCreate(msg.getTimePlaced());
        String pair = msg.getCurrencyFrom() + "/" + msg.getCurrencyTo();

        StatEntry entry = stat.getEntries().get(pair);

        if (entry == null) {
            entry = new StatEntry();
            entry.setPair(pair);
            entry.setBuyAmount(msg.getAmountBuy());
            entry.setSellAmount(msg.getAmountSell());
            stat.getEntries().put(pair, entry);

        } else {

            entry.setBuyAmount(entry.getBuyAmount().add(msg.getAmountBuy()));
            entry.setSellAmount(entry.getSellAmount().add(msg.getAmountSell()));

        }

        stat.setCounter(stat.getCounter() + 1);

        MsgFacade mf = new MsgFacade();
        mf.create(msg);

        try {
            return (Stat) stat.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }

    }

}
