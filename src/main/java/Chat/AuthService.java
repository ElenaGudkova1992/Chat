package Chat;


import java.sql.SQLException;
import java.util.Optional;

/**
 * Сервис авторизации
 */
public interface AuthService {

    /**
     * запустить сервис
     */
    void start() throws SQLException;

    /**
     * Остановить сервис
     */

    void stop();

    /**
     * Получить никнейм
     * @return
     */
    // Optional<String> getNickByLoginAndPass(String login, String pass);

    String getNickByLoginPass(String login, String pass);

    /**
     * Проверить что ник есть в базе - false
     * @param nick
     * @return
     */
    default boolean isNickFree(String nick) {
        return true;
    }


    /**
     * Проверить что ник есть в базе - false
     * @param newNick
     * @param nick
     */
    default void updateNick(String newNick, String nick) {

    }
}