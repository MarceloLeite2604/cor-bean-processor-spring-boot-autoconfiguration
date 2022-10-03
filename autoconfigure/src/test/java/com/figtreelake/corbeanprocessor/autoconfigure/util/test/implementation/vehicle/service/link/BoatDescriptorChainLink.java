package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Boat;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.VehicleType;

public class BoatDescriptorChainLink extends AbstractVehicleDescriptorChainLink<Boat> {

  public BoatDescriptorChainLink() {
    super(VehicleType.BOAT, Boat.class);
  }

  @SuppressWarnings("StringBufferReplaceableByString")
  @Override
  protected String describeDetails(Boat boat) {
    final var stringBuilder = new StringBuilder();

    stringBuilder.append(", it has ");
    stringBuilder.append(boat.getLengthMeters());
    stringBuilder.append(boat.getLengthMeters() == 1.0 ? " meter" : " meters");
    stringBuilder.append(" of length and is powered by ");
    stringBuilder.append(boat.getPoweredBy());
    stringBuilder.append(".");

    return stringBuilder.toString();
  }
}
