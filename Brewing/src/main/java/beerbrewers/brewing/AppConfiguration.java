package beerbrewers.brewing;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("beerbrewers.brewing")
@PropertySource("classpath:application.properties")
public class AppConfiguration {
    @Autowired
    Environment environment;

    private final String URL = "jdbc:postgresql://db.hbdjtgvmsnqkxcufwgwh.supabase.co:5432/postgres";
    private final String USER = "postgres";
    private final String DRIVER = "org.postgresql.Driver";
    private final String PASSWORD = "stivpikoghaardeslag";

    @Bean
    DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(URL));
        driverManagerDataSource.setUsername(environment.getProperty(USER));
        driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
        driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
        return driverManagerDataSource;
    }

}
