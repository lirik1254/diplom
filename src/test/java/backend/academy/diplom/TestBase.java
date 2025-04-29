package backend.academy.diplom;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext
public abstract class TestBase {
    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.enabled", () -> "false");
    }

    protected static void runMigrations(Connection conn) throws Exception {
        try (PostgresDatabase database = new PostgresDatabase()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE SCHEMA IF NOT EXISTS engineers");
            }
            database.setConnection(new JdbcConnection(conn));
            database.setLiquibaseSchemaName("engineers");
            database.setDefaultSchemaName("engineers");


            Path migrationsPath = Paths.get("src", "main", "resources", "db", "changelog")
                    .toAbsolutePath()
                    .normalize();

            Scope.child("resourceAccessor", new DirectoryResourceAccessor(migrationsPath.toFile()), () -> {
                CommandScope updateCommand = new CommandScope("update")
                        .addArgumentValue("changelogFile", "changelog-master.xml")
                        .addArgumentValue("database", database);
                updateCommand.execute();
            });
        }
    }

    @BeforeAll
    protected static void beforeAll() {
        System.out.println(postgres.getJdbcUrl());
        try (Connection conn =
                     DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            runMigrations(conn);
            System.out.println("RUnMigrationCorrect");
        } catch (Exception e) {
            throw new RuntimeException("Initial migration failed", e);
        }
    }

    @BeforeEach
    public void clearDatabase() {
        try (Connection conn =
                     DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DROP SCHEMA engineers CASCADE");
                stmt.execute("CREATE SCHEMA engineers");
                stmt.execute("DROP SCHEMA public CASCADE");
                stmt.execute("CREATE SCHEMA public");
            }

            runMigrations(conn);
        } catch (Exception e) {
            throw new RuntimeException("Database cleanup failed", e);
        }
    }
}
