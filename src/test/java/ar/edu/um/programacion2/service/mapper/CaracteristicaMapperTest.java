package ar.edu.um.programacion2.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaracteristicaMapperTest {

    private CaracteristicaMapper caracteristicaMapper;

    @BeforeEach
    public void setUp() {
        caracteristicaMapper = new CaracteristicaMapperImpl();
    }
}
