package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Vehicle;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.VehicleType;
import lombok.Setter;
import org.springframework.util.Assert;

public abstract class AbstractVehicleDescriptorChainLink<T extends Vehicle> implements VehicleDescriptorChainLink {

  @Setter
  private VehicleDescriptorChainLink next;

  private final VehicleType typeHandled;

  private final Class<T> vehicleClass;

  public AbstractVehicleDescriptorChainLink(VehicleType typeHandled, Class<T> vehicleClass) {
    this.typeHandled = typeHandled;
    this.vehicleClass = vehicleClass;
  }

  @Override
  public String describe(Vehicle vehicle) {
    Assert.notNull(vehicle, "Vehicle cannot be null.");

    if (canDescribe(vehicle)) {
      return doDescribe(vehicle);
    }

    if (next != null) {
      return next.describe(vehicle);
    }

    final var message = String.format("Cannot describe vehicle of type \"%s\".", vehicle.getType()
        .getValue());
    throw new IllegalArgumentException(message);
  }

  private boolean canDescribe(final Vehicle vehicle) {
    return typeHandled.equals(vehicle.getType());
  }

  @SuppressWarnings("StringBufferReplaceableByString")
  private String doDescribe(Vehicle vehicle) {
    final var stringBuilder = new StringBuilder();

    stringBuilder.append("Vehicle is a ");
    stringBuilder.append(vehicle.getType()
        .getValue());
    stringBuilder.append(" named \"");
    stringBuilder.append(vehicle.getName());
    stringBuilder.append("\" that can reach up to ");
    stringBuilder.append(vehicle.getMaxSpeedKph());
    stringBuilder.append("km/h of speed ");

    stringBuilder.append(describeDetails(vehicleClass.cast(vehicle)));

    return stringBuilder.toString();
  }

  protected abstract String describeDetails(T vehicle);

  ;
}
