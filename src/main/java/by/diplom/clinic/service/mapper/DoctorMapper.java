package by.diplom.clinic.service.mapper;


import by.diplom.clinic.domain.*;
import by.diplom.clinic.service.dto.DoctorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Doctor} and its DTO {@link DoctorDTO}.
 */
@Mapper(componentModel = "spring", uses = {SpecialtyMapper.class})
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {

    @Mapping(source = "specialty.id", target = "specialtyId")
    @Mapping(source = "specialty.name", target = "specialtyName")
    DoctorDTO toDto(Doctor doctor);

    @Mapping(source = "specialtyId", target = "specialty")
    Doctor toEntity(DoctorDTO doctorDTO);

    default Doctor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }
}
