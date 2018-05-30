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

fun getPort(dialect: Dialect, port: Int?): Int =
    port ?: when (dialect) {
      Dialect.postgres -> 5432
    }

fun createDataSource(config: DatabaseConfig): HikariDataSource {
  val source = HikariDataSource()
  val port = getPort(config.dialect, config.port)
  source.jdbcUrl = "jdbc:mysql://" + config.host + "/" + port + "/" + config.database
  source.username = config.username
  source.password = config.password
  return source
}

