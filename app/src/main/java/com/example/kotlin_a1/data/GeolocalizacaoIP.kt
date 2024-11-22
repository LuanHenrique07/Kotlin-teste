package com.example.kotlin_a1.data

data class GeolocalizacaoIP(
    val ip: String = "",
    val hostname: String? = null,
    val continent_code: String? = null,
    val city: String? = null
)
