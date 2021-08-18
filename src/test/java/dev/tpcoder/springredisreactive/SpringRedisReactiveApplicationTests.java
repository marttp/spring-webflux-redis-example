package dev.tpcoder.springredisreactive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringRedisReactiveApplicationTests {

	@Test
	void contextLoads() {
		SpringRedisReactiveApplication.main(new String[] {});
		Assertions.assertTrue(true);
	}

}
