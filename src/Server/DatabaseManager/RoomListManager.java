package Server.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class RoomListManager extends DatabaseManager {

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:roomlist.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS RoomList " +
                "(Account STRING PRIMARY KEY NOT NULL," +
                " RoomIDs STRING," +
                " RoomTypes    STRING," +
                " RoomNames   STRING)";
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
                c = DriverManager.getConnection("jdbc:sqlite:roomlist.db");
                c.setAutoCommit(false);
                stmt = c.prepareStatement("INSERT INTO RoomList (Account) VALUES (?);");
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

    public List<Map<String, String>> query(String account) {
        Connection c = null;
        PreparedStatement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:roomlist.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT * FROM RoomList WHERE Account = ?;");
                stmt.setString(1, account);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String dirty_ids = rs.getString("RoomIDs");
                    String dirty_types = rs.getString("RoomTypes");
                    String dirty_names = rs.getString("RoomNames");
                    String[] ids = dirty_ids.split("\\.");
                    String[] types = dirty_types.split("\\.");
                    String[] names = dirty_names.split("\\.");

                    for (int i = 0; i < ids.length; i++) {
                        Map<String, String> entry = new HashMap<String, String>();

                        entry.put("room_type", types[i]);
                        entry.put("room_id", ids[i]);
                        entry.put("room_name", names[i]);
                        response.add(entry);
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

    public String getMutualRoomID(String first, String second) {
        Connection c = null;
        PreparedStatement stmt = null;
        String empty = "";
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:roomlist.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT * FROM RoomList WHERE Account = ?;");
                stmt.setString(1, first);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String dirty_ids = rs.getString("RoomIDs");
                    String dirty_types = rs.getString("RoomTypes");
                    String dirty_names = rs.getString("RoomNames");
                    String[] ids = dirty_ids.split("\\.");
                    String[] types = dirty_types.split("\\.");
                    String[] names = dirty_names.split("\\.");

                    for (int i = 0; i < ids.length; i++) {
                        if (names[i].equals(second) && types[i].equals("single")) {
                            rs.close();
                            stmt.close();
                            c.close();
                            return ids[i];
                        }
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
        return empty;
    }

    public void update(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:roomlist.db");
                c.setAutoCommit(false);
                stmt = c.prepareStatement("SELECT * FROM RoomList WHERE Account = ?;");
                stmt.setString(1, entry.get("account"));
                String dirty_ids = new String();
                String dirty_types = new String();
                String dirty_names = new String();
                boolean add = false;
                if (entry.get("type").equals("add")) {
                    add = true;
                }

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    dirty_ids = rs.getString("RoomIDs");
                    dirty_types = rs.getString("RoomTypes");
                    dirty_names = rs.getString("RoomNames");
                    if (add) {
                        if (dirty_ids != null) {
                            dirty_ids += "." + entry.get("room_id");
                            dirty_types += "." + entry.get("room_type");
                            dirty_names += "." + entry.get("room_name");
                        }
                        else {
                            dirty_ids = entry.get("room_id");
                            dirty_types = entry.get("room_type");
                            dirty_names = entry.get("room_name");
                        }
                    }
                    else {
                        String target = entry.get("room_id");
                        List<String> id_list = new ArrayList<String>();
                        List<String> type_list = new ArrayList<String>();
                        List<String> name_list = new ArrayList<String>();
                        String[] ids = dirty_ids.split("\\.");
                        String[] types = dirty_types.split("\\.");
                        String[] names = dirty_names.split("\\.");
                        for (int i = 0; i < ids.length; i++) {
                            if (!ids[i].equals(target)) {
                                id_list.add(ids[i]);
                                type_list.add(types[i]);
                                name_list.add(names[i]);
                            }
                        }
                        if (id_list.size() > 1) {
                            dirty_ids = id_list.get(0);
                            dirty_types = type_list.get(0);
                            dirty_names = name_list.get(0);
                        }
                        for (int i = 1; i < id_list.size(); i++) {
                            dirty_ids += "." + id_list.get(i);
                            dirty_types += "." + type_list.get(i);
                            dirty_names += "." + name_list.get(i);
                        }
                    }
                }
                rs.close();
                stmt.close();

                stmt = c.prepareStatement("UPDATE RoomList SET RoomIDs = ?, RoomTypes = ?, RoomNames = ? WHERE Account = ?;");

                stmt.setString(1, dirty_ids);
                stmt.setString(2, dirty_types);
                stmt.setString(3, dirty_names);
                stmt.setString(4, entry.get("account"));
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
