package es.amplia.springamqp;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest(value = "spring.profiles.active=test")
@SpringApplicationConfiguration(classes = SpringAmqpApplication.class)
public class AbstractSpringBootTest {
}
