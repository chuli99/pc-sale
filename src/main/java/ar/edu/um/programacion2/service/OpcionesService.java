package ar.edu.um.programacion2.service;

import ar.edu.um.programacion2.domain.Opciones;
import ar.edu.um.programacion2.repository.OpcionesRepository;
import ar.edu.um.programacion2.service.dto.OpcionesDTO;
import ar.edu.um.programacion2.service.mapper.OpcionesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Opciones}.
 */
@Service
@Transactional
public class OpcionesService {

    private final Logger log = LoggerFactory.getLogger(OpcionesService.class);

    private final OpcionesRepository opcionesRepository;

    private final OpcionesMapper opcionesMapper;

    public OpcionesService(OpcionesRepository opcionesRepository, OpcionesMapper opcionesMapper) {
        this.opcionesRepository = opcionesRepository;
        this.opcionesMapper = opcionesMapper;
    }

    /**
     * Save a opciones.
     *
     * @param opcionesDTO the entity to save.
     * @return the persisted entity.
     */
    public OpcionesDTO save(OpcionesDTO opcionesDTO) {
        log.debug("Request to save Opciones : {}", opcionesDTO);
        Opciones opciones = opcionesMapper.toEntity(opcionesDTO);
        opciones = opcionesRepository.save(opciones);
        return opcionesMapper.toDto(opciones);
    }

    /**
     * Update a opciones.
     *
     * @param opcionesDTO the entity to save.
     * @return the persisted entity.
     */
    public OpcionesDTO update(OpcionesDTO opcionesDTO) {
        log.debug("Request to update Opciones : {}", opcionesDTO);
        Opciones opciones = opcionesMapper.toEntity(opcionesDTO);
        opciones = opcionesRepository.save(opciones);
        return opcionesMapper.toDto(opciones);
    }

    /**
     * Partially update a opciones.
     *
     * @param opcionesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OpcionesDTO> partialUpdate(OpcionesDTO opcionesDTO) {
        log.debug("Request to partially update Opciones : {}", opcionesDTO);

        return opcionesRepository
            .findById(opcionesDTO.getId())
            .map(existingOpciones -> {
                opcionesMapper.partialUpdate(existingOpciones, opcionesDTO);

                return existingOpciones;
            })
            .map(opcionesRepository::save)
            .map(opcionesMapper::toDto);
    }

    /**
     * Get all the opciones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OpcionesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Opciones");
        return opcionesRepository.findAll(pageable).map(opcionesMapper::toDto);
    }

    /**
     * Get one opciones by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OpcionesDTO> findOne(Long id) {
        log.debug("Request to get Opciones : {}", id);
        return opcionesRepository.findById(id).map(opcionesMapper::toDto);
    }

    /**
     * Delete the opciones by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Opciones : {}", id);
        opcionesRepository.deleteById(id);
    }
}
