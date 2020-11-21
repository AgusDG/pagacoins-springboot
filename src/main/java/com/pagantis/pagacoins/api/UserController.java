package com.pagantis.pagacoins.api;

import com.pagantis.pagacoins.ErrorMessages;
import com.pagantis.pagacoins.model.User;
import com.pagantis.pagacoins.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @NotNull @Valid User user){
        Optional<User> dbUser = userRepository.findByEmail(user.getEmail());
        if(dbUser.isPresent()){
            return new ResponseEntity(ErrorMessages.user_mustbe_unique ,HttpStatus.CONFLICT);
        }
        try {
            User newUser = userRepository.save(new User(user.getName(), user.getEmail()));
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity(ErrorMessages.internal_error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        System.out.println("Entro en get Users");
        try{
            List<User> users = new ArrayList<>(userRepository.findAll());

            if(users.isEmpty())
                return new ResponseEntity( ErrorMessages.user_notfound ,HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(users, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity(ErrorMessages.internal_error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id){

        Optional<User> user  = userRepository.findById(id);

       if(user.isPresent())
           return new ResponseEntity<>(user.get(), HttpStatus.OK);

       return new ResponseEntity(ErrorMessages.user_notfound, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") String id){
        try{
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(ErrorMessages.internal_error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @Valid @NonNull @RequestBody User userToUpdate){
        Optional<User> userData = userRepository.findById(id);

        if(userData.isPresent()){
            User user = userData.get();
            user.setName(userToUpdate.getName());
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        }else{
            return new ResponseEntity(ErrorMessages.user_notfound, HttpStatus.NOT_FOUND);
        }

    }

}
