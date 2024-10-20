package ar.edu.um.programacion2.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpcionesMapperTest {

    private OpcionesMapper opcionesMapper;

    @BeforeEach
    public void setUp() {
        opcionesMapper = new OpcionesMapperImpl();
    }
}
