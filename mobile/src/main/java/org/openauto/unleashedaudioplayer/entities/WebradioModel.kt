package org.openauto.unleashedaudioplayer.entities

class WebradioModel : Comparable<WebradioModel> {

    override fun compareTo(other: WebradioModel): Int {
         return this.title!!.compareTo(other.title!!)
    }

    var title: String? = null
    var subtitle: String? = null
    var stream: String? = null
    var cover: String? = null
}
