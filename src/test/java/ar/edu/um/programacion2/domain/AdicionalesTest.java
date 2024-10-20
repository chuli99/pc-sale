package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdicionalesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adicionales.class);
        Adicionales adicionales1 = new Adicionales();
        adicionales1.setId(1L);
        Adicionales adicionales2 = new Adicionales();
        adicionales2.setId(adicionales1.getId());
        assertThat(adicionales1).isEqualTo(adicionales2);
        adicionales2.setId(2L);
        assertThat(adicionales1).isNotEqualTo(adicionales2);
        adicionales1.setId(null);
        assertThat(adicionales1).isNotEqualTo(adicionales2);
    }
}
