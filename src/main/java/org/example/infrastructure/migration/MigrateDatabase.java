package org.example.infrastructure.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MigrateDatabase {
    @Autowired
    public MigrateDatabase(MigrationTool migrationTool) {
        migrationTool.runMigrate();
    }
}
