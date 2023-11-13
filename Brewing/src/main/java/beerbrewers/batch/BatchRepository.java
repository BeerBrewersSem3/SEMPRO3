package beerbrewers.batch;

import beerbrewers.operation.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Perhaps this class should be generalized and not exist for each Model.
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    Batch findByBatchId(Long batchId);

    List<Batch> findAllByCompletedIs(boolean bool);

    List<Batch> findAllByAmountBetween(long minAmount, long maxAmount);

    List<Batch> findAllByOperation(Operation operation);

    List<Batch> findAllByBrewName(BrewEnum brewName);
}