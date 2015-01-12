package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.PreconfiguredEnergySource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreconfiguredEnergySource() {
        //This is a pretty simple wrapper, so we can do it in one line.
        return Response.status(Response.Status.OK)
                .entity(convertAllEntitiesToApiModels(procedureDefinitionService.getAllPreconfiguredEnergySource()))
                .build();
    }

    /**
     * This just returns a list of all Preconfigured Energy Sources for the user's associated Tenant
     *
     * @return A Response containing all Preconfigured Energy Sources for the current Tenant.
     */
    @GET
    @Path("/defaultIsolationPointSourceTypes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDefaultIsolationPoints() {

        ArrayList<ApiIsolationPointSourceType> defaults = getDefaultValues();
        return Response.status(Response.Status.OK)
                .entity(defaults)
                .build();
    }

    @Override
    protected ApiEnergySource convertEntityToApiModel(PreconfiguredEnergySource entityModel) {
        ApiEnergySource energySource = new ApiEnergySource();

        energySource.setIsolationPointSourceType(entityModel.getIsolationPointSourceType().name());
        energySource.setSource(entityModel.getSource());

        return energySource;
    }

    protected ArrayList<ApiIsolationPointSourceType> getDefaultValues() {
        ArrayList<ApiIsolationPointSourceType> defaults = new ArrayList<>();
        for(IsolationPointSourceType sourceType:IsolationPointSourceType.values()) {
            ApiIsolationPointSourceType temp = new ApiIsolationPointSourceType();
            temp.setSource(sourceType.name());
            temp.setSourceText(sourceType.getIdentifier());

            defaults.add(temp);
        }
        return defaults;
    }

    public class ApiIsolationPointSourceType {
        private String source;
        private String sourceText;

        public ApiIsolationPointSourceType() {}

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getSourceText() {
            return sourceText;
        }

        public void setSourceText(String sourceText) {
            this.sourceText = sourceText;
        }
    }
}
