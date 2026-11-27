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

    private final MockAnimeCatalog animeCatalog;

    public AnimeController(MockAnimeCatalog animeCatalog) {
        this.animeCatalog = animeCatalog;
    }

    @GetMapping
    public List<AnimeDto> getAllAnime() {
        return animeCatalog.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeDto> getAnimeById(@PathVariable Long id) {
        return ResponseEntity.of(animeCatalog.findById(id));
    }

    @GetMapping("/{id}/franchise-parts")
    public ResponseEntity<List<AnimeDto>> getFranchisePartsByAnimeId(@PathVariable Long id) {
        if (animeCatalog.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<AnimeDto> franchise = animeCatalog.findFranchiseByAnimeId(id);
        return ResponseEntity.ok(franchise);
    }
}
