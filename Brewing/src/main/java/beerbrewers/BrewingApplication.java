package beerbrewers;

import beerbrewers.brewmaster9000.models.Worker;
import beerbrewers.persistence.SupabaseConnection;
import beerbrewers.persistence.WorkerRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan("beerbrewers.brewmaster9000.models")
public class BrewingApplication
{
    private static final Logger log = LoggerFactory.getLogger(BrewingApplication.class);

    @Autowired
    private OpcuaClientConnection opcuaClientConnection;
    @Autowired
    private SupabaseConnection supabaseConnection;
    @PostConstruct
    public void initialize(){
        supabaseConnection.testQueryForDatabase();
    }
    public static void main(String[] args)
    {

        SpringApplication.run(BrewingApplication.class, args);
    }

    @Bean
    public CommandLineRunner databaseDemo(WorkerRepository workerRepository) {
        return (args) -> {
            //Save a few workers to test
            workerRepository.save(new Worker("John", "habahabasutsut"));
            workerRepository.save(new Worker("Jane", "hunter123"));

            //Fetch all workers
            log.info("Workers found with findAll():");
            log.info("-------------------------------");
            workerRepository.findAll().forEach(worker -> {
                log.info(worker.toString());
            });
            log.info("");

            //Fetch an individual worker by ID
            Worker worker = workerRepository.findByWorkerId(1);
            log.info("Customer found with findByWorkerId(1):");
            log.info("--------------------------------");
            log.info(worker.toString());
            log.info("");

            //Fetch worker(s) by name
            log.info("Worker found with findAllByName('John'):");
            log.info("--------------------------------------------");
            workerRepository.findAllByName("John").forEach(john -> {
                log.info(john.toString());
            });
            log.info("");
        };

    }
}