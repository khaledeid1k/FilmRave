package com.codewithshadow.filmrave.data.model

import com.codewithshadow.filmrave.domain.model.IN
import com.codewithshadow.filmrave.domain.model.Results
import com.codewithshadow.filmrave.domain.model.WatchProviders
import com.google.gson.annotations.SerializedName


data class WatchProvidersResponse(
    var id: Int? = null,
    var results: ResultsResponse? = null
) {
    fun toWatchProviders(): WatchProviders {
        return WatchProviders(id = id, results = results?.toResult())
    }
}


data class ResultsResponse(
    @SerializedName("IN")
    var iN: INResponse? = null
) {
    fun toResult(): Results {
        return Results(iN = iN?.toIn())
    }
}

data class INResponse(
    val link: String?,
) {
    fun toIn(): IN {
        return IN(link = link)
    }
}