package beerbrewers.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

/**
 * This class uses the JdbcTemplate from the Spring framework to handle the connection to the database hosted on
 * Supabase.
 */
@Component
public class SupabaseConnection {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SupabaseConnection(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Simple test query that fetches a test string from a test_table in the database, and prints it in the console
     * to show that the application has access to the database and can receive query results.
     */
    public boolean testQueryForDatabase() {
        try {
            String query = "SELECT information FROM test_table WHERE id = ?";
            System.out.println("Database query status: " + jdbcTemplate.queryForObject(query, String.class, 1));
            return true;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No data found for the specified ID.");
            // Handle accordingly or rethrow if needed.
        } catch (BadSqlGrammarException e) {
            System.out.println("SQL syntax error: " + e.getMessage());
            // Handle accordingly or rethrow if needed.
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Database connection issue: " + e.getMessage());
            // Handle accordingly or rethrow if needed.
        } catch (DataAccessException e) {
            System.out.println("Data access error: " + e.getMessage());
            // Handle accordingly or rethrow if needed.
        }
        return false;
    }
}
