package backend.academy.diplom.examples;

import backend.academy.diplom.DiplomApplication;
import backend.academy.diplom.examples.TestcontainersConfiguration;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.SpringApplication;

@Disabled
public class TestDiplomApplication {

    public static void main(String[] args) {
        SpringApplication.from(DiplomApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
