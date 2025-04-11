package apis.Manga.API.service;

import apis.Manga.API.entity.Page;
import apis.Manga.API.entity.StringList;
import apis.Manga.API.repository.PageRepository;
import apis.Manga.API.repository.StringListRepository;
import org.springframework.stereotype.Service;
// Optional: import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PageService {

    private final PageRepository pageRepository;
    private final StringListRepository stringListRepository;

    // Constructor Injection – entfernt die Field Injection Warnings
    public PageService(PageRepository pageRepository, StringListRepository stringListRepository) {
        this.pageRepository = pageRepository;
        this.stringListRepository = stringListRepository;
    }

    /**
     * Erstellt eine neue Page und speichert sie.
     */
    public Page createPage(Page page) {
        return pageRepository.save(page);
    }

    /**
     * Gibt eine Page anhand der ID zurück.
     */
    public Page getPageById(int id) {
        return pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + id));
    }

    /**
     * Gibt alle Pages als Liste zurück.
     */
    public List<Page> getAllPages() {
        return pageRepository.findAll();
    }

    /**
     * Aktualisiert eine bestehende Page und löscht verwaiste StringList-Einträge
     * aus den Many-to-Many-Feldern "colors" und "texts".
     */
//  @Transactional // optional, für atomares Update
    public Page updatePage(int id, Page updatedPage) {
        Page existingPage = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + id));

        // Ermittele verwaiste Einträge in "colors"
        List<StringList> orphanColors = findOrphans(existingPage.getColors(), updatedPage.getColors());

        // Ermittele verwaiste Einträge in "texts"
        List<StringList> orphanTexts = findOrphans(existingPage.getTexts(), updatedPage.getTexts());

        // Aktualisiere die Felder von existingPage
        existingPage.setKeywords(updatedPage.getKeywords());
        existingPage.setDescription(updatedPage.getDescription());
        existingPage.setCanonical(updatedPage.getCanonical());
        existingPage.setTitle(updatedPage.getTitle());
        existingPage.setIndex(updatedPage.getIndex());
        existingPage.setColors(updatedPage.getColors());
        existingPage.setTexts(updatedPage.getTexts());

        // Page aktualisieren
        Page savedPage = pageRepository.save(existingPage);

        // Orphans zu einer Liste zusammenfassen und in einem Durchgang löschen
        List<StringList> allOrphans = new ArrayList<>();
        allOrphans.addAll(orphanColors);
        allOrphans.addAll(orphanTexts);

        if (!allOrphans.isEmpty()) {
            stringListRepository.deleteAll(allOrphans);
        }

        return savedPage;
    }

    /**
     * Löscht eine Page anhand ihrer ID.
     */
    public void deletePage(int id) {
        Page existingPage = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + id));
        pageRepository.delete(existingPage);
    }

    /**
     * Hilfsmethode zum Auffinden "verwaister" (orphaned) StringList-Einträge,
     * die in der aktuellen Liste vorhanden waren, aber in der neuen Liste nicht mehr vorkommen.
     */
    private List<StringList> findOrphans(List<StringList> currentList, List<StringList> newList) {
        if (currentList == null) {
            return List.of(); // Keine alten Einträge
        }
        if (newList == null) {
            // Alles in currentList wird orphaned
            return currentList;
        }
        // IDs der neuen Liste
        List<Long> newIds = newList.stream()
                .map(StringList::getId)
                .filter(Objects::nonNull)
                .toList();

        // Orphans = Einträge mit einer ID, die in der neuen Liste nicht mehr auftaucht
        return currentList.stream()
                .filter(item -> item.getId() != null && !newIds.contains(item.getId()))
                .toList();
    }

    public Page getPageByTitle(String title) {
        return pageRepository.findAll().stream()
                .filter(page -> page.getTitle() != null && page.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Page not found with title: " + title));
    }

}
