package ar.edu.um.programacion2.service;

import ar.edu.um.programacion2.domain.Caracteristicas;
import ar.edu.um.programacion2.repository.CaracteristicasRepository;
import ar.edu.um.programacion2.service.dto.CaracteristicasDTO;
import ar.edu.um.programacion2.service.mapper.CaracteristicasMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Caracteristicas}.
 */
@Service
@Transactional
public class CaracteristicasService {

    private final Logger log = LoggerFactory.getLogger(CaracteristicasService.class);

    private final CaracteristicasRepository caracteristicasRepository;

    private final CaracteristicasMapper caracteristicasMapper;

    public CaracteristicasService(CaracteristicasRepository caracteristicasRepository, CaracteristicasMapper caracteristicasMapper) {
        this.caracteristicasRepository = caracteristicasRepository;
        this.caracteristicasMapper = caracteristicasMapper;
    }

    /**
     * Save a caracteristicas.
     *
     * @param caracteristicasDTO the entity to save.
     * @return the persisted entity.
     */
    public CaracteristicasDTO save(CaracteristicasDTO caracteristicasDTO) {
        log.debug("Request to save Caracteristicas : {}", caracteristicasDTO);
        Caracteristicas caracteristicas = caracteristicasMapper.toEntity(caracteristicasDTO);
        caracteristicas = caracteristicasRepository.save(caracteristicas);
        return caracteristicasMapper.toDto(caracteristicas);
    }

    /**
     * Update a caracteristicas.
     *
     * @param caracteristicasDTO the entity to save.
     * @return the persisted entity.
     */
    public CaracteristicasDTO update(CaracteristicasDTO caracteristicasDTO) {
        log.debug("Request to update Caracteristicas : {}", caracteristicasDTO);
        Caracteristicas caracteristicas = caracteristicasMapper.toEntity(caracteristicasDTO);
        caracteristicas = caracteristicasRepository.save(caracteristicas);
        return caracteristicasMapper.toDto(caracteristicas);
    }

    /**
     * Partially update a caracteristicas.
     *
     * @param caracteristicasDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CaracteristicasDTO> partialUpdate(CaracteristicasDTO caracteristicasDTO) {
        log.debug("Request to partially update Caracteristicas : {}", caracteristicasDTO);

        return caracteristicasRepository
            .findById(caracteristicasDTO.getId())
            .map(existingCaracteristicas -> {
                caracteristicasMapper.partialUpdate(existingCaracteristicas, caracteristicasDTO);

                return existingCaracteristicas;
            })
            .map(caracteristicasRepository::save)
            .map(caracteristicasMapper::toDto);
    }

    /**
     * Get all the caracteristicas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CaracteristicasDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Caracteristicas");
        return caracteristicasRepository.findAll(pageable).map(caracteristicasMapper::toDto);
    }

    /**
     * Get one caracteristicas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CaracteristicasDTO> findOne(Long id) {
        log.debug("Request to get Caracteristicas : {}", id);
        return caracteristicasRepository.findById(id).map(caracteristicasMapper::toDto);
    }

    /**
     * Delete the caracteristicas by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Caracteristicas : {}", id);
        caracteristicasRepository.deleteById(id);
    }
}
