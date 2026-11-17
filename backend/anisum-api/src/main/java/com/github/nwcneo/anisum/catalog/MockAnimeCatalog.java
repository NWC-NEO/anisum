package com.github.nwcneo.anisum.catalog;

import com.github.nwcneo.anisum.api.dto.AnimeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MockAnimeCatalog {

    private final Map<Long, AnimeDto> animeById;

    public MockAnimeCatalog() {
        AnimeDto naruto = new AnimeDto(1L, "Naruto", 2002, 220, "тест");
        AnimeDto bleach = new AnimeDto(2L, "Bleach", 2004, 366, "тест");
        AnimeDto gto = new AnimeDto(3L, "GTO", 1999, 43, "тест");
        AnimeDto frieren = new AnimeDto(4L, "Frieren", 2023, 28, "тест");
        AnimeDto spyFamily = new AnimeDto(5L, "Spy x Family", 2022, 25, "тест");

        animeById = Map.of(
                naruto.id(), naruto,
                bleach.id(), bleach,
                gto.id(), gto,
                frieren.id(), frieren,
                spyFamily.id(), spyFamily
        );
    }

    public List<AnimeDto> findAll() {
        return List.copyOf(animeById.values());
    }

    public Optional<AnimeDto> findById(Long id) {
        return Optional.ofNullable(animeById.get(id));
    }
}
