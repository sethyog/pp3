package com.springapp.dao;

import com.springapp.entity.Transaction;
import com.springapp.entity.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class TxRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Transaction getTxById(final int txId) {
        String sql = "select * from tx inner join user_tx on tx.id=user_tx.tx_id where tx.id=?";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Transaction>() {
            @Override
            public Transaction doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, txId);
                ResultSet rs = ps.executeQuery();
                Transaction t = null;
                boolean isInitiated = false;
                while(rs.next()) {
                    if(!isInitiated) {
                        t = new Transaction();
                        t.setId(txId);
                        t.setAmount(rs.getDouble("amount"));
                        t.setTxTime(rs.getTimestamp("tx_time"));
                        t.setUserTransactions(new ArrayList<UserTransaction>());
                    }

                    UserTransaction ut = new UserTransaction();
                    ut.setTxType(rs.getInt("tx_type"));
                    ut.setUserId(rs.getInt("user_id"));
                    t.getUserTransactions().add(ut);
                }
                return t;
            }
        });
    }

    public int insertNewTx(final Transaction t) {
        KeyHolder txIdHolder = new GeneratedKeyHolder();
        final String sql = "insert into tx (amount, tx_time) values (?, ?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setDouble(1, t.getAmount());
                ps.setTimestamp(2, t.getTxTime());
                return ps;
            }
        }, txIdHolder);

        final int txId = txIdHolder.getKey().intValue();

        List<UserTransaction> userTransactions = t.getUserTransactions();
        final String utSql = "insert into user_tx (tx_id, user_id, tx_type) values (?,?,?)";
        for(final UserTransaction ut: userTransactions) {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(utSql);
                    ps.setInt(1, txId);
                    ps.setInt(2, ut.getUserId());
                    ps.setInt(3, ut.getTxType());
                    return ps;
                }
            });
        }

        return txIdHolder.getKey().intValue();
    }

    public void addBalance(final int userId, final double amount) {
        final String sql = "update user set balance=balance + ? where id = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setDouble(1, amount);
                ps.setInt(2, userId);
                return ps;
            }
        });
    }
}
