package com.pagantis.pagacoins.repository;

import com.pagantis.pagacoins.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WalletRepository extends MongoRepository<Wallet, String> {
    List<Wallet> findWalletByOwner(String owner);
}
