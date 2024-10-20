package ar.edu.um.programacion2.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalizacionesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalizacionesDTO.class);
        PersonalizacionesDTO personalizacionesDTO1 = new PersonalizacionesDTO();
        personalizacionesDTO1.setId(1L);
        PersonalizacionesDTO personalizacionesDTO2 = new PersonalizacionesDTO();
        assertThat(personalizacionesDTO1).isNotEqualTo(personalizacionesDTO2);
        personalizacionesDTO2.setId(personalizacionesDTO1.getId());
        assertThat(personalizacionesDTO1).isEqualTo(personalizacionesDTO2);
        personalizacionesDTO2.setId(2L);
        assertThat(personalizacionesDTO1).isNotEqualTo(personalizacionesDTO2);
        personalizacionesDTO1.setId(null);
        assertThat(personalizacionesDTO1).isNotEqualTo(personalizacionesDTO2);
    }
}
