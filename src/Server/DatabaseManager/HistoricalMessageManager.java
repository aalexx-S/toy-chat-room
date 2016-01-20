package Server.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class HistoricalMessageManager extends DatabaseManager {

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:message.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Message " +
                "(MessageID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " RoomID    INT NOT NULL," +
                " Sender  STRING  NOT NULL," +
                " Content  STRING," +
                " Timestamp INT NOT NULL)";
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
                c = DriverManager.getConnection("jdbc:sqlite:message.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Message (RoomID,Sender,Content,Timestamp)" +
                        "VALUES (?, ?, ?, ?);");
                stmt.setString(1, entry.get("room_id"));
                stmt.setString(2, entry.get("sender_name"));
                stmt.setString(3, entry.get("content"));
                stmt.setString(4, entry.get("time_stamp"));

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

    public List<Map<String, String>> query(String room_id) {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:message.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Message WHERE RoomID = " + room_id + ";");
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String sender = rs.getString("Sender");
                    String content = rs.getString("Content");
                    entry.put("room_id", room_id);
                    entry.put("sender_name", sender);
                    entry.put("content", content);
                    response.add(entry);
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
}
