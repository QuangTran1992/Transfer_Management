package com.quang.DAO;

import com.quang.model.User;
import com.quang.utils.ConfigConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO{


    private static final String SELECT_USER_BY_ID = "select id,name,email,country,salary from users where id =?";
    private static final String QUERY = "select * from users";
    private static final String UPDATE_SALARY = "UPDATE users SET salary = ? WHERE id = ?;";

    public UserDAO() {
    }

    Connection connection = ConfigConnection.getConnection();


    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }


    @Override
    public void insertUser(User user) throws SQLException {
        String query = "INSERT INTO users" + "  (name, email, country,salary) VALUES " +
                " (?, ?, ? , ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setFloat(4,user.getSalary());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public User selectUser(int id) {
        String query = "select * from users where id = ?;";
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                Float salary = rs.getFloat("salary");
                user = new User(id, name, email, country,salary);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    @Override
    public List<User> selectAllUsersNonExistCurrentUser(int currentUserId) {
        String query = "select * from users where id != ?;";
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, currentUserId);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                Float salary = rs.getFloat("salary");
                users.add(new User(id, name, email, country,salary));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        return users;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                Float salary = rs.getFloat("salary");
                users.add(new User(id, name, email, country,salary));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        String query = "delete  from users where id = ?";
        boolean rowDeleted;
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        String query = "update users set name = ?,email= ?, country =?, salary = ? where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setFloat(4, user.getSalary());
            statement.setInt(5, user.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public void transferBalance(User user) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SALARY)){
            preparedStatement.setFloat(1, user.getSalary());
            preparedStatement.setInt(2,user.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            printSQLException(e);
        }
    }
}
