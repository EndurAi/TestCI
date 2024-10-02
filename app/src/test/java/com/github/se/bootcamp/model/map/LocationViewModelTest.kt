package com.github.se.bootcamp.model.map

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

class LocationViewModelTest {

  private lateinit var nominatimMockedRepository: NominatimLocationRepository
  private lateinit var locationViewModel: LocationViewModel

  @Before
  fun setUp() {
    nominatimMockedRepository = mock(NominatimLocationRepository::class.java)
    locationViewModel = LocationViewModel(nominatimMockedRepository)
  }

  @Test
  fun `test setQuery calls repository with correct argument`() {
    locationViewModel.setQuery("LookUp")
    verify(nominatimMockedRepository).search(eq("LookUp"), any(), any())
  }
}
