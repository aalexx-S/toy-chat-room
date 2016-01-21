package Server.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class AccountManager extends DatabaseManager {
    
    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:account.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Account " +
                "(Account STRING PRIMARY KEY    NOT NULL," +
                " Password  STRING  NOT NULL," +
                " LastLogoutTime INT    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void add(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:account.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Account (Account, Password, LastLogoutTime) VALUES(?, ?, -1);");
                stmt.setString(1, entry.get("account"));
                stmt.setString(2, entry.get("password"));
                stmt.executeUpdate();
                stmt.close();
                c.commit();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }

    public boolean query(String account) {
        Connection c = null;
        PreparedStatement stmt = null;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:account.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT * FROM Account WHERE Account = ?;");
                stmt.setString(1, account);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return true;
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return false;
    }

    public boolean authenticate(String account, String password) {
        Connection c = null;
        PreparedStatement stmt = null;
        boolean response = false;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:account.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT * FROM Account WHERE Account = ? AND Password = ?;");
                stmt.setString(1, account);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    response = true;
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            } catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }
}
