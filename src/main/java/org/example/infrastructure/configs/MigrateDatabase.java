package org.example.infrastructure.configs;

import org.example.infrastructure.migration.MigrationTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MigrateDatabase {
    @Autowired
    public MigrateDatabase(MigrationTool migrationTool) {
        migrationTool.runMigrate();
    }
}
