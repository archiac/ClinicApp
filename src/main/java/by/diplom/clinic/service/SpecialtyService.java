package by.diplom.clinic.service;

import by.diplom.clinic.domain.Specialty;
import by.diplom.clinic.repository.SpecialtyRepository;
import by.diplom.clinic.service.dto.SpecialtyDTO;
import by.diplom.clinic.service.mapper.SpecialtyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Specialty}.
 */
@Service
@Transactional
public class SpecialtyService {

    private final Logger log = LoggerFactory.getLogger(SpecialtyService.class);

    private final SpecialtyRepository specialtyRepository;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyService(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
    }

    /**
     * Save a specialty.
     *
     * @param specialtyDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecialtyDTO save(SpecialtyDTO specialtyDTO) {
        log.debug("Request to save Specialty : {}", specialtyDTO);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);
        specialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(specialty);
    }

    /**
     * Get all the specialties.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SpecialtyDTO> findAll() {
        log.debug("Request to get all Specialties");
        return specialtyRepository.findAll().stream()
            .map(specialtyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one specialty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpecialtyDTO> findOne(Long id) {
        log.debug("Request to get Specialty : {}", id);
        return specialtyRepository.findById(id)
            .map(specialtyMapper::toDto);
    }

    /**
     * Delete the specialty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
    }
}
