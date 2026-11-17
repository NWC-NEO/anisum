package com.github.nwcneo.anisum.api;

import com.github.nwcneo.anisum.api.dto.AnimeDto;
import com.github.nwcneo.anisum.catalog.MockAnimeCatalog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/anime")
public class AnimeController {

    private final MockAnimeCatalog catalog;

    public AnimeController(MockAnimeCatalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    public List<AnimeDto> getAllAnime() {
        return catalog.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeDto> getAnimeById(@PathVariable Long id) {
        return ResponseEntity.of(catalog.findById(id));
    }
}
