package beerbrewers.brewing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.*;

@SpringBootApplication
public class BrewingApplication
{
    @Autowired
    private OpcuaClientConnection opcuaClientConnection;
    public static void main(String[] args)
    {

        SpringApplication.run(BrewingApplication.class, args);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        SupabaseConnection supabaseConnection = new SupabaseConnection(context.getBean(DataSource.class));
        supabaseConnection.testQueryForDatabase();
    }

}