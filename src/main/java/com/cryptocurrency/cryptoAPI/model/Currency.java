package com.cryptocurrency.cryptoAPI.model;

import javax.persistence.*;

@Entity(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String symbol;
    Double value;

    @ManyToOne
    User user;

    public Currency(String symbol, Double value) {
        this.symbol = symbol;
        this.value = value;
    }

    public Currency() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
