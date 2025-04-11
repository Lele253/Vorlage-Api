package apis.Manga.API.scheduling;

import apis.Manga.API.entity.DefaultPage;
import apis.Manga.API.entity.Page;
import apis.Manga.API.entity.User;
import apis.Manga.API.repository.DefaultPageRepository;
import apis.Manga.API.repository.PageRepository;
import apis.Manga.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@EnableScheduling
public class ScheduledAdminCreation {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DefaultPageRepository defaultPageRepository;
    private final PageRepository pageRepository;

    @Autowired
    public ScheduledAdminCreation(PasswordEncoder passwordEncoder,
                                  UserRepository userRepository,
                                  DefaultPageRepository defaultPageRepository, PageRepository pageRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.defaultPageRepository = defaultPageRepository;
        this.pageRepository = pageRepository;
    }

    @Scheduled(initialDelay = 0, fixedRate = Long.MAX_VALUE)
    public void createAdminUserAdminAdmin() {
        Optional<User> vorhanden = userRepository.findByEmail("admin");
        if (!vorhanden.isPresent()){
            User user = new User();
            user.setEmail("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            User createdAdmin = userRepository.save(user);
            ResponseEntity.ok(createdAdmin);
        }
    }

    @Scheduled(initialDelay = 0, fixedRate = 60000) // alle 60 Sekunden
    public void ensureOneDefaultPage() {
        List<DefaultPage> pages = defaultPageRepository.findAll();
        if (pages.isEmpty()) {
            // Kein DefaultPage vorhanden: Erzeuge eines mit Standardwerten.
            DefaultPage defaultPage = new DefaultPage();
            defaultPage.setEmail(null);
            defaultPage.setPhonenumber(null);
            defaultPage.setAdress(null);
            defaultPage.setLogo(null);
            defaultPage.setOpeningHours(null);
            defaultPageRepository.save(defaultPage);
        } else if (pages.size() > 1) {
            // Mehr als ein DefaultPage vorhanden, behalte das erste und lösche die übrigen.
            DefaultPage keep = pages.get(0);
            for (int i = 1; i < pages.size(); i++) {
                defaultPageRepository.delete(pages.get(i));
            }
        }
    }

    @Scheduled(initialDelay = 0, fixedRate = 60000) // alle 60 Sekunden
    public void ensureSpecificPages() {
        String[] requiredTitles = {"Impressum", "Datenschutz", "Startseite"};

        for (String title : requiredTitles) {
            // Lade alle Page-Objekte und filtere jene mit dem gewünschten Titel (case-insensitive)
            boolean exists = pageRepository.findAll()
                    .stream()
                    .anyMatch(page -> title.equalsIgnoreCase(page.getTitle()));

            // Falls keine Page mit diesem Titel existiert, erstellen wir eine neue
            if (!exists) {
                Page newPage = new Page();
                newPage.setTitle(title);
                // Hier kannst du ggf. weitere Standardwerte setzen:
                newPage.setCanonical(null);
                newPage.setDescription(null);
                newPage.setKeywords(null);
                newPage.setIndex(null);
                newPage.setColors(null);
                newPage.setTexts(null);
                pageRepository.save(newPage);
            }
        }
    }

}
