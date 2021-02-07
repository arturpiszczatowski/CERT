package com.cryptocurrency.cryptoAPI.model;

import javassist.compiler.ast.Symbol;

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

    public Currency(String symbol, Double value, User user) {
        this.symbol = symbol;
        this.value = value;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
