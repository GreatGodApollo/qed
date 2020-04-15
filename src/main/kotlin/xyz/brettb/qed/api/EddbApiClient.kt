package xyz.brettb.qed.api

import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import xyz.brettb.qed.errors.*
import xyz.brettb.qed.models.*

class EddbApiClient(var client: OkHttpClient) {

    val baseUrl: String = "https://eddbapi.kodeblox.com/api/v4/"

    fun getSystem(name: String): EdSystem {
        val urlBuilder = HttpUrl.parse(baseUrl + "systems").newBuilder()
        urlBuilder.addQueryParameter("name", name)

        val response = makeRequest((urlBuilder))

        val json    = Json(JsonConfiguration.Stable)
        val page    = json.parse(EdSystemPage.serializer(), response.body().string())

        if (page.total > 0) {
            return page.docs[0]
        } else {
            throw SystemNonExistent()
        }
    }

    fun getSystem(id: Long): EdSystem {
        val urlBuilder = HttpUrl.parse(baseUrl + "systems").newBuilder()
        urlBuilder.addQueryParameter("eddbid", id.toString())

        val response = makeRequest((urlBuilder))

        val json    = Json(JsonConfiguration.Stable)
        val page    = json.parse(EdSystemPage.serializer(), response.body().string())

        if (page.total > 0) {
            return page.docs[0]
        } else {
            throw SystemNonExistent()
        }
    }

    fun getStation(name: String): EdStation {
        val urlBuilder = HttpUrl.parse(baseUrl + "stations").newBuilder()
        urlBuilder.addQueryParameter("name", name)

        val response = makeRequest(urlBuilder)

        val json    = Json(JsonConfiguration.Stable)
        val page    = json.parse(EdStationsPage.serializer(), response.body().string())

        if (page.total > 0) {
            return page.docs[0]
        } else {
            throw StationNonExistent()
        }
    }

    fun getSystemStations(system: EdSystem): List<EdStation> {
        val urlBuilder = HttpUrl.parse(baseUrl + "stations").newBuilder()
        urlBuilder.addQueryParameter("systemname", system.name)

        val response = makeRequest(urlBuilder)

        val json    = Json(JsonConfiguration.Stable)
        val page    = json.parse(EdStationsPage.serializer(), response.body().string())

        if (page.total > 0) {
            if (page.pages == 1L) {
                return page.docs
            } else {
                val stations: MutableList<EdStation> = mutableListOf()
                stations.addAll(page.docs)
                for (i in 2..page.total) {
                    val uBuilder = HttpUrl.parse(baseUrl + "stations").newBuilder()
                    uBuilder.addQueryParameter("systemname", system.name)
                    uBuilder.addQueryParameter("page", i.toString())

                    val resp = makeRequest(uBuilder)
                    val p = json.parse(EdStationsPage.serializer(), resp.body().string())

                    stations.addAll(p.docs)
                }

                return stations
            }
        } else {
            throw StationNonExistent()
        }
    }

    private fun makeRequest(urlBuilder: HttpUrl.Builder): Response {
        val url = urlBuilder.build().toString()

        val request = Request.Builder()
                .url(url)
                .build()

        return client.newCall(request).execute()
    }
}