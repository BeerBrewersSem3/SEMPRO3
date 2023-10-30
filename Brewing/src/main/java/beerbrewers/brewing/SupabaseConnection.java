package beerbrewers.brewing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupabaseConnection {

    private static SupabaseConnection instance;
    private final String databaseURL = "db.hbdjtgvmsnqkxcufwgwh.supabase.co";
    private final String databaseName = "postgres";
    private final String databasePort = "5432";
    private final String databaseUser = "postgres";
    private final String databasePW = "stivpikoghaardeslag";
    private Connection connection = null;

    private SupabaseConnection() {
        connectToSupabaseDatabase();
    }

    //Using Singleton pattern:
    public static SupabaseConnection getInstance() {
        if (instance == null) {
            instance = new SupabaseConnection();
        }
        return instance;
    }

    private void connectToSupabaseDatabase() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + databaseURL
                            +":" + databasePort
                            +"/" + databaseName
                            + "?user=" + databaseUser
                            +"&password=" + databasePW);
            System.out.println("Successfully connected to the database.");
        } catch (SQLException | IllegalArgumentException exception) {
            exception.printStackTrace(System.err);
        }
        finally {
            if (connection == null) System.exit(-1);
        }
    }

    public void testQueryForDatabase() {
        try {
            PreparedStatement queryStatement = connection.prepareStatement("SELECT * FROM test_table WHERE id = ?");
            queryStatement.setInt(1, 1);
            ResultSet sqlReturnValues = queryStatement.executeQuery();

            while (sqlReturnValues.next()) {
                System.out.println("Database query status: " + sqlReturnValues.getString(2));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
