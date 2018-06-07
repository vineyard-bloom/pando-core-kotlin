package jsoning

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

inline fun <reified T> loadJsonFile(path: String): T {
  if (!File(path).isFile)
    throw FileNotFoundException(path)

  val mapper = ObjectMapper()
  mapper.registerModule(KotlinModule())

  return Files.newBufferedReader(Paths.get(path)).use {
    mapper.readValue(it, T::class.java)
  }
}

inline fun <reified T> parseJson(json: String): T {
  val mapper = ObjectMapper()
  mapper.registerModule(KotlinModule())
  return mapper.readValue(json, T::class.java)
}
