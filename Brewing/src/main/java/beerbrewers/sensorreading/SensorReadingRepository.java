package beerbrewers.sensorreading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, SensorReadingId> {
    SensorReading findBySensorReadingId(SensorReadingId sensorReadingId);
    List<SensorReading> findAllBySensorReadingId_OperationId(long operationId);
    List<SensorReading> findAllBySensorReadingId_SensorId(long sensorId);
}
