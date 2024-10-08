package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpcionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opcion.class);
        Opcion opcion1 = new Opcion();
        opcion1.setId(1L);
        Opcion opcion2 = new Opcion();
        opcion2.setId(opcion1.getId());
        assertThat(opcion1).isEqualTo(opcion2);
        opcion2.setId(2L);
        assertThat(opcion1).isNotEqualTo(opcion2);
        opcion1.setId(null);
        assertThat(opcion1).isNotEqualTo(opcion2);
    }
}
