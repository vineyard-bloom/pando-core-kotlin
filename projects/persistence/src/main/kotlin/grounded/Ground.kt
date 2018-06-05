package grounded

import com.zaxxer.hikari.HikariDataSource

enum class Dialect {
  postgres
}

data class DatabaseConfig(
    val host: String,
    val database: String,
    val username: String,
    val password: String,
    val dialect: Dialect,
    val port: Int?
)

fun createDataSource(config: DatabaseConfig): HikariDataSource {
  val source = HikariDataSource()
  val portString = if (config.port != null) ":${config.port}" else ""
  source.jdbcUrl = "jdbc:postgresql://${config.host}$portString/${config.database}"
  source.username = config.username
  source.password = config.password
  return source
}

