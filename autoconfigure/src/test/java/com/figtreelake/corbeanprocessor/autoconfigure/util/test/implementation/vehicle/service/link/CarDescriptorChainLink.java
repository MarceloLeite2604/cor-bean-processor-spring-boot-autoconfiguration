package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Car;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.VehicleType;

public class CarDescriptorChainLink extends AbstractVehicleDescriptorChainLink<Car> {

  public CarDescriptorChainLink() {
    super(VehicleType.CAR, Car.class);
  }

  @SuppressWarnings("StringBufferReplaceableByString")
  @Override
  protected String describeDetails(Car car) {
    final var stringBuilder = new StringBuilder();

    stringBuilder.append(", it has ");
    stringBuilder.append(car.getSeats());
    stringBuilder.append(car.getSeats() == 1 ? " seat" : " seats");
    stringBuilder.append(" and ");
    stringBuilder.append(car.getDoors());
    stringBuilder.append(car.getDoors() == 1 ? " door" : " doors");
    stringBuilder.append(".");

    return stringBuilder.toString();
  }
}
