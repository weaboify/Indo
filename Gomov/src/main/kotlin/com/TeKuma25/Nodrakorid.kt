package com.tekuma25

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class Nodrakorid : Gomov() {
    override var mainUrl = "https://tv.nodrakor22.sbs"
    override var name = "Nodrakorid"

    override val mainPage = mainPageOf(
        "genre/movie/page/%d/" to "Film Terbaru",
        "genre/drama/page/%d/" to "Drama Korea",
        "genre/c-drama/page/%d/" to "Drama China",
        "genre/fantasy/page/%d/" to "Fantasy",
    )

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        // Parsing metadata film
        val title = document.selectFirst("h1.entry-title")?.text().orEmpty()
        val description = document.selectFirst("div.entry-content p")?.text().orEmpty()
        val year = document.selectFirst(".gmr-moviedata a[rel=tag]")?.text()?.toIntOrNull()
        val posterUrl = document.selectFirst(".gmr-movie-data-top img")?.attr("src")
        val genre = document.select(".gmr-moviedata a[rel=category tag]").joinToString(", ") { it.text() }

        // Parsing tabel tautan unduhan
        val downloadLinks = parseDownloadLinks(document)

        return MovieLoadResponse(
            name = title,
            url = url,
            apiName = this.name, // Wajib disertakan
            type = TvType.Movie, // Pastikan sesuai dengan jenis konten
            dataUrl = url,       // URL data yang dimuat
            posterUrl = posterUrl,
            year = year,
            plot = description,
            rating = null,
            duration = null,
            tags = listOf(genre),
        )
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val links = tryParseJson<ArrayList<LinkData>>(data) ?: return false
        links.forEach { linkData ->
            linkData.second.forEach { second ->
                callback.invoke(
                    ExtractorLink(
                        source = second.second,  // Nama server
                        name = second.second,    // Nama server
                        url = second.first,      // URL unduhan
                        referer = mainUrl,
                        quality = linkData.first ?: Qualities.Unknown.value,
                        isM3u8 = false           // Tambahkan ini jika diperlukan
                    )
                )
            }
        }
        return true
    }

    private fun parseDownloadLinks(document: Document): List<LinkData> {
        val rows = document.select("table tbody tr")
        return rows.mapNotNull { row ->
            val provider = row.selectFirst("td strong")?.text() ?: return@mapNotNull null
            val linkElement = row.selectFirst("td a")
            val url = linkElement?.attr("href") ?: return@mapNotNull null
            val resolution = linkElement.attr("class").substringAfter("btn-").substringBefore(" ")
            LinkData(
                first = resolution.toIntOrNull(),
                second = arrayListOf(
                    Second(first = url, second = provider)
                )
            )
        }
    }

    data class LinkData(
        @JsonProperty("first") var first: Int? = null,
        @JsonProperty("second") var second: ArrayList<Second> = arrayListOf()
    )

    data class Second(
        @JsonProperty("first") var first: String,
        @JsonProperty("second") var second: String
    )
}