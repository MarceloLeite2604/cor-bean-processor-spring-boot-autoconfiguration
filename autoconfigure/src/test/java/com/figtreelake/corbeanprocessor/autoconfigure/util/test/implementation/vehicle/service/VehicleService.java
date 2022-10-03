package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Vehicle;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link.VehicleDescriptorChainLink;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VehicleService {

  private final VehicleDescriptorChainLink vehicleChainLink;

  public String retrieveDescription(Vehicle vehicle) {
    return vehicleChainLink.describe(vehicle);
  }
}
