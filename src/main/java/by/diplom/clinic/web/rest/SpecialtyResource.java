package by.diplom.clinic.web.rest;

import by.diplom.clinic.service.SpecialtyService;
import by.diplom.clinic.web.rest.errors.BadRequestAlertException;
import by.diplom.clinic.service.dto.SpecialtyDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link by.diplom.clinic.domain.Specialty}.
 */
@RestController
@RequestMapping("/api")
public class SpecialtyResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);

    private final SpecialtyService specialtyService;

    public SpecialtyResource(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    /**
     * {@code GET  /specialties} : get all the specialties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialties in body.
     */
    @GetMapping("/specialties")
    public List<SpecialtyDTO> getAllSpecialties() {
        log.debug("REST request to get all Specialties");
        return specialtyService.findAll();
    }

    /**
     * {@code GET  /specialties/:id} : get the "id" specialty.
     *
     * @param id the id of the specialtyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialtyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specialties/{id}")
    public ResponseEntity<SpecialtyDTO> getSpecialty(@PathVariable Long id) {
        log.debug("REST request to get Specialty : {}", id);
        Optional<SpecialtyDTO> specialtyDTO = specialtyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialtyDTO);
    }
}
