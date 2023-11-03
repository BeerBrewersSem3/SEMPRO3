package beerbrewers.sensor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SensorConfig {

    /**
     * The following method should be refactored to read the sensors from a txt file or JSON document.
     * @param repository
     * @return
     */
    @Bean
    CommandLineRunner sensorCommandLineRunner(SensorRepository repository) {
        return args -> {
            Sensor vibrationSensor = new Sensor(
                1, "Vibration sensor", 6, "::Program:Data.Value.Vibration"
            );

            Sensor temperatureSensor = new Sensor(
                2, "Temperature sensor", 6, "::Program:Data.Value.Temperature"
            );

            Sensor humiditySensor = new Sensor(
                3, "Humidity sensor", 6, "::Program:Data.Value.RelHumidity"
            );

            Sensor accelerationSensor1 = new Sensor(
                4, "Acceleration sensor 1", 6, "::Program:Data.Value.Acceleration1"
            );

            Sensor accelerationSensor2 = new Sensor(
                5, "Acceleration sensor 2", 6, "::Program:Data.Value.Acceleration2"
            );

            Sensor accelerationSensor3 = new Sensor(
                6, "Acceleration sensor 3", 6, "::Program:Data.Value.Acceleration3"
            );

            Sensor rotationSensor1 = new Sensor(
                7, "Rotation sensor 1", 6, "::Program:Data.Value.Rotation1"
            );

            Sensor rotationSensor2 = new Sensor(
                8, "Rotation sensor 2", 6, "::Program:Data.Value.Rotation2"
            );

            Sensor rotationSensor3 = new Sensor(
                9, "Rotation sensor 3", 6, "::Program:Data.Value.Rotation3"
            );

            repository.saveAll(
                    List.of(
                            vibrationSensor,
                            temperatureSensor,
                            humiditySensor,
                            accelerationSensor1,
                            accelerationSensor2,
                            accelerationSensor3,
                            rotationSensor1,
                            rotationSensor2,
                            rotationSensor3
                    )
            );
        };
    }
}
