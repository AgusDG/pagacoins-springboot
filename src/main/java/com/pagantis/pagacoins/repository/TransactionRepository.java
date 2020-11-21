package com.pagantis.pagacoins.repository;

import com.pagantis.pagacoins.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findTransactionByWalletOrigin(String walletOrigin);
    List<Transaction> findTransactionByWalletDestiny(String walletDestiny);
}
