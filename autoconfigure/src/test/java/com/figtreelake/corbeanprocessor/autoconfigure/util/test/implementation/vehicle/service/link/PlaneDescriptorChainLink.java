package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Plane;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.VehicleType;

public class PlaneDescriptorChainLink extends AbstractVehicleDescriptorChainLink<Plane> {

  public PlaneDescriptorChainLink() {
    super(VehicleType.PLANE, Plane.class);
  }

  @SuppressWarnings("StringBufferReplaceableByString")
  @Override
  protected String describeDetails(Plane plane) {
    final var stringBuilder = new StringBuilder();

    stringBuilder.append(", it accepts up to ");
    stringBuilder.append(plane.getMaxPassengers());
    stringBuilder.append(plane.getMaxPassengers() == 1.0 ? " passenger" : " passengers");
    stringBuilder.append(" and can carry up to ");
    stringBuilder.append(plane.getMaxWeightKilograms());
    stringBuilder.append(plane.getMaxWeightKilograms() == 1.0 ? " kilogram" : " kilograms");
    stringBuilder.append(".");

    return stringBuilder.toString();
  }
}
