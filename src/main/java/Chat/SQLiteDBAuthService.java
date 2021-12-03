package Chat;

/**
 * 1. Добавить в сетевой чат запись локальной истории в текстовый файл на клиенте.
 * 2. После загрузки клиента показывать ему последние 100 строк чата.
 */

import java.sql.*;
import java.util.Optional;


public class SQLiteDBAuthService implements AuthService {

    private static Connection connection; // интерфейс для соединения с базой данных
    private static Statement statement; // интерфейс для отправки запросов в БД

    @Override
    public void start() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        statement = connection.createStatement();
        createTable();
        insertTable();
    }

    // создать таблицу, если ее нет
    public void createTable() {
        String sqlCommand = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "nick VARCHAR UNIQUE NOT NULL, " +
                "login TEXT UNIQUE NOT NULL" +
                "pass TEXT NOT NULL" +
                ")";
        try {
            statement.executeUpdate(sqlCommand);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // проверить, что таблица пустая
    public boolean tableIsEmpty() {
        boolean result = false;
        String sqlCommand = "SELECT * FROM chat.users;";
        try (ResultSet resultSet = statement.executeQuery(sqlCommand)) {
            result = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !result;
    }


    // заполнить таблицу
    public void insertTable() {
        if (tableIsEmpty()) {
            String sqlCommand =
                    "INSERT INTO users (nick, login, pass) VALUES ('nick1', 'login1', 'pass1'), " +
                            "('nick2', 'login2', 'pass2'), " +
                            "('nick3', 'login3', 'pass3');";
            try {
                statement.executeUpdate(sqlCommand);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // получить ник пользователя
    @Override
    public String getNickByLoginPass(String login, String pass) {
        String nick = null;
        String sqlCommand = "SELECT nick FROM users WHERE login = '" + login + "' AND pass ='" + pass + "';";
        try {
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            while(resultSet.next()){
                nick = resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return nick;
    }

    // проверить, что ник свободен
    @Override
    public boolean isNickFree(String nick) {
        boolean result = false;
        String sqlCommand = "SELECT nick FROM users WHERE nick = '" + nick + "';";
        try (ResultSet resultSet = statement.executeQuery(sqlCommand)) {
            result = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !result;
    }

    // получить id пользователя
    public int getIdUsers(String nick) {
        int id = 0;
        String sqlCommand = "SELECT id FROM users where nick = '" + nick + "';";
        try {
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            while(resultSet.next()){
                id = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    // обновить ник в таблице
    @Override
    public void updateNick(String newNick, String nick) {
        int id = getIdUsers(nick);
        String sqlCommand = "UPDATE users SET nick = '" + newNick + "' WHERE (id = '" + id + "')";
        try {
            statement.executeUpdate(sqlCommand);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
