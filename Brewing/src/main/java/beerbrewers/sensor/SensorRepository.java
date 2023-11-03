package beerbrewers.sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Sensor findBySensorId(long sensorId);
    List<Sensor> findAllByName(String name);

}
