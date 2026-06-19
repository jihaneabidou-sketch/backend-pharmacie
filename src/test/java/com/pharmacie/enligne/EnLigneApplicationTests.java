package com.pharmacie.enligne;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EnLigneApplicationTests {

	@Test
public void testMainApplication() {
    // Cela force l'exécution de la méthode main pour JaCoCo
    EnLigneApplication.main(new String[] {});
}
	@Test
	void contextLoads() {
	}

}
