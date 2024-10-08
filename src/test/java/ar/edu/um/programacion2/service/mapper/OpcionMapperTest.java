package ar.edu.um.programacion2.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpcionMapperTest {

    private OpcionMapper opcionMapper;

    @BeforeEach
    public void setUp() {
        opcionMapper = new OpcionMapperImpl();
    }
}
