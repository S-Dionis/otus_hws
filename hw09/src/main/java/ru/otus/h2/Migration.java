package ru.otus.h2;

import org.flywaydb.core.Flyway;
import javax.sql.DataSource;

public class Migration {

    public static void migrate(DataSource dataSource) {
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }

}
