package beerbrewers.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SupabaseConnection.class);
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
            logger.info("Database query status: " + jdbcTemplate.queryForObject(query, String.class, 1));
            return true;
        } catch (EmptyResultDataAccessException e) {
            logger.error("No data found for the specified ID.");
            // Handle accordingly or rethrow if needed.
        } catch (BadSqlGrammarException e) {
            logger.error("SQL syntax error: " + e.getMessage());
            // Handle accordingly or rethrow if needed.
        } catch (CannotGetJdbcConnectionException e) {
            logger.error("Database connection issue: " + e.getMessage());
            // Handle accordingly or rethrow if needed.
        } catch (DataAccessException e) {
            logger.error("Data access error: " + e.getMessage());
            // Handle accordingly or rethrow if needed.
        }
        return false;
    }
}

