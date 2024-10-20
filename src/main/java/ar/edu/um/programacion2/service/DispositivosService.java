package ar.edu.um.programacion2.service;

import ar.edu.um.programacion2.domain.Dispositivos;
import ar.edu.um.programacion2.repository.DispositivosRepository;
import ar.edu.um.programacion2.service.dto.DispositivosDTO;
import ar.edu.um.programacion2.service.mapper.DispositivosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dispositivos}.
 */
@Service
@Transactional
public class DispositivosService {

    private final Logger log = LoggerFactory.getLogger(DispositivosService.class);

    private final DispositivosRepository dispositivosRepository;

    private final DispositivosMapper dispositivosMapper;

    public DispositivosService(DispositivosRepository dispositivosRepository, DispositivosMapper dispositivosMapper) {
        this.dispositivosRepository = dispositivosRepository;
        this.dispositivosMapper = dispositivosMapper;
    }

    /**
     * Save a dispositivos.
     *
     * @param dispositivosDTO the entity to save.
     * @return the persisted entity.
     */
    public DispositivosDTO save(DispositivosDTO dispositivosDTO) {
        log.debug("Request to save Dispositivos : {}", dispositivosDTO);
        Dispositivos dispositivos = dispositivosMapper.toEntity(dispositivosDTO);
        dispositivos = dispositivosRepository.save(dispositivos);
        return dispositivosMapper.toDto(dispositivos);
    }

    /**
     * Update a dispositivos.
     *
     * @param dispositivosDTO the entity to save.
     * @return the persisted entity.
     */
    public DispositivosDTO update(DispositivosDTO dispositivosDTO) {
        log.debug("Request to update Dispositivos : {}", dispositivosDTO);
        Dispositivos dispositivos = dispositivosMapper.toEntity(dispositivosDTO);
        dispositivos = dispositivosRepository.save(dispositivos);
        return dispositivosMapper.toDto(dispositivos);
    }

    /**
     * Partially update a dispositivos.
     *
     * @param dispositivosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DispositivosDTO> partialUpdate(DispositivosDTO dispositivosDTO) {
        log.debug("Request to partially update Dispositivos : {}", dispositivosDTO);

        return dispositivosRepository
            .findById(dispositivosDTO.getId())
            .map(existingDispositivos -> {
                dispositivosMapper.partialUpdate(existingDispositivos, dispositivosDTO);

                return existingDispositivos;
            })
            .map(dispositivosRepository::save)
            .map(dispositivosMapper::toDto);
    }

    /**
     * Get all the dispositivos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DispositivosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dispositivos");
        return dispositivosRepository.findAll(pageable).map(dispositivosMapper::toDto);
    }

    /**
     * Get one dispositivos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DispositivosDTO> findOne(Long id) {
        log.debug("Request to get Dispositivos : {}", id);
        return dispositivosRepository.findById(id).map(dispositivosMapper::toDto);
    }

    /**
     * Delete the dispositivos by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dispositivos : {}", id);
        dispositivosRepository.deleteById(id);
    }
}
