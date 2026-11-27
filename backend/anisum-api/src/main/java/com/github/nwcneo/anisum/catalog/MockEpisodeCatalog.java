package com.github.nwcneo.anisum.catalog;

import com.github.nwcneo.anisum.api.dto.EpisodeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MockEpisodeCatalog {

    private final Map<Long, List<EpisodeDto>> episodeDtoMap;

    public MockEpisodeCatalog() {
        EpisodeDto naruto1 = new EpisodeDto(1L, 1L, "1", "Название1", "Описание1");
        EpisodeDto naruto2 = new EpisodeDto(2L, 1L, "2", "Название2", "Описание2");
        EpisodeDto naruto3 = new EpisodeDto(3L, 1L, "OVA", "Название3", "Описание3");

        List<EpisodeDto> episodeNaruto = List.of(naruto1, naruto2, naruto3);

        episodeDtoMap = Map.of(
                naruto1.animeId(), episodeNaruto
        );
    }

    public List<EpisodeDto> findByAnimeId(Long animeId) {
        return episodeDtoMap.getOrDefault(animeId, List.of());
    }
}
