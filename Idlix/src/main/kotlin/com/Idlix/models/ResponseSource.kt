package com.Idlix

import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseSource(
    @JsonProperty("hls") val hls: Boolean,
    @JsonProperty("videoSource") val videoSource: String,
    @JsonProperty("tracks") val subtitleTracks: List<Tracks>?,
    @JsonProperty("securedLink") val securedLink: String?
)

data class Tracks(
    @JsonProperty("kind") val kind: String?,
    @JsonProperty("file") val file: String,
    @JsonProperty("label") val label: String?,
)