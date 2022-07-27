package org.example;
import java.sql.*;
import java.util.Collection;

/*
* This class is to establish JDBC connections
* - save data to the database
* - update the database data
* - retrieve data from the database
* */
public class AccountRepo implements Repository<User, Integer> {
    private ConnectionManager connectionManager;

    //construstor allowing database connection from other classes
    public AccountRepo(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    //method to get accounts from the databse using their account_id
    @Override
    public User getById(Integer accountId) {
        Connection connection = null;
        User item = null;
        try {
            //query to select the specified account
            String sql = "select account_id , username, userpassword, balance from customer_accounts where account_id =?";

            connection = connectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId); //pass in the id of the account to select

            ResultSet rs = ps.executeQuery(); //execute the query and save the account to rs

            //save each retrieved data to item
            while(rs.next()) {
                item = new User(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("userpassword"),
                        rs.getInt("balance")
                );
            }
            //return the data retrieved
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return item;
    }

    @Override
    public Collection<User> getAll(Integer accountId) {
        return null;
    }

    /*method to update the balance of the accounts
    * - after a transfer or
    * - after a deposit
    * - after a withdrawal
     */
    @Override
    public Integer updateBalance(User balance, int ID){
        Connection connection = null;

        try{
            String sql = "UPDATE customer_accounts SET balance =? WHERE account_id = ?";
            connection = connectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, balance.getBalance());
            ps.setInt(2,ID);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /*
    * method to save new data to the database
    * */
    @Override
    public Integer save(User item) {
        Connection connection = null;

        try {

            String sql = "insert into customer_accounts (account_id, userName, userpassword, balance) values (?,?,?,?)";
            connection = connectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, item.getAccountId());
            ps.setString(2, item.getUserName());
            ps.setString(3, item.getPassword());
            ps.setInt(4, item.getBalance());
            ps.executeUpdate();

            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}