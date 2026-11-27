package com.github.nwcneo.anisum.api.dto;

public record EpisodeDto(
        Long id,
        Long animeId,
        String episodeKey,
        String title,
        String description
) {}
