package ar.edu.um.programacion2.web.rest;

import ar.edu.um.programacion2.repository.CaracteristicasRepository;
import ar.edu.um.programacion2.service.CaracteristicasService;
import ar.edu.um.programacion2.service.dto.CaracteristicasDTO;
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
 * REST controller for managing {@link ar.edu.um.programacion2.domain.Caracteristicas}.
 */
@RestController
@RequestMapping("/api")
public class CaracteristicasResource {

    private final Logger log = LoggerFactory.getLogger(CaracteristicasResource.class);

    private static final String ENTITY_NAME = "caracteristicas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaracteristicasService caracteristicasService;

    private final CaracteristicasRepository caracteristicasRepository;

    public CaracteristicasResource(CaracteristicasService caracteristicasService, CaracteristicasRepository caracteristicasRepository) {
        this.caracteristicasService = caracteristicasService;
        this.caracteristicasRepository = caracteristicasRepository;
    }

    /**
     * {@code POST  /caracteristicas} : Create a new caracteristicas.
     *
     * @param caracteristicasDTO the caracteristicasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caracteristicasDTO, or with status {@code 400 (Bad Request)} if the caracteristicas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caracteristicas")
    public ResponseEntity<CaracteristicasDTO> createCaracteristicas(@Valid @RequestBody CaracteristicasDTO caracteristicasDTO)
        throws URISyntaxException {
        log.debug("REST request to save Caracteristicas : {}", caracteristicasDTO);
        if (caracteristicasDTO.getId() == null) {
            throw new BadRequestAlertException("The caracteristicas must have an ID", ENTITY_NAME, "idnull");
        }
        CaracteristicasDTO result = caracteristicasService.save(caracteristicasDTO);
        return ResponseEntity
            .created(new URI("/api/caracteristicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caracteristicas/:id} : Updates an existing caracteristicas.
     *
     * @param id the id of the caracteristicasDTO to save.
     * @param caracteristicasDTO the caracteristicasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caracteristicasDTO,
     * or with status {@code 400 (Bad Request)} if the caracteristicasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caracteristicasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caracteristicas/{id}")
    public ResponseEntity<CaracteristicasDTO> updateCaracteristicas(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CaracteristicasDTO caracteristicasDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Caracteristicas : {}, {}", id, caracteristicasDTO);
        if (caracteristicasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caracteristicasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caracteristicasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CaracteristicasDTO result = caracteristicasService.update(caracteristicasDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caracteristicasDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /caracteristicas/:id} : Partial updates given fields of an existing caracteristicas, field will ignore if it is null
     *
     * @param id the id of the caracteristicasDTO to save.
     * @param caracteristicasDTO the caracteristicasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caracteristicasDTO,
     * or with status {@code 400 (Bad Request)} if the caracteristicasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the caracteristicasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the caracteristicasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/caracteristicas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CaracteristicasDTO> partialUpdateCaracteristicas(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CaracteristicasDTO caracteristicasDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Caracteristicas partially : {}, {}", id, caracteristicasDTO);
        if (caracteristicasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caracteristicasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caracteristicasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CaracteristicasDTO> result = caracteristicasService.partialUpdate(caracteristicasDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caracteristicasDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /caracteristicas} : get all the caracteristicas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caracteristicas in body.
     */
    @GetMapping("/caracteristicas")
    public ResponseEntity<List<CaracteristicasDTO>> getAllCaracteristicas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Caracteristicas");
        Page<CaracteristicasDTO> page = caracteristicasService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /caracteristicas/:id} : get the "id" caracteristicas.
     *
     * @param id the id of the caracteristicasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caracteristicasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caracteristicas/{id}")
    public ResponseEntity<CaracteristicasDTO> getCaracteristicas(@PathVariable Long id) {
        log.debug("REST request to get Caracteristicas : {}", id);
        Optional<CaracteristicasDTO> caracteristicasDTO = caracteristicasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caracteristicasDTO);
    }

    /**
     * {@code DELETE  /caracteristicas/:id} : delete the "id" caracteristicas.
     *
     * @param id the id of the caracteristicasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caracteristicas/{id}")
    public ResponseEntity<Void> deleteCaracteristicas(@PathVariable Long id) {
        log.debug("REST request to delete Caracteristicas : {}", id);
        caracteristicasService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
