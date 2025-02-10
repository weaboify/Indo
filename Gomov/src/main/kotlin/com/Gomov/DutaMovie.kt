package com.Gomov

import com.lagradost.cloudstream3.*

class DutaMovie : Gomov() {

    override var mainUrl = "https://scandal.dutamovie21.tv"
    override var name = "DutaMovie"
    override val mainPage =
            mainPageOf(
                    "category/box-office/page/%d/" to "Box Office",
                    "category/serial-tv/page/%d/" to "Serial TV",
                    "category/animation/page/%d/" to "Animasi",
                    "country/korea/page/%d/" to "Serial TV Korea",
                    "country/indonesia/page/%d/" to "Serial TV Indonesia",
            )
}
