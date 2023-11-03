package beerbrewers.sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Perhaps this class should be generalized and not exist for each Model.
 */
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Sensor findBySensorId(long sensorId);
    List<Sensor> findAllByName(String name);

}
