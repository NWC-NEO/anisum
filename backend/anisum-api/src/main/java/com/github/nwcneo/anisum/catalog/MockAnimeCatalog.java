package com.github.nwcneo.anisum.catalog;

import com.github.nwcneo.anisum.api.dto.AnimeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class MockAnimeCatalog {

    private final Map<Long, AnimeDto> animeById;
    private final Map<Long, List<Long>> franchisePartIdsByAnimeId;

    public MockAnimeCatalog() {
        AnimeDto naruto = new AnimeDto(1L, "Naruto", 2002, 220, "тест");
        AnimeDto bleach = new AnimeDto(2L, "Bleach", 2004, 366, "тест");
        AnimeDto gto = new AnimeDto(3L, "GTO", 1999, 43, "тест");
        AnimeDto frieren = new AnimeDto(4L, "Frieren", 2023, 28, "тест");
        AnimeDto spyFamily = new AnimeDto(5L, "Spy x Family", 2022, 25, "тест");
        AnimeDto narutoShippuden = new AnimeDto(6L, "Naruto Shippuden", 2015, 25, "тест");

        animeById = Map.of(
                naruto.id(), naruto,
                bleach.id(), bleach,
                gto.id(), gto,
                frieren.id(), frieren,
                spyFamily.id(),  spyFamily,
                narutoShippuden.id(), narutoShippuden
        );

        franchisePartIdsByAnimeId = Map.of(
                naruto.id(), List.of(narutoShippuden.id()),
                narutoShippuden.id(), List.of(naruto.id())
        );
    }

    public List<AnimeDto> findAll() {
        return List.copyOf(animeById.values());
    }

    public Optional<AnimeDto> findById(Long id) {
        return Optional.ofNullable(animeById.get(id));
    }

    public List<AnimeDto> findFranchiseByAnimeId(Long id) {
        return franchisePartIdsByAnimeId
                .getOrDefault(id, List.of())
                .stream()
                .map(animeById::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
