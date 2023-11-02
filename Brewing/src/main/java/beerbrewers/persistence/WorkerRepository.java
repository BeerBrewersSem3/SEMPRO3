package beerbrewers.persistence;

import beerbrewers.brewmaster9000.models.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring will auto implement this into a bean.
 */

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {
    Worker findByWorkerId(long workerId);
    List<Worker> findAllByName(String name);

}
