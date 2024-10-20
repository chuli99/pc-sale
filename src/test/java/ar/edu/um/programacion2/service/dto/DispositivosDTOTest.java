package ar.edu.um.programacion2.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DispositivosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DispositivosDTO.class);
        DispositivosDTO dispositivosDTO1 = new DispositivosDTO();
        dispositivosDTO1.setId(1L);
        DispositivosDTO dispositivosDTO2 = new DispositivosDTO();
        assertThat(dispositivosDTO1).isNotEqualTo(dispositivosDTO2);
        dispositivosDTO2.setId(dispositivosDTO1.getId());
        assertThat(dispositivosDTO1).isEqualTo(dispositivosDTO2);
        dispositivosDTO2.setId(2L);
        assertThat(dispositivosDTO1).isNotEqualTo(dispositivosDTO2);
        dispositivosDTO1.setId(null);
        assertThat(dispositivosDTO1).isNotEqualTo(dispositivosDTO2);
    }
}
