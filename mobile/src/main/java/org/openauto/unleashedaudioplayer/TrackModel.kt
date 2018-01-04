package org.openauto.unleashedaudioplayer

class TrackModel : Comparable<TrackModel> {

    override fun compareTo(other: TrackModel): Int {
        try {
            //track without cd, string compare would place 10 before 8
            val i1 = Integer.parseInt(this.track)
            val i2 = Integer.parseInt(other.track)
            return i1.compareTo(i2)
        } catch(e:Exception){
            //Track is given in format track/CD
            return this.track!!.compareTo(other.track!!)
        }
    }

    var id: Long = 0
    var track: String? = null
    var title: String? = null
    var artist: String? = null
    var data: String? = null
    var coverArt: String? = null
    var fileExt: String? = null
}
