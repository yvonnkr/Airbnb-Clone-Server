package com.yvolabs.airbnbclone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = {AirbnbCloneServerApplication.class, TestSecurityConfiguration.class})
class AirbnbCloneServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
