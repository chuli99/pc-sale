package ar.edu.um.programacion2.service;

import ar.edu.um.programacion2.domain.Personalizaciones;
import ar.edu.um.programacion2.repository.PersonalizacionesRepository;
import ar.edu.um.programacion2.service.dto.PersonalizacionesDTO;
import ar.edu.um.programacion2.service.mapper.PersonalizacionesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Personalizaciones}.
 */
@Service
@Transactional
public class PersonalizacionesService {

    private final Logger log = LoggerFactory.getLogger(PersonalizacionesService.class);

    private final PersonalizacionesRepository personalizacionesRepository;

    private final PersonalizacionesMapper personalizacionesMapper;

    public PersonalizacionesService(
        PersonalizacionesRepository personalizacionesRepository,
        PersonalizacionesMapper personalizacionesMapper
    ) {
        this.personalizacionesRepository = personalizacionesRepository;
        this.personalizacionesMapper = personalizacionesMapper;
    }

    /**
     * Save a personalizaciones.
     *
     * @param personalizacionesDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonalizacionesDTO save(PersonalizacionesDTO personalizacionesDTO) {
        log.debug("Request to save Personalizaciones : {}", personalizacionesDTO);
        Personalizaciones personalizaciones = personalizacionesMapper.toEntity(personalizacionesDTO);
        personalizaciones = personalizacionesRepository.save(personalizaciones);
        return personalizacionesMapper.toDto(personalizaciones);
    }

    /**
     * Update a personalizaciones.
     *
     * @param personalizacionesDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonalizacionesDTO update(PersonalizacionesDTO personalizacionesDTO) {
        log.debug("Request to update Personalizaciones : {}", personalizacionesDTO);
        Personalizaciones personalizaciones = personalizacionesMapper.toEntity(personalizacionesDTO);
        personalizaciones = personalizacionesRepository.save(personalizaciones);
        return personalizacionesMapper.toDto(personalizaciones);
    }

    /**
     * Partially update a personalizaciones.
     *
     * @param personalizacionesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonalizacionesDTO> partialUpdate(PersonalizacionesDTO personalizacionesDTO) {
        log.debug("Request to partially update Personalizaciones : {}", personalizacionesDTO);

        return personalizacionesRepository
            .findById(personalizacionesDTO.getId())
            .map(existingPersonalizaciones -> {
                personalizacionesMapper.partialUpdate(existingPersonalizaciones, personalizacionesDTO);

                return existingPersonalizaciones;
            })
            .map(personalizacionesRepository::save)
            .map(personalizacionesMapper::toDto);
    }

    /**
     * Get all the personalizaciones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonalizacionesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Personalizaciones");
        return personalizacionesRepository.findAll(pageable).map(personalizacionesMapper::toDto);
    }

    /**
     * Get one personalizaciones by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonalizacionesDTO> findOne(Long id) {
        log.debug("Request to get Personalizaciones : {}", id);
        return personalizacionesRepository.findById(id).map(personalizacionesMapper::toDto);
    }

    /**
     * Delete the personalizaciones by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Personalizaciones : {}", id);
        personalizacionesRepository.deleteById(id);
    }
}
