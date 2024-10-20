package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaracteristicasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caracteristicas.class);
        Caracteristicas caracteristicas1 = new Caracteristicas();
        caracteristicas1.setId(1L);
        Caracteristicas caracteristicas2 = new Caracteristicas();
        caracteristicas2.setId(caracteristicas1.getId());
        assertThat(caracteristicas1).isEqualTo(caracteristicas2);
        caracteristicas2.setId(2L);
        assertThat(caracteristicas1).isNotEqualTo(caracteristicas2);
        caracteristicas1.setId(null);
        assertThat(caracteristicas1).isNotEqualTo(caracteristicas2);
    }
}
