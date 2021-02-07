package com.cryptocurrency.cryptoAPI.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "user")
public class User implements UserDetails {

    public User(String username, String password, Double money, String authority) {
        this.username = username;
        this.password = password;
        this.money = money;
        this.authority = authority;
    }

    public User(){
        this.money = 1000.0; //lil starter-pack for newcomers
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String username;
    String password;
    Double money;
    private String authority;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    List<Currency> currencies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double value) {
        this.money = value;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
                .stream(this.authority.split(","))
                .map(String::trim)
                .filter(authority -> !authority.equals(""))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void addAuthority(GrantedAuthority authority) {
        String newAuthority = authority.getAuthority().trim();
        String currentAuthorities = this.authority == null ? "" : (this.authority + ",");
        this.authority = currentAuthorities + newAuthority;
    }

    public void removeAuthority(GrantedAuthority authority) {
        String deletedAuthority = authority.getAuthority().trim();
        String remainingAuthorities = this.authority.replace(deletedAuthority, "")
                .replace(",,", "")
                .trim();
        this.authority = remainingAuthorities;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
