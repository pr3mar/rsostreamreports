package com.rsostream.reports.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rsostream.reports.models.DeviceReport;
import com.rsostream.reports.properties.PropertiesMongoDB;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@ApplicationScoped
public class ServiceMongoDB {

    private static final Logger log = LogManager.getLogger(ServiceMongoDB.class.getName());


    @Inject
    PropertiesMongoDB propertiesMongoDB;

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<DeviceReport> deviceReportCollection;

    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoClient = new MongoClient(
                new MongoClientURI(propertiesMongoDB.getUri())
                /*propertiesMongoDB.getUri(),
                MongoClientOptions.builder()
                        .codecRegistry(pojoCodecRegistry).build()*/
        );

        db = mongoClient.getDatabase(propertiesMongoDB.getDb());
        db = db.withCodecRegistry(pojoCodecRegistry);
        deviceReportCollection = db.getCollection(propertiesMongoDB.getDeviceCollection(), DeviceReport.class);
    }

    private void stop(@Observes @Destroyed(ApplicationScoped.class) Object destroyed) {
        mongoClient.close();
    }

    public List<DeviceReport> findReportsByDeviceID(ObjectId deviceId) {
        try {
            List<DeviceReport> docs = new ArrayList<DeviceReport>() {
            };
            Block<DeviceReport> reports = new Block<DeviceReport>() {
                @Override
                public void apply(final DeviceReport deviceReport) {
                    docs.add(deviceReport);
                }
            };
            deviceReportCollection.find(eq("deviceId", deviceId)).forEach(reports);
            log.info("Found: " + docs.size() + " reports.");
            return docs;
        } catch (NullPointerException e) {
            log.info("Error finding device reports.");
            e.printStackTrace();
            return null;
        }
    }

    public DeviceReport findByID(ObjectId id) {
        try {
            DeviceReport report = deviceReportCollection.find(eq("imei", id)).first();
            log.info("Found report:" + report);
            return report;
        } catch (NullPointerException e) {
            log.info("Error finding device reports.");
            e.printStackTrace();
            return null;
        }
    }

    public boolean createNewDeviceReport(DeviceReport report) throws Exception {
        try {
            deviceReportCollection.insertOne(report);
            return true;
        } catch (MongoWriteException e) {
            log.error("Device not unique!");
            e.printStackTrace();
            return false;
        }
    }
}
