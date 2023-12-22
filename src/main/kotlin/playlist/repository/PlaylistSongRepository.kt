package com.wafflestudio.seminar.spring2023.playlist.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlaylistSongRepository : JpaRepository<PlaylistSongEntity, Long> {
    @Query("SELECT ps " +
            "FROM playlist_songs ps " +
            "LEFT JOIN FETCH ps.song s " +
            "LEFT JOIN FETCH s.album ab " +
            "LEFT JOIN FETCH s.songArtists sa " +
            "LEFT JOIN FETCH sa.artist " +
            "WHERE ps.playlist.id = :playlistId")
    fun findPlaylistSongEntitiesByPlaylistIdWithFetchJoin(playlistId: Long) : List<PlaylistSongEntity>
}