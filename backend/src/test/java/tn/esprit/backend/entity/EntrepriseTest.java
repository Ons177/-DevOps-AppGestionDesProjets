package tn.esprit.backend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class EntrepriseTest {

    @Test
    void testSettersEtGetters() {
        Entreprise entreprise = new Entreprise();
        entreprise.setId(1L);
        entreprise.setNom("ESPRIT");
        entreprise.setAdresse("Tunis");

        assertEquals(1L, entreprise.getId());
        assertEquals("ESPRIT", entreprise.getNom());
        assertEquals("Tunis", entreprise.getAdresse());
    }

    @Test
    void testBuilder() {
        Entreprise entreprise = Entreprise.builder()
                .nom("Inetum")
                .adresse("Lac 2")
                .build();

        assertEquals("Inetum", entreprise.getNom());
        assertEquals("Lac 2", entreprise.getAdresse());
        assertNull(entreprise.getId());
    }

    @Test
    void testConstructeurComplet() {
        Entreprise entreprise = new Entreprise(2L, "Ericsson", "Tunis", null);

        assertEquals(2L, entreprise.getId());
        assertEquals("Ericsson", entreprise.getNom());
        assertEquals("Tunis", entreprise.getAdresse());
    }
}
