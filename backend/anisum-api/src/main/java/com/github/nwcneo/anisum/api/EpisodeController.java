package com.github.nwcneo.anisum.api;

import com.github.nwcneo.anisum.api.dto.EpisodeDto;
import com.github.nwcneo.anisum.catalog.MockAnimeCatalog;
import com.github.nwcneo.anisum.catalog.MockEpisodeCatalog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/anime")
public class EpisodeController {

    private final MockEpisodeCatalog episodeCatalog;
    private final MockAnimeCatalog animeCatalog;

    public EpisodeController(
            MockEpisodeCatalog episodeCatalog,
            MockAnimeCatalog animeCatalog
    ) {
        this.episodeCatalog = episodeCatalog;
        this.animeCatalog = animeCatalog;
    }

    @GetMapping("/{id}/episodes")
    public ResponseEntity<List<EpisodeDto>> getEpisodesBiAnimeId(@PathVariable Long id) {
        if (animeCatalog.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EpisodeDto> episodes = episodeCatalog.findByAnimeId(id);

        return ResponseEntity.ok(episodes);
    }
}
