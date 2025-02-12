package com.Gomov

import android.content.Context
import com.lagradost.cloudstream3.extractors.Chillx
import com.lagradost.cloudstream3.extractors.YoutubeExtractor
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class GomovPlugin : Plugin() {
    override fun load(context: Context) {
        // All providers should be added in this manner. Please don't edit the providers list
        // directly.
        registerMainAPI(Gomov())
        registerExtractorAPI(Embedfirex())
        registerExtractorAPI(YoutubeExtractor())
        registerExtractorAPI(Chillx())
        registerExtractorAPI(FilelionsTo())
        registerExtractorAPI(Likessb())
        registerExtractorAPI(DbGdriveplayer())
        registerExtractorAPI(Embedwish())
        registerExtractorAPI(Doods())
        registerExtractorAPI(Lylxan())
        registerExtractorAPI(FilelionsOn())
        registerExtractorAPI(Kotakajaib())
        registerExtractorAPI(Uplayer())
    }
}
