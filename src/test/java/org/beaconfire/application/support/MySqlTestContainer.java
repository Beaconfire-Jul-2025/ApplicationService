package org.beaconfire.application.support;


import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MySqlTestContainer {

    @Container
    public static final MySQLContainer<?> MYSQL =
            new MySQLContainer<>("mysql:lts")
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(false)
                    .withStartupTimeoutSeconds(300)
                    .withConnectTimeoutSeconds(300)
                    .withCommand("--default-authentication-plugin=mysql_native_password");
}