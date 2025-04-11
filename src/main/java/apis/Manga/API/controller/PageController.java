package apis.Manga.API.controller;

import apis.Manga.API.entity.Page;
import apis.Manga.API.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auth/page")
public class PageController {

    @Autowired
    private PageService pageService;

    // CREATE
    @PostMapping
    public Page createPage(@RequestBody Page page) {
        return pageService.createPage(page);
    }

    // READ ONE
    @GetMapping("/{id}")
    public Page getPage(@PathVariable int id) {
        return pageService.getPageById(id);
    }

    // READ ALL
    @GetMapping
    public List<Page> getAllPages() {
        return pageService.getAllPages();
    }

    // UPDATE
    @PutMapping("/{id}")
    public Page updatePage(@PathVariable int id, @RequestBody Page page) {
        return pageService.updatePage(id, page);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletePage(@PathVariable int id) {
        pageService.deletePage(id);
    }

    // READ BY TITLE
    @GetMapping("/byTitle/{title}")
    public Page getPageByTitle(@PathVariable String title) {
        return pageService.getPageByTitle(title);
    }
}
