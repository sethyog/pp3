package com.springapp.biz;

import com.springapp.Constants;
import com.springapp.dao.TxRepository;
import com.springapp.entity.Transaction;
import com.springapp.entity.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TxService {

    @Autowired
    private TxRepository txRepository;

    @Autowired
    private UserService userService;

    public List<Transaction> getTransactionHistory(int userId) {
        return null;
    }

    public Transaction getTxById(int txId) {
        return null;
    }

    public int initTransfer(int fromUserId, int toUserId, double amount) {
        UserTransaction giverTx = new UserTransaction();
        giverTx.setUserId(fromUserId);
        giverTx.setTxType(Constants.TxType.DEBIT);

        UserTransaction receiverTx = new UserTransaction();
        receiverTx.setUserId(toUserId);
        receiverTx.setTxType(Constants.TxType.CREDIT);

        List<UserTransaction> userTransactions = new ArrayList<UserTransaction>();
        userTransactions.add(giverTx);
        userTransactions.add(receiverTx);

        Transaction t = new Transaction();
        t.setUserTransactions(userTransactions);
        t.setAmount(amount);

        int txId = txRepository.insertNewTx(t);

        txRepository.addBalance(fromUserId, 0-amount);
        txRepository.addBalance(toUserId, amount);

        return txId;

    }

}
