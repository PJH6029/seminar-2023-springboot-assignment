package com.wafflestudio.seminar.spring2023.song.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SongRepository: JpaRepository<SongEntity, Long> {
    fun findAllByTitleContains(keyword: String): List<SongEntity>

    @Query("SELECT s FROM songs s LEFT JOIN FETCH s.album LEFT JOIN FETCH s.songArtists sa LEFT JOIN FETCH sa.artist WHERE s.title LIKE %:keyword%")
    fun findAllByTitleContainsWithFetchJoin(keyword: String): List<SongEntity>
}