package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Boat;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Car;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Plane;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Vehicle;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.VehicleType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VehicleFixture {

  public static Vehicle createCar() {
    return Car.builder()
        .type(VehicleType.CAR)
        .name("Toyota Prius")
        .maxSpeedKph(170.6)
        .doors(4)
        .seats(5)
        .build();
  }

  public static Vehicle createBoat() {
    return Boat.builder()
        .type(VehicleType.BOAT)
        .name("Enchantment Of The Seas cruise ship")
        .maxSpeedKph(40.74)
        .lengthMeters(279.1)
        .poweredBy("explosive engine")
        .build();
  }

  public static Plane createPlane() {
    return Plane.builder()
        .type(VehicleType.PLANE)
        .name("Supermarine Spitfire")
        .maxSpeedKph(594)
        .maxPassengers(1)
        .maxWeightKilograms(453.6)
        .build();
  }
}
