package database;

import java.sql.*;

public class DatabaseHandler {
    private static final String DB_url = "jdbc:derby:database/forum;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    public static DatabaseHandler handler;
    public static String tableName;

    //Constructor
    public DatabaseHandler(){
        createConnection();
        createTable();
    }

    /**
     * Get the single instance of the database handler
     * @return database handler
     */
    public static DatabaseHandler getHandler(){
        if(handler == null){
            handler = new DatabaseHandler();
            return handler;
        }else{
            return handler;
        }
    }

    /**
     * Create Table with different columns in the database
     */
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

    /**
     * Create Connection with the database
     */
    private void createConnection() {
        try{
            conn = DriverManager.getConnection(DB_url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute the action included in the string
     * @param qu action as a string
     * @return True if the action is executed or False if it is not executed
     */
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

    /**
     * Create an ResultSet object from the query
     * @param query String, ex. "SELECT * FROM " + database table name
     * @return The resultSet or null if the database does not exist
     */
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

    /**
     * Obtain the table names in the database
     */
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

