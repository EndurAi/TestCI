package com.github.se.bootcamp.model.map

import junit.framework.TestCase.assertEquals
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

class NominatimRepositoryTest {

  private lateinit var client: OkHttpClient
  private lateinit var nominatimLocationRepository: NominatimLocationRepository
  private lateinit var mockCall: Call
  private lateinit var mockResponse: Response

  @Before
  fun setUp() {
    client = mock(OkHttpClient::class.java)
    nominatimLocationRepository = NominatimLocationRepository(client)
    mockCall = mock(Call::class.java)
    mockResponse = mock(Response::class.java)
  }

  @Test
  fun `parse valid json should return correct locations`() {
    val json =
        """
            [
                {
                    "display_name": "Paris, France",
                    "lon": "2.3522",
                    "lat": "48.8566"
                },
                {
                    "display_name": "Berlin, Germany",
                    "lon": "13.4050",
                    "lat": "52.5200"
                }
            ]
        """
            .trimIndent()

    val result = parseJsonNominatim(json)

    assertEquals(2, result.size)
    assertEquals(Location(48.8566, 2.3522, "Paris, France"), result[0])
    assertEquals(Location(52.5200, 13.4050, "Berlin, Germany"), result[1])
  }

  @Test
  fun `repository should call parseJsonNominatim and return correct locations`() {
    // Given
    val query = "Paris"
    val expectedLocations = listOf(Location(48.8566, 2.3522, "Paris, France"))
    val mockResponseJson =
        "[{\"display_name\":\"Paris, France\",\"lon\":\"2.3522\",\"lat\":\"48.8566\"}]"

    // Mock the response body
    val mockResponseBody = ResponseBody.create("application/json".toMediaType(), mockResponseJson)
    `when`(mockResponse.body).thenReturn(mockResponseBody)
    `when`(mockResponse.isSuccessful).thenReturn(true)

    // Mock OkHttp call
    `when`(client.newCall(any())).thenReturn(mockCall)
    doAnswer {
          val callback = it.getArgument<Callback>(0)
          callback.onResponse(mockCall, mockResponse)
        }
        .`when`(mockCall)
        .enqueue(any())

    // When
    var result: List<Location>? = null
    nominatimLocationRepository.search(
        query,
        onSuccess = { result = it },
        onFailure = {
          // Fail the test if this gets called
          assert(false)
        })

    // Then
    assertEquals(1, result?.size)
    assertEquals(expectedLocations[0], result?.get(0))

    // Verify if the HTTP call was made
    verify(client).newCall(any())
  }
}
