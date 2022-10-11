package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.Vehicle;

public interface VehicleDescriptorChainLink extends ChainLink<VehicleDescriptorChainLink> {

  public String describe(Vehicle vehicle);
}
