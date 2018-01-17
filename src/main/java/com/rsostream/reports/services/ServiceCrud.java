package com.rsostream.reports.services;

import com.rsostream.reports.properties.PropertiesCRUD;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class ServiceCrud {
    @Inject
    PropertiesCRUD crud;

    // TODO: write methods to obtain all readings in a time interval
}
