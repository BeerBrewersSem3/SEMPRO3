package beerbrewers.sensorreading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    SensorReading findBySensorReadingId(Long sensorReadingId);
}
