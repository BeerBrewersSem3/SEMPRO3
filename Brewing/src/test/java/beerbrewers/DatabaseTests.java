package beerbrewers;

import beerbrewers.persistence.SupabaseConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseTests {
    @Autowired
    private SupabaseConnection supabaseConnection;

    @Test
    void canFetchFromTestTable() {
        assertTrue(supabaseConnection.testQueryForDatabase());
    }

}
