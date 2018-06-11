package grounded

import com.zaxxer.hikari.HikariDataSource
import org.sqlite.SQLiteDataSource
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

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

fun createDataSource(config: DatabaseConfig): Pair<HikariDataSource, SQLiteDataSource> {
  val psqlSource = HikariDataSource()
  val sqliteSource = SQLiteDataSource()
  if (config.dialect == Dialect.postgres) {
    val portString = if (config.port != null) ":${config.port}" else ""
    psqlSource.jdbcUrl = "jdbc:postgresql://${config.host}$portString/${config.database}"
    psqlSource.username = config.username
    psqlSource.password = config.password

  }
  else {
    sqliteSource.url = "jdbc:sqlite:${config.database}.db"
  }

  return Pair(psqlSource, sqliteSource)
}

