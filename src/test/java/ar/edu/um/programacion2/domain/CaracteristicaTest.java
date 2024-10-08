package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaracteristicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caracteristica.class);
        Caracteristica caracteristica1 = new Caracteristica();
        caracteristica1.setId(1L);
        Caracteristica caracteristica2 = new Caracteristica();
        caracteristica2.setId(caracteristica1.getId());
        assertThat(caracteristica1).isEqualTo(caracteristica2);
        caracteristica2.setId(2L);
        assertThat(caracteristica1).isNotEqualTo(caracteristica2);
        caracteristica1.setId(null);
        assertThat(caracteristica1).isNotEqualTo(caracteristica2);
    }
}
