/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class Stat extends AbstractPersistentEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private Date id;

    public Stat() {
        super();
    }

    public Stat(Date date) {
        this();
        id = removeTime(date);
    }

    public static Date removeTime(Date dt) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        return cal.getTime();
    }

    private Map<String, StatEntry> entries = new HashMap<String, StatEntry>();

    public Date getId() {
        return id;
    }

    public void setId(Date id) {
        this.id = removeTime(id);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Stat)) {
            return false;
        }
        Stat other = (Stat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    private long counter;

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public Map<String, StatEntry> getEntries() {
        return entries;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public JSONObject toJSON() {
        JSONObject jsonStat = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();
            for (StatEntry entry : getEntries().values()) {
                JSONObject jsonEntry = new JSONObject();

                jsonEntry.put("pair", entry.getPair());
                jsonEntry.put("buyAmount", entry.getBuyAmount().doubleValue());
                jsonEntry.put("sellAmount", entry.getSellAmount().doubleValue());

                jArray.put(jsonEntry);
            }

            jsonStat.put("entries", jArray);
            jsonStat.put("counter", getCounter());

        } catch (JSONException ex) {//Never thrown
        }
        return jsonStat;
    }

}
