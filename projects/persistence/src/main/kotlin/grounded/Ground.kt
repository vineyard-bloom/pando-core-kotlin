package grounded

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

enum class Dialect {
  postgres,
  sqlite
}

data class DatabaseConfig(
  val host: String,
  val database: String,
  val username: String,
  val password: String,
  val dialect: Dialect,
  val port: Int?
)

fun createDataSource(config: DatabaseConfig): DataSource {
  val portString = if (config.port != null) ":${config.port}" else ""
  val source = HikariDataSource()
  
  when(config.dialect) {

    Dialect.postgres -> {
      source.jdbcUrl = "jdbc:postgresql://${config.host}$portString/${config.database}"
      source.username = config.username
      source.password = config.password
    }

    Dialect.sqlite -> {
      source.jdbcUrl = "jdbc:sqlite:${config.database}.db"
    }
  }

  return source
}

