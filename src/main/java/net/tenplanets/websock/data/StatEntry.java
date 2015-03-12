/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class StatEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pair;
    private BigDecimal sellAmount = BigDecimal.ZERO;
    private BigDecimal buyAmount = BigDecimal.ZERO;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.pair);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatEntry other = (StatEntry) obj;
        if (!Objects.equals(this.pair, other.pair)) {
            return false;
        }
        return true;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public BigDecimal getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(BigDecimal amount) {
        if (amount != null) {
            amount=amount.setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        this.sellAmount = amount;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal amount) {
        if (amount != null) {
            amount=amount.setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        this.buyAmount = amount;
    }

}
