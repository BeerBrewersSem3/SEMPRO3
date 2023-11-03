package beerbrewers.worker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring will auto implement this into a bean.
 */

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Worker findByWorkerId(long workerId);
    List<Worker> findAllByName(String name);

}
