package ar.edu.um.programacion2.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaracteristicasDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaracteristicasDTO.class);
        CaracteristicasDTO caracteristicasDTO1 = new CaracteristicasDTO();
        caracteristicasDTO1.setId(1L);
        CaracteristicasDTO caracteristicasDTO2 = new CaracteristicasDTO();
        assertThat(caracteristicasDTO1).isNotEqualTo(caracteristicasDTO2);
        caracteristicasDTO2.setId(caracteristicasDTO1.getId());
        assertThat(caracteristicasDTO1).isEqualTo(caracteristicasDTO2);
        caracteristicasDTO2.setId(2L);
        assertThat(caracteristicasDTO1).isNotEqualTo(caracteristicasDTO2);
        caracteristicasDTO1.setId(null);
        assertThat(caracteristicasDTO1).isNotEqualTo(caracteristicasDTO2);
    }
}
