package com.rsostream.reports.resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.rsostream.reports.models.Device;
import com.rsostream.reports.models.DeviceReport;
import com.rsostream.reports.models.sensorReadings.*;
import com.rsostream.reports.properties.PropertiesCRUD;
import com.rsostream.reports.services.ServiceCrud;
import com.rsostream.reports.services.ServiceMongoDB;
import com.rsostream.reports.util.GsonUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@Path("/reports")
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReportsResource {
    private static final Logger log = LogManager.getLogger(ReportsResource.class.getName());

    @Inject
    @DiscoverService(value = "rsostream-crud", version = "1.0.0", environment = "dev")
    private Optional<WebTarget> crudService;

    @Inject
    private PropertiesCRUD propertiesCRUD;

    @Inject
    private ServiceMongoDB serviceMongoDB;

    @Inject
    private ServiceCrud serviceCrud;

    @GET
    public Response getReports() {
        return Response.ok().build();
    }

    @GET
    @Path("/fib/{n}")
    public Response getFib(@PathParam("n") int n) {
        return Response.ok(fib(n)).build();
    }

    private long fib(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fib(n - 1) + fib(n - 2);
        }
    }

    @GET
    @Path("/device/{imei}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDevice(@PathParam("imei") String imei) {
        Device device = serviceCrud.getDeviceByIMEI(imei);
        Gson deserializer = GsonUtils.getGson();
        if (device == null) {
            return Response.noContent().build();
        }
        return Response.ok(deserializer.toJson(device)).build();
    }

    @GET
    @Path("/{imei}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAltReadings(@PathParam("imei") String imei) {
        List<AltitudeReading> altitudeReadings = serviceCrud.getAltReadingsByIMEI(imei);
        List<BatteryReading> batteryReadings = serviceCrud.getBatReadingsByIMEI(imei);
        List<GPSReading> gpsReadings = serviceCrud.getGPSReadingsByIMEI(imei);
        List<HumidityReading> humidityReadings = serviceCrud.getHumReadingsByIMEI(imei);
        List<LuxReading> luxReadings = serviceCrud.getLuxReadingsByIMEI(imei);
        List<SensorReading> readings = new ArrayList<SensorReading>();
        readings.addAll(altitudeReadings);
        readings.addAll(batteryReadings);
        readings.addAll(gpsReadings);
        readings.addAll(humidityReadings);
        readings.addAll(luxReadings);
        DeviceReport report = new DeviceReport(imei, readings);
        Gson deserializer = GsonUtils.getGson();
        return Response.ok(deserializer.toJson(report)).build();
//        Type type = new TypeToken<List<SensorReading>>(){}.getType();
//        return Response.ok(deserializer.toJson(readings, type)).build();
    }


}
