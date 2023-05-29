package database;

import java.sql.*;

public class DatabaseHandler {
    private static final String DB_url = "jdbc:derby:database/forum;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    public static DatabaseHandler handler;
    public static String tableName;

    public DatabaseHandler(){
        createConnection();
        createTable();
    }

    public static DatabaseHandler getHandler(){
        if(handler == null){
            handler = new DatabaseHandler();
            return handler;
        }else{
            return handler;
        }
    }

    private void createTable() {
        // Name of the folder stored from user input
        String TABLE_NAME = tableName;
        try{
            stmt = conn.createStatement();
            DatabaseMetaData dmn = conn.getMetaData();
            ResultSet tables = dmn.getTables(null,null,TABLE_NAME,null);
            if(tables.next()){
                System.out.println("Table " + TABLE_NAME+ " exists");
            }
            else{
                String statement = "CREATE TABLE " + TABLE_NAME + " ("
                        + "name varchar(200), \n"
                        + "path varchar(200), \n"
                        + "extension varchar(200), \n"
                        + "size varchar(200))";
                //System.out.println(statement);
                stmt.execute(statement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createConnection() {
        try{
            conn = DriverManager.getConnection(DB_url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean execAction(String qu) {
        try{
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Did not enter data");

        }
        return false;
    }

    public ResultSet execQuery(String query) {
        ResultSet resultSet;
        try{
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("There is no such table.");
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public void showTable() {
        try {
            DatabaseMetaData dmn = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dmn.getTables(null, null, "%", types);
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

