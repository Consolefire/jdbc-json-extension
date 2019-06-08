package com.cf.jdbc.json.ext.api;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JdbcJsonExtensionApplication.class})
public class JdbcJsonExtensionApplicationTest {

    @Test
    void test() {
        Assertions.assertTrue(true);
        log.info("Context loaded ...");
    }
}
