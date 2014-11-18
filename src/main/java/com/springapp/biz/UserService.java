package com.springapp.biz;

import com.springapp.Constants;
import com.springapp.dao.UserRepository;
import com.springapp.entity.Transaction;
import com.springapp.entity.User;
import com.springapp.entity.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return this.userRepository.getAllUsers();
    }

    public User getUser(String username, String pwd){
        return this.userRepository.getUser(username, pwd);
    }

    public boolean hasSufficientBalance(int userId, double amount) {
        if(userRepository.getBalanceForUser(userId) < amount)
            return false;
        return true;

    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public List<Transaction> getLedgerForUser(int userId) {
        return userRepository.getLedgerForUser(userId);
    }
}
