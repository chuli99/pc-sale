package ar.edu.um.programacion2.service;

import ar.edu.um.programacion2.domain.Adicionales;
import ar.edu.um.programacion2.repository.AdicionalesRepository;
import ar.edu.um.programacion2.service.dto.AdicionalesDTO;
import ar.edu.um.programacion2.service.mapper.AdicionalesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Adicionales}.
 */
@Service
@Transactional
public class AdicionalesService {

    private final Logger log = LoggerFactory.getLogger(AdicionalesService.class);

    private final AdicionalesRepository adicionalesRepository;

    private final AdicionalesMapper adicionalesMapper;

    public AdicionalesService(AdicionalesRepository adicionalesRepository, AdicionalesMapper adicionalesMapper) {
        this.adicionalesRepository = adicionalesRepository;
        this.adicionalesMapper = adicionalesMapper;
    }

    /**
     * Save a adicionales.
     *
     * @param adicionalesDTO the entity to save.
     * @return the persisted entity.
     */
    public AdicionalesDTO save(AdicionalesDTO adicionalesDTO) {
        log.debug("Request to save Adicionales : {}", adicionalesDTO);
        Adicionales adicionales = adicionalesMapper.toEntity(adicionalesDTO);
        adicionales = adicionalesRepository.save(adicionales);
        return adicionalesMapper.toDto(adicionales);
    }

    /**
     * Update a adicionales.
     *
     * @param adicionalesDTO the entity to save.
     * @return the persisted entity.
     */
    public AdicionalesDTO update(AdicionalesDTO adicionalesDTO) {
        log.debug("Request to update Adicionales : {}", adicionalesDTO);
        Adicionales adicionales = adicionalesMapper.toEntity(adicionalesDTO);
        adicionales = adicionalesRepository.save(adicionales);
        return adicionalesMapper.toDto(adicionales);
    }

    /**
     * Partially update a adicionales.
     *
     * @param adicionalesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdicionalesDTO> partialUpdate(AdicionalesDTO adicionalesDTO) {
        log.debug("Request to partially update Adicionales : {}", adicionalesDTO);

        return adicionalesRepository
            .findById(adicionalesDTO.getId())
            .map(existingAdicionales -> {
                adicionalesMapper.partialUpdate(existingAdicionales, adicionalesDTO);

                return existingAdicionales;
            })
            .map(adicionalesRepository::save)
            .map(adicionalesMapper::toDto);
    }

    /**
     * Get all the adicionales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdicionalesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Adicionales");
        return adicionalesRepository.findAll(pageable).map(adicionalesMapper::toDto);
    }

    /**
     * Get one adicionales by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdicionalesDTO> findOne(Long id) {
        log.debug("Request to get Adicionales : {}", id);
        return adicionalesRepository.findById(id).map(adicionalesMapper::toDto);
    }

    /**
     * Delete the adicionales by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Adicionales : {}", id);
        adicionalesRepository.deleteById(id);
    }
}
