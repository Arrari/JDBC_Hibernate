package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.getConnection();


    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("CREATE TABLE IF NOT EXISTS `users` (\n" +
                        "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                        "  `name` VARCHAR(20) NOT NULL,\n" +
                        "  `lastName` VARCHAR(20) NOT NULL,\n" +
                        "  `age` INT NOT NULL,\n" +
                        "  PRIMARY KEY (`id`));")) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("DROP TABLE IF EXISTS `users`;")) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("INSERT INTO users (name, lastname, age) values( ?, ?, ?);")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.execute();
            System.out.println("User c именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM users WHERE id= ? ;")) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge((byte) resultSet.getInt("age"));

                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("TRUNCATE TABLE users;")) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
