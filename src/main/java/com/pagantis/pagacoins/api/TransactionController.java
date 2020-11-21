package com.pagantis.pagacoins.api;

import com.pagantis.pagacoins.ErrorMessages;
import com.pagantis.pagacoins.model.Transaction;
import com.pagantis.pagacoins.model.Wallet;
import com.pagantis.pagacoins.repository.TransactionRepository;
import com.pagantis.pagacoins.repository.WalletRepository;
import org.apache.catalina.valves.ErrorReportValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    WalletRepository walletRepository;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid Transaction transaction){
            System.out.println("Llamada a POST");
            Wallet walletOrigin, walletDestiny;

            Optional<Wallet> walletOriginOpt = walletRepository.findById(transaction.getWalletOrigin());
            if(walletOriginOpt.isPresent()){
                walletOrigin = walletOriginOpt.get();
                if(walletOrigin.getBalance() < transaction.getAmount())
                    return new ResponseEntity(ErrorMessages.transaction_no_money,
                                                    HttpStatus.PRECONDITION_FAILED);
            }else{
                // Hay que probar esto
                return new ResponseEntity(ErrorMessages.transaction_originWallet404, HttpStatus.PRECONDITION_REQUIRED);
            }

        Optional<Wallet> walletDestinyOpt = walletRepository.findById(transaction.getWalletDestiny());
        if(!walletDestinyOpt.isPresent())
            return new ResponseEntity(ErrorMessages.transaction_destinyWallet404,
                                            HttpStatus.PRECONDITION_REQUIRED);

        try{
            Transaction newTransaction =
                    transactionRepository.save(new Transaction( transaction.getWalletOrigin(),
                                                                        transaction.getWalletDestiny(),
                                                                        transaction.getAmount()));

            walletDestiny = walletDestinyOpt.get();

            walletOrigin.restBalance(transaction.getAmount());
            walletDestiny.addBalance(transaction.getAmount());

            walletRepository.save(walletOrigin);
            walletRepository.save(walletDestiny);

            return new ResponseEntity<>(newTransaction, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(ErrorMessages.internal_error , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "{id}")
    public ResponseEntity<Transaction> geTransactionById(@PathVariable("id") String id){
        Optional<Transaction> wallet = transactionRepository.findById(id);
        if(wallet.isPresent())
            return new ResponseEntity<>(wallet.get(), HttpStatus.OK);
        return new ResponseEntity(ErrorMessages.transaction_notFound ,HttpStatus.NOT_FOUND);

    }

    @GetMapping(path= "/history/{id}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable("id") String id){
       try {
           List<Transaction> transactions = new ArrayList<>(transactionRepository.findTransactionByWalletDestiny(id));
           transactions.addAll(new ArrayList<>(transactionRepository.findTransactionByWalletOrigin(id)));

           if (transactions.isEmpty())
               return new ResponseEntity(ErrorMessages.transaction_noHistory, HttpStatus.NO_CONTENT);

           return new ResponseEntity<>(transactions, HttpStatus.OK);
       }catch(Exception e){
           return new ResponseEntity(ErrorMessages.internal_error, HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }

}
