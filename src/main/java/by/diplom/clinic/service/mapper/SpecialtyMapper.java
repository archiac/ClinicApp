package by.diplom.clinic.service.mapper;


import by.diplom.clinic.domain.*;
import by.diplom.clinic.service.dto.SpecialtyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialty} and its DTO {@link SpecialtyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {



    default Specialty fromId(Long id) {
        if (id == null) {
            return null;
        }
        Specialty specialty = new Specialty();
        specialty.setId(id);
        return specialty;
    }
}
