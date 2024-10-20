package ar.edu.um.programacion2.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DispositivosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dispositivos.class);
        Dispositivos dispositivos1 = new Dispositivos();
        dispositivos1.setId(1L);
        Dispositivos dispositivos2 = new Dispositivos();
        dispositivos2.setId(dispositivos1.getId());
        assertThat(dispositivos1).isEqualTo(dispositivos2);
        dispositivos2.setId(2L);
        assertThat(dispositivos1).isNotEqualTo(dispositivos2);
        dispositivos1.setId(null);
        assertThat(dispositivos1).isNotEqualTo(dispositivos2);
    }
}
