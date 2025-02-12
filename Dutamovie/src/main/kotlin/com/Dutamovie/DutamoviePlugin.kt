package com.Dutamovie

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import com.lagradost.cloudstream3.extractors.JWPlayer

@CloudstreamPlugin
class GomovPlugin : Plugin() {
    override fun load(context: Context) {
        // All providers should be added in this manner. Please don't edit the providers list
        // directly.
        registerMainAPI(DutaMovie())
        registerExtractorAPI(Ryderjet())
        registerExtractorAPI(JWPlayer())
        registerExtractorAPI(Embedfirex())
    }
}
