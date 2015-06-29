package com.n4systems.fieldid.api.mobile.resources.procedure;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.api.mobile.resources.ApiResource;
import com.n4systems.model.procedure.PreconfiguredDevice;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rrana on 2015-01-07.
 */
@Path("preconfiguredDevices")
public class ApiPreconfiguredDeviceResource extends ApiResource<ApiPreconfiguredDevice, PreconfiguredDevice> {

    @Autowired
    private ProcedureDefinitionService procedureDefinitionService;

    /**
     * This just returns a list of all Preconfigured Devices for the user's associated Tenant
     *
     * @return A Response containing all Preconfigured Devices for the current Tenant.
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreconfiguredDeviceList() {
        //This is a pretty simple wrapper, so we can do it in one line.
        return Response.status(Response.Status.OK)
                .entity(convertAllEntitiesToApiModels(procedureDefinitionService.getAllPreConfiguredDevices()))
                .build();
    }

    @Override
    protected ApiPreconfiguredDevice convertEntityToApiModel(PreconfiguredDevice entityModel) {
        ApiPreconfiguredDevice preconfiguredDevice = new ApiPreconfiguredDevice();

        if(entityModel.getIsolationPointSourceType() == null) {
            preconfiguredDevice.setIsolationPointSourceType("null");
        } else {
            preconfiguredDevice.setIsolationPointSourceType(entityModel.getIsolationPointSourceType().name());
        }
        preconfiguredDevice.setDevice(entityModel.getDevice());
        preconfiguredDevice.setMethod(entityModel.getMethod());

        return preconfiguredDevice;
    }
}