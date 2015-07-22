package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.energysources;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.procedure.PreconfiguredEnergySource;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("energySource")
public class ApiEnergySourceResource extends SetupDataResourceReadOnly<ApiEnergySource, PreconfiguredEnergySource> {

    public ApiEnergySourceResource() {
        super(PreconfiguredEnergySource.class, true);
    }

    @Override
    protected ApiEnergySource convertEntityToApiModel(PreconfiguredEnergySource source) {
        ApiEnergySource energySource = new ApiEnergySource();
        energySource.setSid(source.getId());
        energySource.setActive(true);
        energySource.setModified(source.getModified());
        energySource.setIsolationPointSourceType(source.getIsolationPointSourceType().name());
        energySource.setSource(source.getSource());
        return energySource;
    }

}
