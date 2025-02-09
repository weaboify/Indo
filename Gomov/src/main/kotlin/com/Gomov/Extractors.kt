package com.Gomov

import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.apmap
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.base64Decode
import com.lagradost.cloudstream3.extractors.*
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.loadExtractor

open class Uplayer : ExtractorApi() {
    override val name = "Uplayer"
    override val mainUrl = "https://uplayer.xyz"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val res = app.get(url,referer=referer).text
        val m3u8 = Regex("file:\\s*\"(.*?m3u8.*?)\"").find(res)?.groupValues?.getOrNull(1)
        M3u8Helper.generateM3u8(
            name,
            m3u8 ?: return,
            mainUrl
        ).forEach(callback)
    }

}

open class Kotakajaib : ExtractorApi() {
    override val name = "Kotakajaib"
    override val mainUrl = "https://kotakajaib.me"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        app.get(url,referer=referer).document.select("ul#dropdown-server li a").apmap {
            loadExtractor(base64Decode(it.attr("data-frame")), "$mainUrl/", subtitleCallback, callback)
        }
    }

}

open class JWPlayer : ExtractorApi() {
    override val name = "JWPlayer"
    override val mainUrl = "https://www.jwplayer.com"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink>? {
        val sources = mutableListOf<ExtractorLink>()
        with(app.get(url).document) {
            val data = this.select("script").mapNotNull { script ->
                if (script.data().contains("sources: [")) {
                    script.data().substringAfter("sources: [")
                        .substringBefore("],").replace("'", "\"")
                } else if (script.data().contains("otakudesu('")) {
                    script.data().substringAfter("otakudesu('")
                        .substringBefore("');")
                } else {
                    null
                }
            }

            tryParseJson<List<ResponseSource>>("$data")?.map {
                sources.add(
                    ExtractorLink(
                        name,
                        name,
                        it.file,
                        referer = url,
                        quality = getQualityFromName(
                            Regex("(\\d{3,4}p)").find(it.file)?.groupValues?.get(
                                1
                            )
                        )
                    )
                )
            }
        }
        return sources
    }

    private data class ResponseSource(
        @JsonProperty("file") val file: String,
        @JsonProperty("type") val type: String?,
        @JsonProperty("label") val label: String?
    )

}

class Doods : DoodLaExtractor() {
    override var name = "Doods"
    override var mainUrl = "https://doods.pro"
}

class Dutamovie21 : StreamSB() {
    override var name = "Dutamovie21"
    override var mainUrl = "https://scandal.dutamovie21.tv"
}

class FilelionsTo : Filesim() {
    override val name = "Filelions"
    override var mainUrl = "https://filelions.to"
}

class FilelionsOn : Filesim() {
    override val name = "Filelions"
    override var mainUrl = "https://filelions.online"
}

class Lylxan : Filesim() {
    override val name = "Lylxan"
    override var mainUrl = "https://lylxan.com"
}

class Embedwish : Filesim() {
    override val name = "Embedwish"
    override var mainUrl = "https://embedwish.com"
}

class Likessb : StreamSB() {
    override var name = "Likessb"
    override var mainUrl = "https://likessb.com"
}

class DbGdriveplayer : Gdriveplayer() {
    override var mainUrl = "https://database.gdriveplayer.us"
}

class Asiaplayer : JWPlayer() {
    override val name = "Asiaplayer"
    override val mainUrl = "https://watch.asiaplayer.cc"
}