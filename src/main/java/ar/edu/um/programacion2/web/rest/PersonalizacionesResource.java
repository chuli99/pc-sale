package ar.edu.um.programacion2.web.rest;

import ar.edu.um.programacion2.repository.PersonalizacionesRepository;
import ar.edu.um.programacion2.service.PersonalizacionesService;
import ar.edu.um.programacion2.service.dto.PersonalizacionesDTO;
import ar.edu.um.programacion2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.programacion2.domain.Personalizaciones}.
 */
@RestController
@RequestMapping("/api")
public class PersonalizacionesResource {

    private final Logger log = LoggerFactory.getLogger(PersonalizacionesResource.class);

    private static final String ENTITY_NAME = "personalizaciones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalizacionesService personalizacionesService;

    private final PersonalizacionesRepository personalizacionesRepository;

    public PersonalizacionesResource(
        PersonalizacionesService personalizacionesService,
        PersonalizacionesRepository personalizacionesRepository
    ) {
        this.personalizacionesService = personalizacionesService;
        this.personalizacionesRepository = personalizacionesRepository;
    }

    /**
     * {@code POST  /personalizaciones} : Create a new personalizaciones.
     *
     * @param personalizacionesDTO the personalizacionesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalizacionesDTO, or with status {@code 400 (Bad Request)} if the personalizaciones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personalizaciones")
    public ResponseEntity<PersonalizacionesDTO> createPersonalizaciones(@Valid @RequestBody PersonalizacionesDTO personalizacionesDTO)
        throws URISyntaxException {
        log.debug("REST request to save Personalizaciones : {}", personalizacionesDTO);
        if (personalizacionesDTO.getId() != null) {
            throw new BadRequestAlertException("A new personalizaciones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalizacionesDTO result = personalizacionesService.save(personalizacionesDTO);
        return ResponseEntity
            .created(new URI("/api/personalizaciones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personalizaciones/:id} : Updates an existing personalizaciones.
     *
     * @param id the id of the personalizacionesDTO to save.
     * @param personalizacionesDTO the personalizacionesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalizacionesDTO,
     * or with status {@code 400 (Bad Request)} if the personalizacionesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalizacionesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personalizaciones/{id}")
    public ResponseEntity<PersonalizacionesDTO> updatePersonalizaciones(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonalizacionesDTO personalizacionesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Personalizaciones : {}, {}", id, personalizacionesDTO);
        if (personalizacionesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalizacionesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalizacionesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonalizacionesDTO result = personalizacionesService.update(personalizacionesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalizacionesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personalizaciones/:id} : Partial updates given fields of an existing personalizaciones, field will ignore if it is null
     *
     * @param id the id of the personalizacionesDTO to save.
     * @param personalizacionesDTO the personalizacionesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalizacionesDTO,
     * or with status {@code 400 (Bad Request)} if the personalizacionesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personalizacionesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalizacionesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personalizaciones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalizacionesDTO> partialUpdatePersonalizaciones(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonalizacionesDTO personalizacionesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Personalizaciones partially : {}, {}", id, personalizacionesDTO);
        if (personalizacionesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalizacionesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalizacionesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalizacionesDTO> result = personalizacionesService.partialUpdate(personalizacionesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalizacionesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /personalizaciones} : get all the personalizaciones.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalizaciones in body.
     */
    @GetMapping("/personalizaciones")
    public ResponseEntity<List<PersonalizacionesDTO>> getAllPersonalizaciones(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Personalizaciones");
        Page<PersonalizacionesDTO> page = personalizacionesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personalizaciones/:id} : get the "id" personalizaciones.
     *
     * @param id the id of the personalizacionesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalizacionesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personalizaciones/{id}")
    public ResponseEntity<PersonalizacionesDTO> getPersonalizaciones(@PathVariable Long id) {
        log.debug("REST request to get Personalizaciones : {}", id);
        Optional<PersonalizacionesDTO> personalizacionesDTO = personalizacionesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalizacionesDTO);
    }

    /**
     * {@code DELETE  /personalizaciones/:id} : delete the "id" personalizaciones.
     *
     * @param id the id of the personalizacionesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personalizaciones/{id}")
    public ResponseEntity<Void> deletePersonalizaciones(@PathVariable Long id) {
        log.debug("REST request to delete Personalizaciones : {}", id);
        personalizacionesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
