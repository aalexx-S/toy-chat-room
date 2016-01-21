package Server.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class NotifyManager extends DatabaseManager {

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:notify.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Notify " +
                "(Account STRING PRIMARY KEY NOT NULL," +
                " Notify  STRING )";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void add(String account) {
        Connection c = null;
        PreparedStatement stmt = null;

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:notify.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Notify (Account) VALUES (?);");
                stmt.setString(1, account);
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

    public List<String> query(String account) {
        Connection c = null;
        PreparedStatement stmt = null;
        List<String> response = new ArrayList<String>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:notify.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT Notify FROM Notify WHERE Account = ?;");
                stmt.setString(1, account);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String dirty_targets = rs.getString("Notify");
                    String[] targets = dirty_targets.split("\\.");
                    for (String target : targets) {
                        response.add(target);
                    }
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
        return response;
    }

    public void update(String account, String new_notify) {
        Connection c = null;
        PreparedStatement stmt = null;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:notify.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT Notify FROM Notify WHERE Account = ?;");
                stmt.setString(1, account);
                String dirty_targets = new String();
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    dirty_targets = rs.getString("Notify");
                    if (dirty_targets == null)
                        dirty_targets = new_notify;
                    else
                        dirty_targets += "." + new_notify;
                    System.err.println("new: " + dirty_targets);
                }
                rs.close();
                stmt.close();

                stmt = c.prepareStatement("UPDATE Notify SET Notify = ? WHERE Account = ?;");

                stmt.setString(1, dirty_targets);
                stmt.setString(2, account);
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
}
