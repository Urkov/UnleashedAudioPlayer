package org.openauto.unleashedaudioplayer.entities

class AlbumModel : Comparable<AlbumModel> {

    override fun compareTo(other: AlbumModel): Int {
         return this.artist!!.compareTo(other.artist!!)
    }

    var name: String? = null
    var art: String? = null
    var id: Long = 0
    var tracks: String? = null
    var artist: String? = null
}
