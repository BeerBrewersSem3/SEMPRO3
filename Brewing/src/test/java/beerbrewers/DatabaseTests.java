package beerbrewers;

import beerbrewers.persistence.SupabaseConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DatabaseTests {

    @Autowired
    private SupabaseConnection supabaseConnection;

    @Test
    public void testDataSourceInjection() {
        assertNotNull(supabaseConnection);
        assertNotNull(supabaseConnection.testQueryForDatabase());
    }
}