package beerbrewers.operationinputs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Perhaps this class should be generalized and not exist for each Model.
 */
@Repository
public interface OperationInputsRepository extends JpaRepository<OperationInputs, Long> {

    OperationInputs findByOperationInputsId(Long operationInputsId);

    List<OperationInputs> findAllByName(Enum<OperationInputsEnum> name);

    List<OperationInputs> findAllByTimestampBetween(String timeStart, String timeEnd);

    List<OperationInputs> findAllByOrderByTimestamp();
}