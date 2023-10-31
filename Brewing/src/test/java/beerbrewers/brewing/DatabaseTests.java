package beerbrewers.brewing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseTests {

    @Test
    void DatabaseIsConnected() {
        SupabaseConnection supabaseConnection = SupabaseConnection.getInstance();
        try {
            assertTrue(supabaseConnection.getConnection().isValid(5));
        } catch (SQLException e) {
            fail("Database connection could not yet be established");
        }
    }

    @Test
    void canQueryData() {
        try {
            SupabaseConnection supabaseConnection = SupabaseConnection.getInstance();
            PreparedStatement queryStatement = supabaseConnection.getConnection().prepareStatement("SELECT * FROM test_table WHERE id = ?");
            queryStatement.setInt(1, 1);
            ResultSet sqlReturnValues = queryStatement.executeQuery();
            while (sqlReturnValues.next()) {
                assertNotNull(sqlReturnValues.getString(1));
            }
        } catch (SQLException ex) {
            fail("Could not retrieve any data. Perhaps the table is empty?");
            ex.printStackTrace();
        }
    }

    @Test
    void supabaseIsInstanceOfSupabase() {
        SupabaseConnection supabaseConnection = SupabaseConnection.getInstance();
        assertTrue(supabaseConnection instanceof SupabaseConnection);
    }

}
