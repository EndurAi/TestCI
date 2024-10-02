package com.github.se.bootcamp.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Greeting() {
  var txtName by remember { mutableStateOf("") }
  var txtRep by remember { mutableStateOf("What's your name ?") }

  Scaffold(
      topBar = { TopAppBar(title = { androidx.compose.material.Text("top text") }) },
  ) { innerPadding ->
    print(innerPadding)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

          // text field
          OutlinedTextField(
              value = txtName,
              onValueChange = { s -> txtName = s },
              modifier = Modifier.testTag("greetingNameInput"))

          // button
          Button(
              onClick = {
                Log.d("DEB", txtName)
                txtRep = "Hi $txtName !"
                txtName = ""
              },
              modifier = Modifier.padding(30.dp).testTag("greetingButton")) {
                androidx.compose.material.Text("Valid")
              }

          // Label

          androidx.compose.material.Text(
              text = txtRep, modifier = Modifier.testTag("greetingDisplay"))
        }
  }
}
