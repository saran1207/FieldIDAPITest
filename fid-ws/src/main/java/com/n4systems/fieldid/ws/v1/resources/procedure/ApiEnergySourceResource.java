package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.procedure.PreconfiguredEnergySource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rrana on 2015-01-07.
 */
@Path("energySources")
public class ApiEnergySourceResource extends ApiResource<ApiEnergySource, PreconfiguredEnergySource> {


    @Autowired
    private ProcedureDefinitionService procedureDefinitionService;

    /**
     * This just returns a list of all Preconfigured Energy Sources for the user's associated Tenant
     *
     * @return A Response containing all Preconfigured Energy Sources for the current Tenant.
     */
    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPreconfiguredEnergySource() {
        //This is a pretty simple wrapper, so we can do it in one line.
        return Response.status(Response.Status.OK)
                .entity(convertAllEntitiesToApiModels(procedureDefinitionService.getAllPreconfiguredEnergySource()))
                .build();
    }


    @Override
    protected ApiEnergySource convertEntityToApiModel(PreconfiguredEnergySource entityModel) {
        ApiEnergySource energySource = new ApiEnergySource();

        energySource.setIsolationPointSourceType(entityModel.getIsolationPointSourceType().name());
        energySource.setSource(entityModel.getSource());

        return energySource;
    }
}
