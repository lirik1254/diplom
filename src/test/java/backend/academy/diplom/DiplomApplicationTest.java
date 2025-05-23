package backend.academy.diplom;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DiplomApplicationTest {
    @Test
    void mainStartsApplicationContext() {
        // Запускаем приложение в тестовом режиме
        try (ConfigurableApplicationContext ctx = SpringApplication.run(
                DiplomApplication.class,
                "--spring.main.web-application-type=none"
        )) {
            assertNotNull(ctx, "ApplicationContext should have been created");
            assertNotNull(ctx.getBean(DiplomApplication.class),
                    "DiplomApplication bean should be present");
        }
    }
}
