package com.pagantis.pagacoins.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "wallets")
public class Wallet {
    @Id
    private String id;
    private String alias;
    private double balance;
    @NotNull
    private String owner;

    public Wallet(){

    }

    public Wallet(String owner) {
        this.alias = "Default Wallet";
        this.balance = 0.0;
        this.owner = owner;
    }

    public Wallet(String owner, String alias, double balance) {
        this.alias = (alias != null && alias.length() > 0) ? alias : "Default wallet";
        this.balance = balance;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getOwner() {
        return owner;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void addBalance(double amount){
        this.balance =  this.balance + amount;
    }

    public void restBalance(double amount){
        this.balance = this.balance - amount;
    }


}
