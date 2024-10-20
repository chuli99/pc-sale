package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpcionesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opciones.class);
        Opciones opciones1 = new Opciones();
        opciones1.setId(1L);
        Opciones opciones2 = new Opciones();
        opciones2.setId(opciones1.getId());
        assertThat(opciones1).isEqualTo(opciones2);
        opciones2.setId(2L);
        assertThat(opciones1).isNotEqualTo(opciones2);
        opciones1.setId(null);
        assertThat(opciones1).isNotEqualTo(opciones2);
    }
}
