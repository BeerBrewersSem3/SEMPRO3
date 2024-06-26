package beerbrewers.operation;

import beerbrewers.batch.Batch;
import beerbrewers.worker.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Perhaps this class should be generalized and not exist for each Model.
 */
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Operation findByOperationId(long operationId);
    List<Operation> findAllByWorker(Worker worker);

    List<Operation> findAllByBatchListContains(Batch batch);

}
