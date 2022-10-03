package com.figtreelake.corbeanprocessor.autoconfigure;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.VehicleFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.CorConfiguration;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Vehicle;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.VehicleService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CorConfiguration.class)
@SpringBootTest(classes = ChainConfiguration.class)
class CorIT {

  @Autowired
  private VehicleService vehicleService;

  @MethodSource("provideParametersForShouldProperlyDescribeVehiclesTest")
  @ParameterizedTest
  void shouldProperlyDescribeVehicles(Vehicle vehicle) {
    final var description = vehicleService.retrieveDescription(vehicle);

    System.out.println(description);
    assertThat(description).isNotBlank();
  }

  private static Stream<Vehicle> provideParametersForShouldProperlyDescribeVehiclesTest() {
    return Stream.of(
        VehicleFixture.createCar(),
        VehicleFixture.createBoat(),
        VehicleFixture.createPlane()
    );
  }
}