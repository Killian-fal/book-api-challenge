package fr.killiandev.book;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModularityTest {

    @Test
    void shouldBeCompliant() {
        ApplicationModules.of(ApiApplication.class).verify();
    }

    @Test
    void createApplicationModuleModel() {
        ApplicationModules modules = ApplicationModules.of(ApiApplication.class);
        modules.forEach(System.out::println);
    }

    @Test
    void createModuleDocumentation() {
        ApplicationModules modules = ApplicationModules.of(ApiApplication.class);
        new Documenter(modules).writeDocumentation().writeIndividualModulesAsPlantUml();
    }
}
