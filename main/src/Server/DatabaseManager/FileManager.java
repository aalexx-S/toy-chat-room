package Server.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class FileManager extends DatabaseManager {

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:file.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS File " +
                "(Token INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Content  STRING," +
                " Timestamp INT)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int add(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;

        int id = -1;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:file.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO File (Content,Timestamp)" +
                        " VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, entry.get("content"));
                stmt.setString(2, entry.get("time_stamp"));
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                   id = rs.getInt(1);
                }
                else {
                    System.err.println("fail to generate file token");
                }
                rs.close();
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
        return id;
    }

    public String query(int token) {
        Connection c = null;
        Statement stmt = null;
        String response = new String();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:file.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT Content FROM File WHERE Token = " + token + ";");
                if (rs.next()) {
                    response = rs.getString("Content");
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