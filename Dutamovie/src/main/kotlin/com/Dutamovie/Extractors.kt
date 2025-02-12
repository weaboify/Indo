package com.Dutamovie

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.apmap
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.base64Decode
import com.lagradost.cloudstream3.extractors.*
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.loadExtractor

open class JWPlayer : ExtractorApi() {
    override val name = "JWPlayer"
    override val mainUrl = "https://www.jwplayer.com"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink>? {
        val sources = mutableListOf<ExtractorLink>()
        with(app.get(url).document) {
            val data =
                    this.select("script").mapNotNull { script ->
                        if (script.data().contains("sources: [")) {
                            script.data()
                                    .substringAfter("sources: [")
                                    .substringBefore("],")
                                    .replace("'", "\"")
                        } else if (script.data().contains("otakudesu('")) {
                            script.data().substringAfter("otakudesu('").substringBefore("');")
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
                                quality =
                                        getQualityFromName(
                                                Regex("(\\d{3,4}p)")
                                                        .find(it.file)
                                                        ?.groupValues
                                                        ?.get(1)
                                        )
                        )
                )
            }
        }
        return sources
    }

// DutaMovie

class Embedfirex : JWPlayer() {
    override var name = "Embedfirex"
    override var mainUrl = "https://embedfirex.xyz"
}

class Ryderjet : JWPlayer() {
    override val name = "Ryderjet"
    override val mainUrl = "https://ryderjet.com"
}
