package ctf.dao;

public interface UserDao {

    boolean checkUser(String username);

    boolean tryLogin(String username, String password);
}
