package ar.edu.um.programacion2.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdicionalesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdicionalesDTO.class);
        AdicionalesDTO adicionalesDTO1 = new AdicionalesDTO();
        adicionalesDTO1.setId(1L);
        AdicionalesDTO adicionalesDTO2 = new AdicionalesDTO();
        assertThat(adicionalesDTO1).isNotEqualTo(adicionalesDTO2);
        adicionalesDTO2.setId(adicionalesDTO1.getId());
        assertThat(adicionalesDTO1).isEqualTo(adicionalesDTO2);
        adicionalesDTO2.setId(2L);
        assertThat(adicionalesDTO1).isNotEqualTo(adicionalesDTO2);
        adicionalesDTO1.setId(null);
        assertThat(adicionalesDTO1).isNotEqualTo(adicionalesDTO2);
    }
}
