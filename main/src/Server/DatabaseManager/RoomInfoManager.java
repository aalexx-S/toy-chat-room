package Server.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class RoomInfoManager extends DatabaseManager {
    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:roominfo.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS RoomInfo " +
                "(RoomID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Users   STRING  NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int add(String type, List<String> users) {
        Connection c = null;
        PreparedStatement stmt = null;

        int id = -1;

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:roominfo.db");
                c.setAutoCommit(false);

                String dirty_users = users.get(0);
                for (int i = 1; i < users.size(); i++) {
                    dirty_users += "." + users.get(i);
                }

                stmt = c.prepareStatement("INSERT INTO RoomInfo " +
                                "(Users)" +
                                "VALUES (?);", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, dirty_users);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                else {
                    System.err.println("Fail to generate ID");
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

    public void update(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:roominfo.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT Users FROM RoomInfo WHERE RoomID = ?;");
                stmt.setString(1, entry.get("room_id"));
                String dirty_users = new String();
                boolean add = false;
                if (entry.get("type").equals("add")) {
                    add = true;
                }

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    dirty_users = rs.getString("Users");
                    String target = entry.get("name");
                    if (add)
                        dirty_users += "." + target;
                    else {
                        List<String> user_list = new ArrayList<String>();
                        String[] users = dirty_users.split(".");
                        for (String user : users) {
                            if (!user.equals(target))
                                user_list.add(user);
                        }
                        dirty_users = user_list.get(0);
                        for (int i = 1; i < user_list.size(); i++) {
                            dirty_users += "." + user_list.get(i);
                        }
                    }
                }
                rs.close();
                stmt.close();

                stmt = c.prepareStatement("UPDATE RoomInfo SET Users = ? WHERE RoomID = ?;");

                stmt.setString(1, dirty_users);
                stmt.setString(2, entry.get("room_id"));
                stmt.executeUpdate();
                stmt.close();
                c.commit();
                c.close();
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }

    public List<String> query(int room_id) {
        Connection c = null;
        Statement stmt = null;
        List<String> response = new ArrayList<String>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:roominfo.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT Users FROM RoomInfo WHERE RoomID = " + Integer.toString(room_id) + ";" );
                while (rs.next()) {
                    String dirty_users = rs.getString("Users");
                    String[] users = dirty_users.split(".");
                    for (String user : users) {
                        response.add(user);
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
}
