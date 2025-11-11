package fr.killiandev.book.guide.domain;

import fr.killiandev.book.guide.domain.dto.*;
import fr.killiandev.book.guide.domain.validator.ISO639_1;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/guide")
@RequiredArgsConstructor
public class GuideController {

    private final HousingService housingService;
    private final GuideTranslationService guideTranslationService;
    private final GuideService guideService;

    @Operation(summary = "Get all the housings of the authenticated user")
    @GetMapping("/")
    public GetHousingsDto getHousings(Authentication authentication) {
        log.info("Received Get housings request");
        return housingService.getHousings(authentication.getName());
    }

    @Operation(
            summary =
                    "Get a specific house (so, a list of guide with his language) of the authenticated user by its id")
    @GetMapping("/housings/{housingId}")
    public GetHousingDto getHousing(Authentication authentication, @Positive @PathVariable Long housingId) {
        log.info("Received Get housing by its id request");
        return housingService.getHousing(authentication.getName(), housingId);
    }

    @Operation(summary = "Get a specific guide of the authenticated user by its id")
    @GetMapping("/{guideId}")
    public GetGuideDto getGuide(Authentication authentication, @Positive @PathVariable Long guideId) {
        log.info("Received Get guide by its id request");
        return guideService.getGuide(authentication.getName(), guideId);
    }

    @Operation(summary = "Update a specific guide of the authenticated user")
    @PutMapping("/")
    public GetGuideDto updateGuide(Authentication authentication, @Valid @RequestBody UpdateGuideDto updateProfileDto) {
        log.info("Received Update guide request");
        return guideService.updateGuide(authentication.getName(), updateProfileDto);
    }

    @Operation(summary = "Create a new guide for the authenticated user")
    @PostMapping("/")
    public GetGuideDto createGuide(Authentication authentication, @Valid @RequestBody CreateGuideDto createGuideDto) {
        log.info("Received Create guide request");
        return guideService.createGuide(authentication.getName(), createGuideDto);
    }

    @Operation(
            summary =
                    "Create a new guide based of an airbnb link, a language and other information for the authenticated user with AI assistance")
    @PostMapping("/create-with-ai")
    public GetGuideDto createGuideWithAI(
            Authentication authentication, @Valid @RequestBody CreateGuideWithAiDto createGuideDto) {
        log.info("Received Create guide-with-AI request");
        return guideService.createGuideWithAi(authentication.getName(), createGuideDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary =
                    "Translate a specific guide of the authenticated user. (This will create a new guide to the existing housing if the language doesn't exist yet)")
    @PostMapping("/TO-BE-REMOVED/translate")
    public GetGuideDto translateGuide(
            Authentication authentication, @Valid @RequestBody TranslateGuideDto translateGuideDto) {
        log.info("Received Translate guide request");
        return guideTranslationService.translateGuide(authentication.getName(), translateGuideDto);
    }

    @Operation(summary = "Delete a specific guide of the authenticated user by its id")
    @DeleteMapping("/{guideId}")
    public void deleteGuide(Authentication authentication, @Positive @PathVariable Long guideId) {
        log.info("Received Delete guide by its id request");
        guideService.deleteGuide(authentication.getName(), guideId);
    }

    @Operation(summary = "Get a specific guide by its language and slug")
    @GetMapping("/public/{language}/{slug}")
    public GetGuideDto getGuideBySlug(
            @NotEmpty @ISO639_1 @PathVariable String language, @NotEmpty @PathVariable String slug) {
        log.info("Received PUBLIC Get guide by its language and slug request");
        return guideTranslationService.getGuideWithSpecificLanguage(language, slug);
    }
}
