package ar.edu.um.programacion2.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpcionesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpcionesDTO.class);
        OpcionesDTO opcionesDTO1 = new OpcionesDTO();
        opcionesDTO1.setId(1L);
        OpcionesDTO opcionesDTO2 = new OpcionesDTO();
        assertThat(opcionesDTO1).isNotEqualTo(opcionesDTO2);
        opcionesDTO2.setId(opcionesDTO1.getId());
        assertThat(opcionesDTO1).isEqualTo(opcionesDTO2);
        opcionesDTO2.setId(2L);
        assertThat(opcionesDTO1).isNotEqualTo(opcionesDTO2);
        opcionesDTO1.setId(null);
        assertThat(opcionesDTO1).isNotEqualTo(opcionesDTO2);
    }
}
