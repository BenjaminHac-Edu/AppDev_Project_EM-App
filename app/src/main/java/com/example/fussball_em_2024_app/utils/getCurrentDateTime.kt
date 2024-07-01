package com.example.fussball_em_2024_app.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateFormater{
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime() : Date {
        return Calendar.getInstance().time
    }

    // Funktion zum Konvertieren des Datums in String
    fun formatDate(date: Date?): String {
        return if(date != null) {
            SimpleDateFormat("dd.MM.yy \n'um' HH:mm 'Uhr'", Locale.GERMANY).format(date)
        } else {
            "Datum unbekannt"
        }
    }

    fun formatDateToParsable(date: Date): String{
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.UK).format(date)
    }

    fun isDateAfterNow(utcDate: Date): Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateToCheck = LocalDateTime.parse(utcDate.toString("yyyy-MM-dd'T'HH:mm:ss"))
            val nowUTC = LocalDateTime.now(ZoneId.of("UTC"))
            return nowUTC.isAfter(dateToCheck)
        }
        val dateToCheck2 = formatDateToParsable(utcDate)
        val currentUTC = formatDateToParsable(Date())
        return currentUTC > dateToCheck2
    }

}

