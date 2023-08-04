package com.hunt.example.test;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class Cfg {
    public static ProcessEngineConfiguration getProEngCfg() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:postgresql://localhost:5432/my_flowable_test")
                .setJdbcUsername("postgres")
                .setJdbcPassword("123456")
                .setJdbcDriver("org.postgresql.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return cfg;
    }

    public static ProcessEngine getProcessEngine() {
        return getProEngCfg().buildProcessEngine();
    }
}
