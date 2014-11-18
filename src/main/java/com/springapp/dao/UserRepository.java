package com.springapp.dao;

import com.springapp.entity.Transaction;
import com.springapp.entity.User;
import com.springapp.entity.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<User> getAllUsers() {
        String sql = "select * from user";
        List<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setPwd(resultSet.getString("pwd"));

                return user;
            }
        });

        return users;
    }

    public User getUser(final String username, final String pwd) {
        String sql = "select * from user where email=? and pwd=?";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<User>() {
            @Override
            public User doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, username);
                ps.setString(2, pwd);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setEmail(rs.getString("email"));
                    u.setPwd(rs.getString("pwd"));
                    u.setFirstName(rs.getString("firstname"));
                    u.setLastName(rs.getString("lastname"));
                    u.setBalance(rs.getDouble("balance"));
                    return u;
                }
                return null;
            }
        });

    }

    public User getUserByEmail(final String email) {
        String sql = "select * from user where email = ?";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<User>() {
            @Override
            public User doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setEmail(rs.getString("email"));
                    u.setFirstName(rs.getString("firstname"));
                    u.setLastName(rs.getString("lastname"));
                    return u;
                }
                return null;
            }
        });
    }

    public double getBalanceForUser(final int userId) {
        String sql = "select balance from user where id = ?";

        return jdbcTemplate.query(sql, new Object[]{Integer.valueOf(userId)}, new ResultSetExtractor<Double>() {
            @Override
            public Double extractData(ResultSet rs) throws SQLException {
                if(rs.next()) {
                    return rs.getDouble("balance");
                }
                return Double.MIN_VALUE;
            }
        });
    }

    public List<Transaction> getLedgerForUser(final int userId) {
        String sql = "select * from user_tx ut inner join tx on ut.tx_id = tx.id where ut.user_id = ? order by tx_time desc";

        final List<Transaction> transactions = jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, userId);
            }
        }, new ResultSetExtractor<List<Transaction>>() {
            @Override
            public List<Transaction> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Transaction> userTransactions = new ArrayList<Transaction>();
                while(rs.next()) {
                    Transaction t = new Transaction();
                    UserTransaction ut = new UserTransaction();
                    t.setAmount(rs.getDouble("tx.amount"));
                    t.setTxTime(rs.getTimestamp("tx.tx_time"));
                    t.setId(rs.getInt("tx.id"));

                    ut.setTxType(rs.getInt("ut.tx_type"));
                    ut.setUserId(userId);

                    t.setUserTransactions(new ArrayList<UserTransaction>());
                    t.getUserTransactions().add(ut);

                    userTransactions.add(t);
                }

                return userTransactions;
            }
        });

        return transactions;
    }
}
