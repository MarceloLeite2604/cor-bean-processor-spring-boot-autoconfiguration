package com.figtreelake.corbeanprocessor.autoconfigure;

import com.figtreelake.corbeanprocessor.autoconfigure.chain.ChainConfiguration;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.CorConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CorConfiguration.class)
@SpringBootTest(classes = ChainConfiguration.class)
class CorIT {

//  @Autowired
//  private VehicleService vehicleService;
//
//  @MethodSource("provideParametersForShouldProperlyDescribeVehiclesTest")
//  @ParameterizedTest
//  void shouldProperlyDescribeVehicles(Vehicle vehicle) {
//    final var description = vehicleService.retrieveDescription(vehicle);
//
//    System.out.println(description);
//    assertThat(description).isNotBlank();
//  }
//
//  private static Stream<Vehicle> provideParametersForShouldProperlyDescribeVehiclesTest() {
//    return Stream.of(
//        VehicleFixture.createCar(),
//        VehicleFixture.createBoat(),
//        VehicleFixture.createPlane()
//    );
//  }
}