package com.dropwizard.demo.configure;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.Configuration;

public class MainConfiguration extends Configuration
{
    @Valid
    @NotNull
    private DataSourceFactory db;
    
    public MainConfiguration() {
        this.db = new DataSourceFactory();
    }
    
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return this.db;
    }
    
    @JsonProperty("database")
    public void setDataSourceFactory(final DataSourceFactory db) {
        this.db = db;
    }
}