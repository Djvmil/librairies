package com.djamil.utils

import android.util.Log
import com.djamil.suntelecom.truetime.TrueTime
import org.joda.time.LocalDate
import org.joda.time.Months
import org.joda.time.Years
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 4/15/20
 */
class DateUtils {
    companion object{
        const val TAG = "DateUtils"
        const val FORMAT_ENGLISH_FULL = "yyyy-MM-dd HH:mm:ss"
        const val FORMAT_ENGLISH_MEDIUM = "dd-MM-yyyy HH:mm:ss"
        const val FORMAT_FRENCH_MEDIUM = "dd/MM/yyyy HH:mm"
        const val FORMAT_FRENCH_FULL = "dd/MM/yyyy HH:mm:ss"

        const val FORMAT_FULL_yyyy_MM_dd = "yyyy-MM-dd"
        const val FORMAT_FULL_yyyyMMdd = "yyyyMMdd"
        const val FORMAT_FULL_yyyyMMddHHmmss = "yyyyMMddHHmmss"
        const val FORMAT_FULL_yyMMddhhmmss = "yyMMddhhmmss"
        const val FORMAT_FULL_yyMMdd = "yyMMdd"
        val sdfIso = SimpleDateFormat(FORMAT_ENGLISH_FULL)

        fun strToDate(strDate: String?, format: String?): Date? {
            // Ex format -> dd/MM/yy hh:mm:ss

            val sdf = SimpleDateFormat(format)
            var d: Date? = null
            try {
                d = sdf.parse(strDate)
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                Log.d("strDate Exception", strDate!!)
                e.printStackTrace()
            }
            return d
        }

        fun currentDateToString(): String {
            // DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date())
            return sdfIso.format(Date())
        }

        fun currentDateToInteger(): Int {
            val dateToString = SimpleDateFormat(FORMAT_FULL_yyyyMMdd)
                    .format(Date())
            return Integer.valueOf(dateToString)
        }

        fun convertDateToInteger(date: Date): Int {
            val dateToString = SimpleDateFormat(FORMAT_FULL_yyyyMMddHHmmss)
                    .format(date)
            Log.i("dateToString =", "$dateToString, date = $date")
            return Integer.valueOf(dateToString)
        }


        fun getDateNow(): String? {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy") //new DateFormat("yyyy-MM-dd");
            val date = TrueTime.now()
            //   currentDate = currentDate.replace("/", "-");
            return dateFormat.format(date) as String
        }

        @JvmOverloads
        fun dateToString(date: Date?, format: String?, locale: Locale? = Locale.getDefault()): String {
            return try {
                if (date != null) {
                    SimpleDateFormat(format, locale).format(date).toString()
                } else {
                    ""
                }
            } catch (e: Exception) {
                ""
            }
        }

        fun formatDate(strDate: String?, inputFormat: String?, outputFormat: String?): String? {
            val sdIn = SimpleDateFormat(inputFormat)
            val sdOut = SimpleDateFormat(outputFormat)
            try {
                val date = sdIn.parse(strDate)
                return sdOut.format(date)
            } catch (e: ParseException) {
                Log.d("strDate Exception", strDate!!)
            }
            return null
        }

        fun dateForSequenceTransac(): String {
            return SimpleDateFormat(FORMAT_FULL_yyMMddhhmmss)
                    .format(Date())
        }

        fun dateToFormatyyMMdd(): String {
            return SimpleDateFormat(FORMAT_FULL_yyMMdd)
                    .format(Date())
        }

        // Calculer la difference en jours par rapport à 2 dates
        fun daysBetween(startDate: Calendar, endDate: Calendar?): Long {
            val date = startDate.clone() as Calendar
            var daysBetween: Long = 0
            while (date.before(endDate)) {
                date.add(Calendar.DAY_OF_MONTH, 1)
                daysBetween++
            }
            return daysBetween
        }

        fun monthBetweenStringDotation(arrayMonthName: Array<String?>?,
                                       startDate: Date?, endDate: Date?): String? {
            Log.d("DateUtils", "**** DEB monthBetweenStringDotation ***")
            if (startDate == null) {
                Log.e("DateUtils", "Paramétres startDate null !!")
                return null
            }
            if (endDate == null) {
                Log.e("DateUtils", "Paramétres endDate null !!")
                return null
            }
            if (arrayMonthName != null && arrayMonthName.size == 0) {
                Log.e("DateUtils", "Paramétres arrayMonthName null ou vide !!")
                return null
            }
            if (startDate.after(endDate)) {
                Log.e("DateUtils", "Paramétres startDate > endDate !!")
                return null
            }
            val moisDotation = StringBuffer("")
            val dateDerniereDotation = dateToCalendar(startDate)
            dateDerniereDotation[Calendar.DAY_OF_MONTH] = 1
            Log.d("dateDerniereDotation", "dateDerniereDotation == "
                    + dateDerniereDotation.time)
            val calEndDate = dateToCalendar(endDate)
            calEndDate[Calendar.DAY_OF_MONTH] = 1
            Log.d("calEndDate", "calEndDate == " + calEndDate.time)
            val monthList = iterateBetweenMonthDotation(
                    dateDerniereDotation.time, calEndDate.time)
            if (arrayMonthName != null && arrayMonthName.size > 0
                    && monthList != null && monthList.size > 0) {
                for (i in 0..monthList.size - 1) {
                    moisDotation.append(arrayMonthName[monthList[i]])
                    if (i < monthList.size - 1) {
                        moisDotation.append(", ")
                    } else {
                        moisDotation.append(".")
                    }
                }
            }
            Log.d("Mois dotation >> ", moisDotation.toString())
            Log.d("DateUtils", "**** END monthBetweenStringDotation ***")
            return moisDotation.toString()
        }

        fun dateToCalendar(date: Date?): Calendar {
            val cal = Calendar.getInstance()
            cal.time = date
            return cal
        }

        fun isThisDateValid(dateToValidate: String?,
                            dateFromat: String?): Boolean {
            if (dateToValidate == null) {
                return false
            }
            val sdf = SimpleDateFormat(dateFromat)
            sdf.isLenient = false
            try {
                // if not valid, it will throw ParseException
                val date = sdf.parse(dateToValidate)
                println(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return false
            }
            return true
        }

        fun changeDateFormat(strDate: String?, inputFormat: String?, outputFormat: String?): String {
            val inputDateFormat = SimpleDateFormat(inputFormat, Locale.FRANCE)
            val outputDateFormat = SimpleDateFormat(outputFormat, Locale.FRANCE)
            val result: String
            result = if (strDate == null || strDate.isEmpty()) {
                ""
            } else {
                try {
                    outputDateFormat.format(inputDateFormat.parse(strDate))
                } catch (e: ParseException) {
                    e.printStackTrace()
                    ""
                }
            }
            return result
        }

        // Ex dateDerniereTransaction = 01/08/2014, currentDate = 10/10/2014
        // Une Dotation aurait du être fait le 01/09/2014, une autre le 01/10/2014
        // seulement la carte n'a pas été présentée a une station qu'aprés le
        // 01/10/2014
        // Donc depuis la derniére dotation ça a fait plus de 60 jours donc
        fun getMonthName(month: String?, locale: Locale?): String? {
            val monthParse = SimpleDateFormat("MM", locale)
            val monthDisplay = SimpleDateFormat("MMMM", locale)
            var monthName: String? = null
            try {
                monthName = monthDisplay.format(monthParse.parse(month))
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return monthName
        }

        /**
         *
         * @param startDate
         * -> Date derniére dotation
         * @param endDate
         * -> Date d'aujourd'hui
         * @return
         */
        fun getMonthsBetweenDatesDotation(startDate: Date?, endDate: Date?): Int {
            Log.d("DateUtils", "*** DEB getMonthsBetweenDatesDotation ***")
            /**
             * - Date derniere dotation plus un mois. - Date derniére dotation à
             * 01/01/2015, le client se pointe au 21/05/2015 - La période de cumul
             * va donc du 01/02/2015 au 01/05/2015 soit 4 mois - Mois à prendre en
             * compte Fev, Mars, Avr, Mai
             */
            var monthsBetween: Int
            try {
                if (endDate == null) {
                    Log.e("DateUtils", "Paramétre endDate manquant !")
                    return 0
                }
                if (startDate == null) {
                    // Cette carte n'a jamais fait l'objet d'une premiére dotation
                    Log.e("DateUtils", "Paramétre startDate est null !")
                    return 1
                }
                if (startDate.after(endDate)) {
                    Log.e("DateUtils",
                            "Paramétre endDate doit être postérieure au parametre startDate !")
                    return 0
                }
                val startCalendar = Calendar.getInstance()
                startCalendar.time = startDate
                startCalendar[Calendar.DAY_OF_MONTH] = 1
                startCalendar.add(Calendar.MONTH, 1)
                val endCalendar = Calendar.getInstance()
                endCalendar.time = endDate
                endCalendar[Calendar.DAY_OF_MONTH] = 1
                val yearDiff = (endCalendar[Calendar.YEAR]
                        - startCalendar[Calendar.YEAR])
                monthsBetween = endCalendar[Calendar.MONTH]
                -startCalendar[Calendar.MONTH] + 12 * yearDiff
                if (endCalendar[Calendar.DAY_OF_MONTH] >= startCalendar[Calendar.DAY_OF_MONTH]) monthsBetween = monthsBetween + 1
            } catch (ex: Exception) {
                return 0
            }
            Log.d("DateUtils", "*** END getMonthsBetweenDatesDotation ***")
            Log.i("Mois Dotation count : ", monthsBetween.toString())
            return monthsBetween
        }

        fun getMonthsBetweenDates(startDate: Date?, endDate: Date?): Int {
            var monthsBetween: Int
            try {
                if (startDate == null) {
                    Log.e("DateUtils", "Paramétre startDate est null !")
                    return 0
                }
                if (endDate == null) {
                    Log.e("DateUtils", "Paramétre endDate manquant !")
                    return 0
                }
                if (startDate.after(endDate)) {
                    Log.e("DateUtils",
                            "Paramétre endDate doit être postérieure au parametre startDate !")
                    return 0
                }
                val startCalendar = Calendar.getInstance()
                startCalendar.time = startDate
                val endCalendar = Calendar.getInstance()
                endCalendar.time = endDate
                val yearDiff = (endCalendar[Calendar.YEAR]
                        - startCalendar[Calendar.YEAR])
                monthsBetween = endCalendar[Calendar.MONTH]
                -startCalendar[Calendar.MONTH] + 12 * yearDiff
                if (endCalendar[Calendar.DAY_OF_MONTH] >= startCalendar[Calendar.DAY_OF_MONTH]) monthsBetween = monthsBetween + 1
            } catch (ex: Exception) {
                return 0
            }
            return monthsBetween
        }

        fun iterateBetweenMonthDotation(
                startDate: Date, endDate: Date): ArrayList<Int> {
            val startCalemder = Calendar.getInstance()
            startCalemder.time = startDate
            startCalemder[Calendar.DAY_OF_MONTH] = 1
            startCalemder.add(Calendar.MONTH, 1)
            val endCalendar = Calendar.getInstance()
            endCalendar.time = endDate
            endCalendar.time = endDate
            endCalendar[Calendar.DAY_OF_MONTH] = 1
            val monthsList = ArrayList<Int>()
            while (startCalemder.compareTo(endCalendar) <= 0) {
                Log.i("DATE >> ", "" + startCalemder.time.toString())
                // write you main logic here
                monthsList.add(startCalemder.time.month)
                startCalemder.add(
                        Calendar.MONTH, 1)
            }
            Log.i(TAG, "monthsList >> startDate = [$startDate], endDate = [$endDate], Month = [$monthsList]")
            return monthsList
        } // End fonction

        fun iterateBetweenMonth(startDate: Date,
                                endDate: Date): ArrayList<Int> {
            val startCalemder = Calendar.getInstance()
            startCalemder.time = startDate
            val endCalendar = Calendar.getInstance()
            endCalendar.time = endDate
            val monthsList = ArrayList<Int>()
            while (startCalemder.compareTo(endCalendar) <= 0) {
                Log.i("DATE >> ", "" + startCalemder.time.toString())
                // write you main logic here
                monthsList.add(startCalemder.time.month)
                startCalemder.add(
                        Calendar.MONTH, 1)
            }
            Log.i(TAG, " monthsList >> startDate = [$startDate], endDate = [$endDate], Month = [$monthsList]")
            return monthsList
        } // End fonction

        fun isDateBetween(dateStart: Date?, dateEnd: Date?,
                          dateInQuestion: Date?): Boolean {
            requireNotNull(dateStart) { "La date de début ne doit pas être null" }
            requireNotNull(dateEnd) { "La date de fin ne doit pas être null" }
            requireNotNull(dateInQuestion) { "La date à vérifier si compris dans l'intervalle ne doit pas être null" }
            return (dateStart.compareTo(dateInQuestion) * dateInQuestion.compareTo(dateEnd)) > 0
        }

        fun addDays(date: Date?, days: Int): String? {
            var dateResult: String? = null
            val calendar = Calendar.getInstance()

            //calendar.setTime(Objects.requireNonNull(sdf.parse(dateDay)));
            calendar.time = date
            calendar.add(Calendar.DATE, days) // number of days to add
            dateResult = SimpleDateFormat(FORMAT_FULL_yyyy_MM_dd).format(calendar.time) // dt is now the new date
            Log.i("Utils.addDays: ", dateResult)
            return dateResult
        }

        val currentTimeStamp: String
            get() = SimpleDateFormat(FORMAT_FULL_yyyy_MM_dd).format(Date())

        fun stringToDate(dateString: String, patern: String = "dd-MM-yyyy HH:mm:ss"): Date? {

            return try {
                SimpleDateFormat(patern).parse(dateString)
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }


        //Convert Calendar to Date
        private fun calendarToDate(calendar: Calendar): Date? {
            return calendar.time
        }


        fun dateInBetweenDates(dateSearchValue: Date?, startDateValue: Date?, endDateValue: Date?): Boolean {
            return try {
                dateSearchValue?.after(startDateValue!!)!! && dateSearchValue.before(endDateValue!!)
            }catch (e: Exception){
                e.printStackTrace()
                false
            }
        }

        fun yearsBetweenDates(startDateValue: Date?, endDateValue: Date?): Int {
            val start = LocalDate(dateToCalendar(startDateValue).get(Calendar.YEAR), dateToCalendar(startDateValue).get(Calendar.MONTH), dateToCalendar(startDateValue).get(Calendar.DAY_OF_MONTH))
            val now =  LocalDate(dateToCalendar(endDateValue).get(Calendar.YEAR), dateToCalendar(endDateValue).get(Calendar.MONTH), dateToCalendar(endDateValue).get(Calendar.DAY_OF_MONTH))

            return Years.yearsBetween(start, now).years
        }

        fun monthsBetweenDates(startDateValue: Date?, endDateValue: Date?): Int {
            val start = LocalDate(dateToCalendar(startDateValue).get(Calendar.YEAR), dateToCalendar(startDateValue).get(Calendar.MONTH), dateToCalendar(startDateValue).get(Calendar.DAY_OF_MONTH))
            val now =  LocalDate(dateToCalendar(endDateValue).get(Calendar.YEAR), dateToCalendar(endDateValue).get(Calendar.MONTH), dateToCalendar(endDateValue).get(Calendar.DAY_OF_MONTH))

            return Months.monthsBetween(start, now).months
        }

        fun daysBetweenDates(startDateValue: Date?, endDateValue: Date?): Long {

            val diff: Long = endDateValue?.time!! - startDateValue?.time!!

            return  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        }

        fun minutesBetweenDates(startDateValue: Date?, endDateValue: Date?): Long {

            val diff: Long = endDateValue?.time!! - startDateValue?.time!!

            return  TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
        }

        fun secondsBetweenDates(startDateValue: Date?, endDateValue: Date?): Long {

            val diff: Long = endDateValue?.time!! - startDateValue?.time!!

            return  TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS)
        }

        fun millisecondsBetweenDates(startDateValue: Date?, endDateValue: Date?): Long {

            val diff: Long = endDateValue?.time!! - startDateValue?.time!!

            return  TimeUnit.MILLISECONDS.convert(diff, TimeUnit.MILLISECONDS)
        }

    }

}