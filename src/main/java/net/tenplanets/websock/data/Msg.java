/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class Msg extends AbstractPersistentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    // private static final String DATE_PATTERN = "dd-MMM-yyyy HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    private static final String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    private Long id = 0L;
    private String userId;
    private String currencyFrom;
    private String currencyTo;
    private BigDecimal amountSell;
    private BigDecimal amountBuy;
    private BigDecimal rate;
    private String originatingCountry;
    private Date timePlaced;

    public Msg() {
        super();
    }

    public Msg(JSONObject obj) throws JSONException, ParseException {
        this();

        currencyFrom = obj.getString("currencyFrom");
        currencyTo = obj.getString("currencyTo");
        double amount = obj.optDouble("amountBuy", 0);
        if (amount != 0) {
            amountBuy = new BigDecimal(Double.toString(amount));
        }
        amount = obj.optDouble("amountSell", 0);
        if (amount != 0) {
            amountSell = new BigDecimal(Double.toString(amount));
        }

        rate = new BigDecimal(Double.toString(obj.getDouble("rate")));
        userId = obj.getString("userId");
        originatingCountry = obj.getString("originatingCountry");

        String time = obj.getString("timePlaced");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.US);

        /* DateFormatSymbols dfs = simpleDateFormat.getDateFormatSymbols();
        dfs.setShortMonths(months);
        simpleDateFormat.setDateFormatSymbols(dfs);*/ 
        timePlaced = simpleDateFormat.parse(time);    

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Msg)) {
            return false;
        }
        Msg other = (Msg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public BigDecimal getAmountSell() {
        return amountSell;
    }

    public void setAmountSell(BigDecimal amount) {
        if (amount != null) {
            amount = amount.setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        this.amountSell = amount;
    }

    public BigDecimal getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(BigDecimal amount) {
        if (amount != null) {
            amount = amount.setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        this.amountBuy = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getOriginatingCountry() {
        return originatingCountry;
    }

    public void setOriginatingCountry(String originatingCountry) {
        this.originatingCountry = originatingCountry;
    }

    public Date getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(Date timePlaced) {
        this.timePlaced = timePlaced;
    }

}
