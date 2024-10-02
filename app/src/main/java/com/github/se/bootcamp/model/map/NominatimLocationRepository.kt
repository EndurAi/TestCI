package com.github.se.bootcamp.model.map

import android.util.Log
import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray

open class NominatimLocationRepository(val client: OkHttpClient) : LocationRepository {
  override fun search(
      query: String,
      onSuccess: (List<Location>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {

    val TAG = "NominatimLocationRepository:"

    val request =
        Request.Builder()
            .url(
                "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=3&polygon_kml=1&addressdetails=0")
            .header("User-Agent", "MyAppMap/1.0")
            .build()

    client
        .newCall(request)
        .enqueue(
            object : Callback {
              override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "Failure when making the request")
                onFailure(e)
              }

              override fun onResponse(call: Call, response: Response) {
                response.use {
                  val jsonData = response.body?.string()
                  Log.d("ResponseOut", jsonData ?: "NULL")

                  if (!response.isSuccessful) {
                    onFailure(IOException("Response not successful"))
                  }

                  onSuccess(parseJsonNominatim(jsonData ?: ""))
                }
              }
            })
  }
}

fun parseJsonNominatim(json: String): List<Location> {
  try {

    val jsonArray = JSONArray(json)
    val locationList = mutableListOf<Location>()
    for (i in 0 until jsonArray.length()) {
      val jsonObj = jsonArray.getJSONObject(i)
      val display_Name = jsonObj.getString("display_name")
      val longitude = jsonObj.getString("lon").toDouble()
      val latitude = jsonObj.getString("lat").toDouble()

      locationList.add(Location(latitude, longitude, display_Name))
    }
    return locationList
  } catch (e: Exception) {
    return emptyList()
  }
}
