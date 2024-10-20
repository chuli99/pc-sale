package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalizacionesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personalizaciones.class);
        Personalizaciones personalizaciones1 = new Personalizaciones();
        personalizaciones1.setId(1L);
        Personalizaciones personalizaciones2 = new Personalizaciones();
        personalizaciones2.setId(personalizaciones1.getId());
        assertThat(personalizaciones1).isEqualTo(personalizaciones2);
        personalizaciones2.setId(2L);
        assertThat(personalizaciones1).isNotEqualTo(personalizaciones2);
        personalizaciones1.setId(null);
        assertThat(personalizaciones1).isNotEqualTo(personalizaciones2);
    }
}
