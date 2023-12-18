package com.wafflestudio.seminar.spring2023.song.service

import com.wafflestudio.seminar.spring2023.song.repository.AlbumRepository
import com.wafflestudio.seminar.spring2023.song.repository.SongRepository
import org.springframework.stereotype.Service

@Service
class SongServiceImpl(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
) : SongService {

    override fun search(keyword: String): List<Song> {
        val songEntities = songRepository.findAllByTitleContainsWithFetchJoin(keyword = keyword)
        return songEntities.map {
            Song(
                id = it.id,
                title = it.title,
                artists = it.songArtists.map { songArtist -> Artist(songArtist.artist.id, songArtist.artist.name) },
                album = it.album.title,
                image = it.album.image,
                duration = it.duration.toString(),
            )
        }
            .sortedBy { it.title.length }
//            .also { println(it.map { "${it.id} / ${it.title} / ${it.album}" }) }
    }

    override fun searchAlbum(keyword: String): List<Album> {
        val albumEntities = albumRepository.findAllByTitleContainsWithFetchJoin(keyword = keyword)
        return albumEntities.map {
            Album(
                id = it.id,
                title = it.title,
                image = it.image,
                artist = Artist(it.artist.id, it.artist.name),
            )
        }
            .sortedBy { it.title.length }
    }
}
