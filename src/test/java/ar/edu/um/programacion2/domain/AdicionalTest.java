package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdicionalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adicional.class);
        Adicional adicional1 = new Adicional();
        adicional1.setId(1L);
        Adicional adicional2 = new Adicional();
        adicional2.setId(adicional1.getId());
        assertThat(adicional1).isEqualTo(adicional2);
        adicional2.setId(2L);
        assertThat(adicional1).isNotEqualTo(adicional2);
        adicional1.setId(null);
        assertThat(adicional1).isNotEqualTo(adicional2);
    }
}
