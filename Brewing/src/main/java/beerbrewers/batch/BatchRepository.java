package beerbrewers.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Perhaps this class should be generalized and not exist for each Model.
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    Batch findByBatchId(long batchId);
    List<Batch> findAllByName(String name);
}