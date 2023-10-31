package beerbrewers.brewing;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;


@Component
public class SupabaseConnection {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SupabaseConnection(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void testQueryForDatabase() {
        try {

        String query = "SELECT * FROM test_table WHERE id = ?";
        System.out.println("Database query status: " + jdbcTemplate.queryForObject(query, String.class, 1));
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

    }
}
