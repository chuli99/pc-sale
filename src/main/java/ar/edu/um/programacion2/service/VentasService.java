package ar.edu.um.programacion2.service;

import ar.edu.um.programacion2.domain.Ventas;
import ar.edu.um.programacion2.repository.VentasRepository;
import ar.edu.um.programacion2.service.dto.VentasDTO;
import ar.edu.um.programacion2.service.mapper.VentasMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ventas}.
 */
@Service
@Transactional
public class VentasService {

    private final Logger log = LoggerFactory.getLogger(VentasService.class);

    private final VentasRepository ventasRepository;

    private final VentasMapper ventasMapper;

    public VentasService(VentasRepository ventasRepository, VentasMapper ventasMapper) {
        this.ventasRepository = ventasRepository;
        this.ventasMapper = ventasMapper;
    }

    /**
     * Save a ventas.
     *
     * @param ventasDTO the entity to save.
     * @return the persisted entity.
     */
    public VentasDTO save(VentasDTO ventasDTO) {
        log.debug("Request to save Ventas : {}", ventasDTO);
        Ventas ventas = ventasMapper.toEntity(ventasDTO);
        ventas = ventasRepository.save(ventas);
        return ventasMapper.toDto(ventas);
    }

    /**
     * Update a ventas.
     *
     * @param ventasDTO the entity to save.
     * @return the persisted entity.
     */
    public VentasDTO update(VentasDTO ventasDTO) {
        log.debug("Request to update Ventas : {}", ventasDTO);
        Ventas ventas = ventasMapper.toEntity(ventasDTO);
        ventas = ventasRepository.save(ventas);
        return ventasMapper.toDto(ventas);
    }

    /**
     * Partially update a ventas.
     *
     * @param ventasDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VentasDTO> partialUpdate(VentasDTO ventasDTO) {
        log.debug("Request to partially update Ventas : {}", ventasDTO);

        return ventasRepository
            .findById(ventasDTO.getId())
            .map(existingVentas -> {
                ventasMapper.partialUpdate(existingVentas, ventasDTO);

                return existingVentas;
            })
            .map(ventasRepository::save)
            .map(ventasMapper::toDto);
    }

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VentasDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ventas");
        return ventasRepository.findAll(pageable).map(ventasMapper::toDto);
    }

    /**
     * Get one ventas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VentasDTO> findOne(Long id) {
        log.debug("Request to get Ventas : {}", id);
        return ventasRepository.findById(id).map(ventasMapper::toDto);
    }

    /**
     * Delete the ventas by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ventas : {}", id);
        ventasRepository.deleteById(id);
    }
}
