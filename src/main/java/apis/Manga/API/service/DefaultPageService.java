package apis.Manga.API.service;

import apis.Manga.API.dto.DefaultPageDTO;
import apis.Manga.API.entity.DefaultPage;
import apis.Manga.API.entity.Page;
import apis.Manga.API.entity.StringList;
import apis.Manga.API.repository.DefaultPageRepository;
import apis.Manga.API.repository.PageRepository;
import apis.Manga.API.repository.StringListRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DefaultPageService {

    private final DefaultPageRepository defaultPageRepository;
    private final PageRepository pageRepository;
    private final StringListRepository stringListRepository;

    public DefaultPageService(DefaultPageRepository defaultPageRepository,
                              PageRepository pageRepository,
                              StringListRepository stringListRepository) {
        this.defaultPageRepository = defaultPageRepository;
        this.pageRepository = pageRepository;
        this.stringListRepository = stringListRepository;
    }

    /**
     * Liefert das einzige DefaultPage-Objekt als DefaultPageDTO.
     * Zusätzlich werden alle Page-Titel (sortiert nach "index") in das DTO übernommen.
     */
    public DefaultPageDTO getDefaultPage() {
        DefaultPage defaultPage = defaultPageRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Kein DefaultPage-Objekt gefunden."));
        return mapToDto(defaultPage);
    }

    /**
     * Aktualisiert das vorhandene DefaultPage-Objekt mit den Werten aus dem übergebenen Objekt.
     * Dabei werden alle Felder (inklusive der Beziehungen zu orgNames und texts) aktualisiert.
     * Nicht mehr referenzierte (orphaned) StringList-Objekte werden anschließend aus der Datenbank gelöscht.
     * Das aktualisierte Objekt wird als DefaultPageDTO zurückgegeben.
     */
    @Transactional
    public DefaultPageDTO updateDefaultPage(DefaultPage updatedDefaultPage) {
        // Hole das vorhandene DefaultPage-Objekt (angenommen, es gibt nur eins)
        DefaultPage existingDefault = defaultPageRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Kein DefaultPage-Objekt gefunden."));

        // Verarbeitung der Many-to-Many-Beziehung orgNames:
        ArrayList<StringList> currentOrgNames = existingDefault.getOrgNames() != null
                ? new ArrayList<>(existingDefault.getOrgNames())
                : new ArrayList<>();
        ArrayList<StringList> newOrgNames = updatedDefaultPage.getOrgNames() != null
                ? new ArrayList<>(updatedDefaultPage.getOrgNames())
                : new ArrayList<>();
        ArrayList<Long> newOrgNamesIds = newOrgNames.stream()
                .map(StringList::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<StringList> orphanOrgNames = currentOrgNames.stream()
                .filter(org -> org.getId() != null && !newOrgNamesIds.contains(org.getId()))
                .collect(Collectors.toCollection(ArrayList::new));

        // Verarbeitung der Many-to-Many-Beziehung texts:
        ArrayList<StringList> currentTexts = existingDefault.getTexts() != null
                ? new ArrayList<>(existingDefault.getTexts())
                : new ArrayList<>();
        ArrayList<StringList> newTexts = updatedDefaultPage.getTexts() != null
                ? new ArrayList<>(updatedDefaultPage.getTexts())
                : new ArrayList<>();
        ArrayList<Long> newTextsIds = newTexts.stream()
                .map(StringList::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<StringList> orphanTexts = currentTexts.stream()
                .filter(text -> text.getId() != null && !newTextsIds.contains(text.getId()))
                .collect(Collectors.toCollection(ArrayList::new));

        // Aktualisiere die einfachen Felder
        existingDefault.setEmail(updatedDefaultPage.getEmail());
        existingDefault.setPhonenumber(updatedDefaultPage.getPhonenumber());
        existingDefault.setAdress(updatedDefaultPage.getAdress());
        existingDefault.setLogo(updatedDefaultPage.getLogo());
        existingDefault.setOpeningHours(updatedDefaultPage.getOpeningHours());

        // Setze die aktualisierten Beziehungen
        existingDefault.setOrgNames(newOrgNames);
        existingDefault.setTexts(newTexts);

        // Speichere das aktualisierte Objekt
        DefaultPage savedDefault = defaultPageRepository.save(existingDefault);

        // Lösche die verwaisten (orphaned) StringList-Objekte aus orgNames und texts
        orphanOrgNames.forEach(stringListRepository::delete);
        orphanTexts.forEach(stringListRepository::delete);

        return mapToDto(savedDefault);
    }
    /**
     * Mapped ein DefaultPage-Objekt in ein DefaultPageDTO.
     * Alle Felder werden übernommen, und zusätzlich wird die Liste der Page-Titel (sortiert nach "index") in das DTO gesetzt.
     */
    private DefaultPageDTO mapToDto(DefaultPage defaultPage) {
        DefaultPageDTO dto = new DefaultPageDTO();
        dto.setId(defaultPage.getId());
        dto.setEmail(defaultPage.getEmail());
        dto.setPhonenumber(defaultPage.getPhonenumber());
        dto.setAdress(defaultPage.getAdress());
        dto.setLogo(defaultPage.getLogo());
        dto.setOpeningHours(defaultPage.getOpeningHours());
        dto.setOrgNames(defaultPage.getOrgNames());
        dto.setTexts(defaultPage.getTexts()); // Hier wird auch das Texte-Feld übertragen

        // Lade alle Page-Einträge, sortiert nach "index", und extrahiere deren Titel
        List<Page> pages = pageRepository.findAll(Sort.by("index"));
        List<String> titles = pages.stream()
                .map(Page::getTitle)
                .toList();
        dto.setTitles(new ArrayList<>(titles));

        return dto;
    }

}
