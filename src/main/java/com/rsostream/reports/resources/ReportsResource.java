package com.rsostream.reports.resources;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.rsostream.reports.properties.PropertiesCRUD;
import com.rsostream.reports.services.ServiceMongoDB;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Log
@Path("/reports")
@RequestScoped

public class ReportsResource {
    private static final Logger log = LogManager.getLogger(ReportsResource.class.getName());

    @Inject
    @DiscoverService(value = "rsostream-crud", version = "1.0.0", environment = "dev")
    private Optional<WebTarget> crudService;

    @Inject
    private PropertiesCRUD propertiesCRUD;

    @Inject
    private ServiceMongoDB serviceMongoDB;

    @GET
    public Response getReports() {
        return Response.ok().build();
    }
}
