package com.github.nwcneo.anisum.api.dto;

public record AnimeDto(
        Long id,
        String title,
        Integer releaseYear,
        Integer totalEpisodes,
        String description
) {
}
