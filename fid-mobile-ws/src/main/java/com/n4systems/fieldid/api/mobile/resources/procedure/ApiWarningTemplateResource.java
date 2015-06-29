package com.n4systems.fieldid.api.mobile.resources.procedure;

import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.fieldid.api.mobile.resources.ApiResource;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This is a simple REST Service to provide predefined WarningTemplates from the
 *
 * Created by Jordan Heath on 11/26/14.
 */
@Path("warningTemplates")
public class ApiWarningTemplateResource extends ApiResource<ApiWarningTemplate, WarningTemplate> {

    @Autowired
    private WarningTemplateService warningTemplateService;

    //Static error messages
    private static final String BAD_ID_ERROR = "You step in the stream,\n" +
                                               "But the water has moved on.\n" +
                                               "This resource is gone.";

    private static final String COULD_NOT_WRITE = "Three things are certain:\n" +
                                                  "Death, taxes and lost data.\n" +
                                                  "Guess which has occurred.";

    private static final String RESOURCE_NOT_FOUND = "Having been erased,\n" +
                                                     "The document you're seeking\n" +
                                                     "Must now be retyped.";

    /**
     * This just returns a list of all Warning Templates for the user's associated Tenant by wrapping the
     * <b>WarningTemplateService</b>.
     *
     * @return A Response containing all Warning Templates for the current Tenant.
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWarningTemplateList() {
        //This is a pretty simple wrapper, so we can do it in one line.
        return Response.status(Response.Status.OK)
                       .entity(convertAllEntitiesToApiModels(warningTemplateService.getAllTemplatesForTenant()))
                       .build();
    }

    /**
     * This method is used to update an existing WarningTemplate.  The id and provided entity must have matching IDs.
     *
     * @param idParam - A <b>String</b> value that must be able to parse to a <b>Long</b>
     * @param apiWarningTemplate - An <b>ApiWarningTemplate</b> representing the warning_template row to update.
     * @return A Response containing a valid status code and no content.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWarningTemplate(@PathParam("id") String idParam,
                                          ApiWarningTemplate apiWarningTemplate) {

        try {
            Long id = Long.parseLong(idParam);

            if(id.equals(Long.parseLong(apiWarningTemplate.getSid()))) {
                //Read current entity from the DB...
                WarningTemplate fromDB = warningTemplateService.loadById(id);

                //Update the only passed-in fields we care about...
                fromDB.setName(apiWarningTemplate.getName());
                fromDB.setWarning(apiWarningTemplate.getWarning());

                //update back to DB.
                warningTemplateService.update(fromDB);

                //Good to go, pass back No Content status because there's no content to return.
                return Response.noContent().build();
            } else {
                throw new Exception("ID must match resource in JSON!!");
            }
        } catch (NumberFormatException e) {
            //ID Can't e parsed to a Long... must be an invalid resource.
            return Response.status(Response.Status.BAD_REQUEST).entity(BAD_ID_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You talk of one resource, but what to update another.  Forbidden.").build();
        }
    }

    /**
     * This method is used to create a Warning Template.
     *
     * @param apiWarningTemplate - An <b>ApiWarningTemplate</b> JSON Entity.
     * @return A Response containing a facsimile of the created warning_templates row.
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createWarningTemplate(ApiWarningTemplate apiWarningTemplate) {
        WarningTemplate warningTemplate = new WarningTemplate();
        warningTemplate.setName(apiWarningTemplate.getName());
        warningTemplate.setWarning(apiWarningTemplate.getWarning());
        warningTemplate.setTenant(getCurrentTenant());
        warningTemplate = warningTemplateService.save(warningTemplate);

        if(warningTemplate != null) {
            return Response.status(Response.Status.CREATED)
                           .entity(warningTemplate)
                           .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(COULD_NOT_WRITE)
                           .build();
        }
    }

    /**
     * This method will delete the Entity with the provided ID from the database.
     *
     * @param idParam - A String value which must be able to be parsed to a Long.
     * @return A Response containing no content and a valid Status code.
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteWarningTemplate(@PathParam("id") String idParam) {
        try {
            Long id = Long.parseLong(idParam);

            WarningTemplate fromDB = warningTemplateService.loadById(id);
            warningTemplateService.delete(fromDB);

            //Delete succeeded.  No need for
            return Response.noContent().build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(BAD_ID_ERROR)
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(RESOURCE_NOT_FOUND)
                           .build();
        }
    }

    /**
     * Mandatory implementation of the abstract <b>convertEntityToApiModel(...)</b> method.  This is used to convert
     * Lists of entities into Lists of Api Models.
     *
     * @param entityModel - A <b>WarningTemplate</b> JPA Entity.
     * @return An <b>ApiWarningTemplate</b> model object.
     */
    @Override
    protected ApiWarningTemplate convertEntityToApiModel(WarningTemplate entityModel) {
        ApiWarningTemplate apiWarningTemplate = new ApiWarningTemplate(entityModel.getName(), entityModel.getWarning());
        apiWarningTemplate.setSid(entityModel.getId().toString());
        apiWarningTemplate.setModified(entityModel.getModified());

        return apiWarningTemplate;
    }
}
