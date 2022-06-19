package ctf.utils;

import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    public static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
