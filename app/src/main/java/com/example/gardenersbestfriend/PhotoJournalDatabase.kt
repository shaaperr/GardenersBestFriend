package com.example.gardenersbestfriend

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class PhotoJournalDatabase(private val dbUrl: String) {
    private var connection: Connection? = null

    init {
        connect()
    }

    private fun connect() {
        connection = DriverManager.getConnection(dbUrl)
    }

    fun createTables() {
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS entries (
                id INTEGER PRIMARY KEY,
                date DATE,
                text TEXT,
                plant_name TEXT,
                reminder_days TEXT
            )
        """

        val statement: PreparedStatement = connection?.prepareStatement(createTableSQL) ?: return
        statement.execute()
    }

    fun insertEntry(date: String,  text: String, plantName: String, reminderDays: String) {
        val insertSQL = "INSERT INTO entries (date, text, plant_name, reminder_days) VALUES (?, ?, ?, ?)"

        val statement: PreparedStatement = connection?.prepareStatement(insertSQL) ?: return
        statement.setString(1, date)
       // statement.setBytes(2, photo)
        statement.setString(2, text)
        statement.setString(3, plantName)
        statement.setString(4, reminderDays)
        statement.execute()
    }

    fun close() {
        connection?.close()
    }

    fun getEntries(): List<Entry> {
        val selectSQL = "SELECT * FROM entries"
        val statement: PreparedStatement = connection?.prepareStatement(selectSQL) ?: return emptyList()

        val resultSet: ResultSet = statement.executeQuery()

        val entries = mutableListOf<Entry>()
        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val date = resultSet.getString("date")
            //val photo = resultSet.getBytes("photo")
            val text = resultSet.getString("text")
            val plantName = resultSet.getString("plant_name")
            val reminderDays = resultSet.getString("reminder_days")
            entries.add(Entry(id, date, text, plantName, reminderDays))
        }
        return entries
    }
}

data class Entry(val id: Int, val date: String, val text: String, val plantName: String, val reminderDays: String)