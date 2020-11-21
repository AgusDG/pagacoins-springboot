package com.pagantis.pagacoins.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.pagantis.pagacoins.model.Wallet;
import com.pagantis.pagacoins.repository.WalletRepository;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pagantis.pagacoins.ErrorMessages;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    WalletRepository walletRepository;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet){
        try{

            Wallet newWallet = walletRepository.save(new Wallet(
                                        wallet.getOwner(),
                                        wallet.getAlias(),
                                        wallet.getBalance()));

            return new ResponseEntity<>(newWallet, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity(ErrorMessages.internal_error,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping
    public ResponseEntity<List<Wallet>> getWallets(){

        try{
            List<Wallet> wallets = new ArrayList<>(walletRepository.findAll());

            if(wallets.isEmpty())
                return new ResponseEntity(ErrorMessages.wallets_notfound, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(wallets, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity(ErrorMessages.internal_error , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping(path = "{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable("id") String id){

        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent())
            return new ResponseEntity<>(wallet.get(), HttpStatus.OK);

        return new ResponseEntity(ErrorMessages.wallets_notfound ,HttpStatus.NOT_FOUND);

    }

    @GetMapping(path = "user/{userId}")
    public ResponseEntity<List<Wallet>> getWalletsByUserId(@PathVariable("userId") String userId){

        try{
            List<Wallet> wallets = new ArrayList<>(walletRepository.findWalletByOwner(userId));

            if(wallets.isEmpty())
                return new ResponseEntity(ErrorMessages.user_has_no_wallets ,HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(wallets, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(ErrorMessages.internal_error ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Wallet> deleteById(@PathVariable("id") String id){
        try{
            walletRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(ErrorMessages.internal_error ,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
