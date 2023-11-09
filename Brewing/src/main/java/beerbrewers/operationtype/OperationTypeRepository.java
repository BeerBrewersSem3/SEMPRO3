package beerbrewers.operationtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Perhaps this class should be generalized and not exist for each Model.
 */
@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
    OperationType findByOperationTypeId(long operationTypeId);
    OperationType findByName (String name);
}
