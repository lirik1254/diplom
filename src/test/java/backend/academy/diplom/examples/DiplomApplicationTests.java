package backend.academy.diplom.examples;

import backend.academy.diplom.examples.TestcontainersConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Disabled
class DiplomApplicationTests {

    @Test
    void contextLoads() {
    }

}
