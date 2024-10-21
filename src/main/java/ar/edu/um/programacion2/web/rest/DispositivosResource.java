package ar.edu.um.programacion2.web.rest;

import ar.edu.um.programacion2.repository.DispositivosRepository;
import ar.edu.um.programacion2.service.DispositivosService;
import ar.edu.um.programacion2.service.dto.DispositivosDTO;
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
 * REST controller for managing {@link ar.edu.um.programacion2.domain.Dispositivos}.
 */
@RestController
@RequestMapping("/api")
public class DispositivosResource {

    private final Logger log = LoggerFactory.getLogger(DispositivosResource.class);

    private static final String ENTITY_NAME = "dispositivos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DispositivosService dispositivosService;

    private final DispositivosRepository dispositivosRepository;

    public DispositivosResource(DispositivosService dispositivosService, DispositivosRepository dispositivosRepository) {
        this.dispositivosService = dispositivosService;
        this.dispositivosRepository = dispositivosRepository;
    }

    /**
     * {@code POST  /dispositivos} : Create a new dispositivos.
     *
     * @param dispositivosDTO the dispositivosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dispositivosDTO, or with status {@code 400 (Bad Request)} if the dispositivos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dispositivos")
    public ResponseEntity<DispositivosDTO> createDispositivos(@Valid @RequestBody DispositivosDTO dispositivosDTO)
        throws URISyntaxException {
        log.debug("REST request to save Dispositivos : {}", dispositivosDTO);
        if (dispositivosDTO.getId() == null) {
            throw new BadRequestAlertException("The dispositivos must have an ID", ENTITY_NAME, "idnull");
        }
        DispositivosDTO result = dispositivosService.save(dispositivosDTO);
        return ResponseEntity
            .created(new URI("/api/dispositivos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dispositivos/:id} : Updates an existing dispositivos.
     *
     * @param id the id of the dispositivosDTO to save.
     * @param dispositivosDTO the dispositivosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispositivosDTO,
     * or with status {@code 400 (Bad Request)} if the dispositivosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dispositivosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dispositivos/{id}")
    public ResponseEntity<DispositivosDTO> updateDispositivos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DispositivosDTO dispositivosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Dispositivos : {}, {}", id, dispositivosDTO);
        if (dispositivosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispositivosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispositivosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DispositivosDTO result = dispositivosService.update(dispositivosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispositivosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dispositivos/:id} : Partial updates given fields of an existing dispositivos, field will ignore if it is null
     *
     * @param id the id of the dispositivosDTO to save.
     * @param dispositivosDTO the dispositivosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispositivosDTO,
     * or with status {@code 400 (Bad Request)} if the dispositivosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dispositivosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dispositivosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dispositivos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DispositivosDTO> partialUpdateDispositivos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DispositivosDTO dispositivosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dispositivos partially : {}, {}", id, dispositivosDTO);
        if (dispositivosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispositivosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispositivosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DispositivosDTO> result = dispositivosService.partialUpdate(dispositivosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispositivosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dispositivos} : get all the dispositivos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dispositivos in body.
     */
    @GetMapping("/dispositivos")
    public ResponseEntity<List<DispositivosDTO>> getAllDispositivos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Dispositivos");
        Page<DispositivosDTO> page = dispositivosService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dispositivos/:id} : get the "id" dispositivos.
     *
     * @param id the id of the dispositivosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dispositivosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dispositivos/{id}")
    public ResponseEntity<DispositivosDTO> getDispositivos(@PathVariable Long id) {
        log.debug("REST request to get Dispositivos : {}", id);
        Optional<DispositivosDTO> dispositivosDTO = dispositivosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dispositivosDTO);
    }

    /**
     * {@code DELETE  /dispositivos/:id} : delete the "id" dispositivos.
     *
     * @param id the id of the dispositivosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dispositivos/{id}")
    public ResponseEntity<Void> deleteDispositivos(@PathVariable Long id) {
        log.debug("REST request to delete Dispositivos : {}", id);
        dispositivosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
