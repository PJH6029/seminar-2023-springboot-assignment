package com.wafflestudio.seminar.spring2023.playlist.service

import com.wafflestudio.seminar.spring2023.playlist.repository.PlaylistGroupRepository
import com.wafflestudio.seminar.spring2023.playlist.repository.PlaylistRepository
import com.wafflestudio.seminar.spring2023.playlist.repository.PlaylistSongRepository
import com.wafflestudio.seminar.spring2023.song.service.Artist
import com.wafflestudio.seminar.spring2023.song.service.Song
import com.wafflestudio.seminar.spring2023.song.service.SongService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
class PlaylistServiceImpl(
    private val songService: SongService,
    private val playlistRepository: PlaylistRepository,
    private val playlistGroupRepository: PlaylistGroupRepository,
    private val playlistSongRepository: PlaylistSongRepository,
) : PlaylistService {

    override fun getGroups(): List<PlaylistGroup> {
        val groups = playlistGroupRepository.findAllByOpen(open = true)
        return groups
            .filter { it.playlists.isNotEmpty() }
            .map {
                PlaylistGroup(
                    id = it.id,
                    title = it.title,
                    playlists = it.playlists.map { entity ->
                        PlaylistBrief(
                            id = entity.id,
                            title = entity.title,
                            subtitle = entity.subtitle,
                            image = entity.image
                        ) }
                )
            }
    }

    override fun get(id: Long): Playlist {
        val playlistEntity = playlistRepository.findById(id).orElseThrow { throw PlaylistNotFoundException()}
        val songs = playlistSongRepository.findPlaylistSongEntitiesByPlaylistIdWithFetchJoin(playlistEntity.id)
        return Playlist(
            id = playlistEntity.id,
            title = playlistEntity.title,
            subtitle = playlistEntity.subtitle,
            image = playlistEntity.image,
            songs = songs.map {
                Song(
                    id = it.song.id,
                    title = it.song.title,
                    artists = it.song.songArtists.map { Artist(it.artist.id, it.artist.name) },
                    album = it.song.album.title,
                    image = it.song.album.image,
                    duration = it.song.duration.toString(),
                )
            }.sortedBy { it.id }
        )
    }
}
