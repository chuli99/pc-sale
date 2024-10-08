package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalizacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personalizacion.class);
        Personalizacion personalizacion1 = new Personalizacion();
        personalizacion1.setId(1L);
        Personalizacion personalizacion2 = new Personalizacion();
        personalizacion2.setId(personalizacion1.getId());
        assertThat(personalizacion1).isEqualTo(personalizacion2);
        personalizacion2.setId(2L);
        assertThat(personalizacion1).isNotEqualTo(personalizacion2);
        personalizacion1.setId(null);
        assertThat(personalizacion1).isNotEqualTo(personalizacion2);
    }
}
