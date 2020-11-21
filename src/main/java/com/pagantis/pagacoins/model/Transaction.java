package com.pagantis.pagacoins.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Document(collection="transactions")
public class Transaction {

    @Id
    private String id;
    @NotNull @NotBlank
    private String walletOrigin;
    @NotNull @NotBlank
    private String walletDestiny;
    @NotNull @PositiveOrZero
    private double amount;
    private LocalDateTime date;

    public Transaction(){

    }

    public Transaction(String walletOrigin, String walletDestiny, double amount) {
        this.walletOrigin = walletOrigin;
        this.walletDestiny = walletDestiny;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getWalletOrigin() {
        return walletOrigin;
    }

    public String getWalletDestiny() {
        return walletDestiny;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
