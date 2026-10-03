package tn.esprit.backend;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Nécessite une base MySQL : désactivé dans le pipeline CI")
@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
