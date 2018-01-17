package com.rsostream.reports.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.fault.tolerance.annotations.GroupKey;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.rsostream.reports.models.Device;
import com.rsostream.reports.models.sensorReadings.*;
import com.rsostream.reports.properties.PropertiesCRUD;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
@GroupKey("crud")
public class ServiceCrud {

    private static final Logger log = LogManager.getLogger(ServiceCrud.class.getName());

    @Inject
    private PropertiesCRUD crudProperties;

    @Inject
    private ServiceMongoDB serviceMongoDB;

    @Inject
    @DiscoverService(value = "rsostream-crud", version = "1.0.0", environment = "dev")
    Optional<WebTarget> crudService;

    @Log
    @Metered
    @CircuitBreaker
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallabackGetDevice")
    public Device getDeviceByIMEI(String imei) {
        if(crudService.isPresent()) {
            String path = crudProperties.getDeviceBaseUri() + imei;
            WebTarget service = crudService.get().path(path);
            log.error("Following uri: " + service.getUri().toString());
            Device device = null;
            try {
//                Response response = service.request(MediaType.APPLICATION_JSON).get();
                Response response = service.request().get();
//                log.error("GOT TO HERE!" + response.getEntity());
                device = (new ObjectMapper().readValue(response.readEntity(String.class), Device.class));
//                log.error("THIS IS WHAT I GOT:" + device.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return new Device();
            }
            return device;
        } else {
            log.error("Service not present!");
            return new Device();
        }
    }

    @Log
    @Metered
    @CircuitBreaker
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallabackGetAlt")
    public List<AltitudeReading> getAltReadingsByIMEI(String imei) {
        if(crudService.isPresent()) {
            String path = crudProperties.getReadingBaseUri() + "alt/" + imei;
            WebTarget service = crudService.get().path(path);
            log.error("Following uri: " + service.getUri().toString());
            List<AltitudeReading> altitudeReadings = null;
            try {
//                Response response = service.request(MediaType.APPLICATION_JSON).get();
                Response response = service.request().get();
//                log.error("GOT TO HERE!" + response.getEntity());
                altitudeReadings = (new ObjectMapper()
                        .readValue(
                                response.readEntity(String.class),
                                new TypeReference<List<AltitudeReading>>(){}
                            )
                );
//                log.error("THIS IS WHAT I GOT:" + altitudeReadings.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<AltitudeReading>();
            }
            return altitudeReadings;
        } else {
            log.error("Service not present!");
            return new ArrayList<AltitudeReading>();
        }
    }

    @Log
    @Metered
    @CircuitBreaker
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallabackGetBat")
    public List<BatteryReading> getBatReadingsByIMEI(String imei) {
        if(crudService.isPresent()) {
            String path = crudProperties.getReadingBaseUri() + "bat/" + imei;
            WebTarget service = crudService.get().path(path);
            log.error("Following uri: " + service.getUri().toString());
            List<BatteryReading> batteryReadings = null;
            try {
//                Response response = service.request(MediaType.APPLICATION_JSON).get();
                Response response = service.request().get();
//                log.error("GOT TO HERE!" + response.getEntity());
                batteryReadings = (new ObjectMapper()
                        .readValue(
                                response.readEntity(String.class),
                                new TypeReference<List<BatteryReading>>(){}
                            )
                );
//                log.error("THIS IS WHAT I GOT:" + batteryReadings.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<BatteryReading>();
            }
            return batteryReadings;
        } else {
            log.error("Service not present!");
            return new ArrayList<BatteryReading>();
        }
    }

    @Log
    @Metered
    @CircuitBreaker
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallabackGetGps")
    public List<GPSReading> getGPSReadingsByIMEI(String imei) {
        if(crudService.isPresent()) {
            String path = crudProperties.getReadingBaseUri() + "gps/" + imei;
            WebTarget service = crudService.get().path(path);
            log.error("Following uri: " + service.getUri().toString());
            List<GPSReading> gpsReadings = null;
            try {
//                Response response = service.request(MediaType.APPLICATION_JSON).get();
                Response response = service.request().get();
//                log.error("GOT TO HERE!" + response.getEntity());
                gpsReadings = (new ObjectMapper()
                        .readValue(
                                response.readEntity(String.class),
                                new TypeReference<List<GPSReading>>(){}
                            )
                );
//                log.error("THIS IS WHAT I GOT:" + gpsReadings.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<GPSReading>();
            }
            return gpsReadings;
        } else {
            log.error("Service not present!");
            return new ArrayList<GPSReading>();
        }
    }

    @Log
    @Metered
    @CircuitBreaker
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallabackGetHum")
    public List<HumidityReading> getHumReadingsByIMEI(String imei) {
        if(crudService.isPresent()) {
            String path = crudProperties.getReadingBaseUri() + "hum/" + imei;
            WebTarget service = crudService.get().path(path);
            log.error("Following uri: " + service.getUri().toString());
            List<HumidityReading> humidityReadings = null;
            try {
//                Response response = service.request(MediaType.APPLICATION_JSON).get();
                Response response = service.request().get();
//                log.error("GOT TO HERE!" + response.getEntity());
                humidityReadings = (new ObjectMapper()
                        .readValue(
                                response.readEntity(String.class),
                                new TypeReference<List<HumidityReading>>(){}
                            )
                );
//                log.error("THIS IS WHAT I GOT:" + humidityReadings.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<HumidityReading>();
            }
            return humidityReadings;
        } else {
            log.error("Service not present!");
            return new ArrayList<HumidityReading>();
        }
    }

    @Log
    @Metered
    @CircuitBreaker
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallabackGetLux")
    public List<LuxReading> getLuxReadingsByIMEI(String imei) {
        if(crudService.isPresent()) {
            String path = crudProperties.getReadingBaseUri() + "lux/" + imei;
            WebTarget service = crudService.get().path(path);
            log.error("Following uri: " + service.getUri().toString());
            List<LuxReading> luxReadings = null;
            try {
//                Response response = service.request(MediaType.APPLICATION_JSON).get();
                Response response = service.request().get();
//                log.error("GOT TO HERE!" + response.getEntity());
                luxReadings = (new ObjectMapper()
                        .readValue(
                                response.readEntity(String.class),
                                new TypeReference<List<LuxReading>>(){}
                            )
                );
//                log.error("THIS IS WHAT I GOT:" + luxReadings.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<LuxReading>();
            }
            return luxReadings;
        } else {
            log.error("Service not present!");
            return new ArrayList<LuxReading>();
        }
    }

    public Device fallabackGetDevice(String imei) {
        log.error("GONE TO FALLBACK METHOD!");
        return new Device();
    }
    public List<AltitudeReading> fallabackGetAlt(String imei) {
        log.error("GONE TO FALLBACK METHOD!");
        return new ArrayList<AltitudeReading>();
    }
    public List<BatteryReading> fallabackGetBat(String imei) {
        log.error("GONE TO FALLBACK METHOD!");
        return new ArrayList<BatteryReading>();
    }
    public List<GPSReading> fallabackGetGps(String imei) {
        log.error("GONE TO FALLBACK METHOD!");
        return new ArrayList<GPSReading>();
    }
    public List<HumidityReading> fallabackGetHum(String imei) {
        log.error("GONE TO FALLBACK METHOD!");
        return new ArrayList<HumidityReading>();
    }
    public List<LuxReading> fallabackGetLux(String imei) {
        log.error("GONE TO FALLBACK METHOD!");
        return new ArrayList<LuxReading>();
    }
}
