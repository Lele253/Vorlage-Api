package apis.Manga.API.controller;

import apis.Manga.API.dto.DefaultPageDTO;
import apis.Manga.API.entity.DefaultPage;
import apis.Manga.API.service.DefaultPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth/defaultpage")
public class DefaultPageController {

    @Autowired
    private DefaultPageService defaultPageService;

    // GET: Liefert das einzige DefaultPage-Objekt
    @GetMapping
    public DefaultPageDTO getDefaultPage() {
        return defaultPageService.getDefaultPage();
    }

    // PUT: Aktualisiert das einzige DefaultPage-Objekt basierend auf dem DTO
    @PutMapping
    public DefaultPageDTO updateDefaultPage(@RequestBody DefaultPage defaultPage) {
        return defaultPageService.updateDefaultPage(defaultPage);
    }
}
