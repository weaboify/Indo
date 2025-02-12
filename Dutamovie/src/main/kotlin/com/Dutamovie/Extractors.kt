package com.Dutamovie

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.extractors.JWPlayer

class Embedfirex : JWPlayer() {
    override var name = "Embedfirex"
    override var mainUrl = "https://embedfirex.xyz"
}

class Ryderjet : JWPlayer() {
    override val name = "Ryderjet"
    override val mainUrl = "https://ryderjet.com"
}
