package beerbrewers.sensor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SensorConfig {

    /**
     * The following method could be refactored to read the sensors from a txt file or JSON document.
     * @param repository
     * @return
     */
    @Bean
    CommandLineRunner sensorCommandLineRunner(SensorRepository repository) {
        return args -> {
            Sensor vibrationSensor = new Sensor(
                1L, "Vibration sensor", 6, "::Program:Data.Value.Vibration"
            );

            Sensor temperatureSensor = new Sensor(
                2L, "Temperature sensor", 6, "::Program:Data.Value.Temperature"
            );

            Sensor humiditySensor = new Sensor(
                3L, "Humidity sensor", 6, "::Program:Data.Value.RelHumidity"
            );

            Sensor speedSensor = new Sensor(
                4L, "Speed sensor", 6, "::Program:Cube.Status.CurMachSpeed"
            );

            repository.saveAll(
                    List.of(
                            vibrationSensor,
                            temperatureSensor,
                            humiditySensor,
                            speedSensor
                    )
            );
        };
    }
}
