package by.diplom.clinic;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("by.diplom.clinic");

        noClasses()
            .that()
                .resideInAnyPackage("by.diplom.clinic.service..")
            .or()
                .resideInAnyPackage("by.diplom.clinic.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..by.diplom.clinic.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
