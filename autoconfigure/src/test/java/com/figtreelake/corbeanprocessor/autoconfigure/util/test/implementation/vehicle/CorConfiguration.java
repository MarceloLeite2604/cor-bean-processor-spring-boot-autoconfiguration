package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.VehicleService;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link.BoatDescriptorChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link.CarDescriptorChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link.PlaneDescriptorChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle.service.link.VehicleDescriptorChainLink;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CorConfiguration {

  @Bean("boatDescriptorChainLink")
  public BoatDescriptorChainLink createBoatDescriptorChainLink() {
    return new BoatDescriptorChainLink();
  }

  @Bean("planeDescriptorChainLink")
  public PlaneDescriptorChainLink createPlaneDescriptorChainLink() {
    return new PlaneDescriptorChainLink();
  }

  @Bean("carDescriptorChainLink")
  public CarDescriptorChainLink createCarDescriptorChainLink() {
    return new CarDescriptorChainLink();
  }

  @Bean("vehicleService")
  public VehicleService createVehicleService(VehicleDescriptorChainLink firstVehicleDescriptorChainLink) {
    return new VehicleService(firstVehicleDescriptorChainLink);
  }
}
