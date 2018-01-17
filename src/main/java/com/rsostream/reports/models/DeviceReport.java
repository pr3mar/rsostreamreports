package com.rsostream.reports.models;

import com.rsostream.reports.models.sensorReadings.SensorReading;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class DeviceReport {
    private ObjectId id;
    private String imei;
    private Date dateFrom;
    private Date dateTo;
    private int numberOfReadings;
    private int numberOfAlerts;
    private List<SensorReading> readingList;

    public DeviceReport() {
    }

    public DeviceReport(String imei, Date dateFrom, Date dateTo) {
        this.imei = imei;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public DeviceReport(ObjectId id, String imei,
                        Date dateFrom, Date dateTo,
                        int numberOfReadings,
                        int numberOfAlerts,
                        List<SensorReading> readings) {
        this.id = id;
        this.imei = imei;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.numberOfReadings = numberOfReadings;
        this.numberOfAlerts = numberOfAlerts;
        this.readingList = readings;
    }

    public DeviceReport(String imei, List<SensorReading> readingList) {
        this.imei = imei;
        this.numberOfReadings = readingList.size();
        this.readingList = readingList;
        this.dateTo = readingList.stream().map(u -> u.getTimeObtained()).max(Date::compareTo).get();
        this.dateFrom = readingList.stream().map(u -> u.getTimeObtained()).min(Date::compareTo).get();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public int getNumberOfReadings() {
        return numberOfReadings;
    }

    public void setNumberOfReadings(int numberOfReadings) {
        this.numberOfReadings = numberOfReadings;
    }

    public int getNumberOfAlerts() {
        return numberOfAlerts;
    }

    public void setNumberOfAlerts(int numberOfAlerts) {
        this.numberOfAlerts = numberOfAlerts;
    }

    public List<SensorReading> getReadingList() {
        return readingList;
    }

    public void setReadingList(List<SensorReading> readingList) {
        this.readingList = readingList;
    }
}
