package com.djamil.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Djvmil_ on 4/27/20
 */

@SuppressLint("SimpleDateFormat")
public class DateUtils {

	public static String FORMAT_ENGLISH_FULL = "yyyy-MM-dd HH:mm:ss";
	public static String FORMAT_ENGLISH_MEDIUM = "dd-MM-yyyy HH:mm:ss";
	public static String FORMAT_FRENCH_MEDIUM = "dd/MM/yyyy HH:mm";
	public static String FORMAT_FRENCH_FULL = "dd/MM/yyyy HH:mm:ss";

	private static SimpleDateFormat sdfIso = new SimpleDateFormat(FORMAT_ENGLISH_FULL);

	public static Date strToDate(String strDate, String format) {
		// Ex format -> dd/MM/yy hh:mm:ss
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.d("strDate Exception", strDate);
			e.printStackTrace();
		}
		return d;
	}

	public static String currentDateToString() {
		// DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date())
		return sdfIso.format(new Date());
	}

	public static Integer currentDateToInteger() {
		String dateToString = new SimpleDateFormat("yyyyMMdd")
				.format(new Date());
		int dateToInteger = Integer.valueOf(dateToString);

		return dateToInteger;
	}

	public static Integer convertDateToInteger(Date date) {
		String dateToString = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(date);
		Log.i("dateToString =", dateToString + ", date = " + date);
		Integer dateToInteger = Integer.valueOf(dateToString);
		return dateToInteger;
	}

	public static String dateToString(Date date, String format, Locale locale) {
		try {
			if (date != null) {
				return String.valueOf(new SimpleDateFormat(format, locale).format(date));
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}

	}

	public static String dateToString(Date date, String format) {
		return dateToString(date, format, Locale.getDefault());
	}

	public static String formatDate(String strDate, String inputFormat, String outputFormat) {
		SimpleDateFormat sdIn = new SimpleDateFormat(inputFormat);
		SimpleDateFormat sdOut = new SimpleDateFormat(outputFormat);
		try {
			Date date = sdIn.parse(strDate);
			return sdOut.format(date);
		} catch (ParseException e) {
			Log.d("strDate Exception", strDate);
		}
		return null;
	}

	public static String dateForSequenceTransac() {
		String dateToString = new SimpleDateFormat("yyMMddhhmmss")
				.format(new Date());
		return dateToString;
	}

	public static String dateToFormatyyMMdd() {
		String dateToString = new SimpleDateFormat("yyMMdd")
				.format(new Date());
		return dateToString;
	}

	// Calculer la difference en jours par rapport à 2 dates
	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	public static String monthBetweenStringDotation(String[] arrayMonthName,
													Date startDate, Date endDate) {
		Log.d("DateUtils", "**** DEB monthBetweenStringDotation ***");

		if (startDate == null) {
			Log.e("DateUtils", "Paramétres startDate null !!");
			return null;
		}

		if (endDate == null) {
			Log.e("DateUtils", "Paramétres endDate null !!");
			return null;
		}

		if (arrayMonthName != null && arrayMonthName.length == 0) {
			Log.e("DateUtils", "Paramétres arrayMonthName null ou vide !!");
			return null;
		}

		if (startDate.after(endDate)) {
			Log.e("DateUtils", "Paramétres startDate > endDate !!");
			return null;
		}

		StringBuffer moisDotation = new StringBuffer("");
		Calendar dateDerniereDotation = DateUtils.dateToCalendar(startDate);
		dateDerniereDotation.set(Calendar.DAY_OF_MONTH, 1);
		Log.d("dateDerniereDotation", "dateDerniereDotation == "
				+ dateDerniereDotation.getTime());

		Calendar calEndDate = DateUtils.dateToCalendar(endDate);
		calEndDate.set(Calendar.DAY_OF_MONTH, 1);
		Log.d("calEndDate", "calEndDate == " + calEndDate.getTime());

		ArrayList<Integer> monthList = iterateBetweenMonthDotation(
				dateDerniereDotation.getTime(), calEndDate.getTime());
		if ((arrayMonthName != null && arrayMonthName.length > 0)
				&& (monthList != null && monthList.size() > 0)) {
			for (int i = 0; i <= monthList.size() - 1; i++) {
				moisDotation.append(arrayMonthName[monthList.get(i)]);
				if (i < monthList.size() - 1) {
					moisDotation.append(", ");
				} else {
					moisDotation.append(".");
				}
			}
		}

		Log.d("Mois dotation >> ", moisDotation.toString());
		Log.d("DateUtils", "**** END monthBetweenStringDotation ***");

		return moisDotation.toString();
	}

	public static Calendar dateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static boolean isThisDateValid(String dateToValidate,
										  String dateFromat) {
		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try {
			// if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String changeDateFormat(String strDate, String inputFormat, String outputFormat) {
		SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat, Locale.FRANCE);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat, Locale.FRANCE);

		String result;
		if(strDate == null || strDate.isEmpty()) {
			result = "";
		}
		else  {

			try {
				result = outputDateFormat.format(inputDateFormat.parse(strDate));
			}
			catch (ParseException e) {
				e.printStackTrace();
				result = "";
			}
		}
		return result;
	}

	// Ex dateDerniereTransaction = 01/08/2014, currentDate = 10/10/2014
	// Une Dotation aurait du être fait le 01/09/2014, une autre le 01/10/2014
	// seulement la carte n'a pas été présentée a une station qu'aprés le
	// 01/10/2014
	// Donc depuis la derniére dotation ça a fait plus de 60 jours donc

	public static String getMonthName(String month, Locale locale) {
		SimpleDateFormat monthParse = new SimpleDateFormat("MM", locale);
		SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM", locale);
		String monthName = null;
		try {
			monthName = monthDisplay.format(monthParse.parse(month));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return monthName;
	}

	/**
	 *
	 * @param startDate
	 *            -> Date derniére dotation
	 * @param endDate
	 *            -> Date d'aujourd'hui
	 * @return
	 */

	public static int getMonthsBetweenDatesDotation(Date startDate, Date endDate) {
		Log.d("DateUtils", "*** DEB getMonthsBetweenDatesDotation ***");

		/**
		 * - Date derniere dotation plus un mois. - Date derniére dotation à
		 * 01/01/2015, le client se pointe au 21/05/2015 - La période de cumul
		 * va donc du 01/02/2015 au 01/05/2015 soit 4 mois - Mois à prendre en
		 * compte Fev, Mars, Avr, Mai
		 **/

		int monthsBetween;
		try {

			if (endDate == null) {
				Log.e("DateUtils", "Paramétre endDate manquant !");
				return 0;
			}

			if (startDate == null) {
				// Cette carte n'a jamais fait l'objet d'une premiére dotation
				Log.e("DateUtils", "Paramétre startDate est null !");
				return 1;
			}

			if (startDate.after(endDate)) {
				Log.e("DateUtils",
						"Paramétre endDate doit être postérieure au parametre startDate !");
				return 0;
			}

			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.add(Calendar.MONTH, 1);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);
			endCalendar.set(Calendar.DAY_OF_MONTH, 1);

			int yearDiff = endCalendar.get(Calendar.YEAR)
					- startCalendar.get(Calendar.YEAR);
			monthsBetween = endCalendar.get(Calendar.MONTH)
					- startCalendar.get(Calendar.MONTH) + 12 * yearDiff;

			if (endCalendar.get(Calendar.DAY_OF_MONTH) >= startCalendar
					.get(Calendar.DAY_OF_MONTH))
				monthsBetween = monthsBetween + 1;
		} catch (Exception ex) {
			return 0;
		}

		Log.d("DateUtils", "*** END getMonthsBetweenDatesDotation ***");

		Log.i("Mois Dotation count : ", String.valueOf(monthsBetween));
		return monthsBetween;
	}

	public static int getMonthsBetweenDates(Date startDate, Date endDate) {
		int monthsBetween;
		try {

			if (startDate == null) {
				Log.e("DateUtils", "Paramétre startDate est null !");
				return 0;
			}

			if (endDate == null) {
				Log.e("DateUtils", "Paramétre endDate manquant !");
				return 0;
			}

			if (startDate.after(endDate)) {
				Log.e("DateUtils",
						"Paramétre endDate doit être postérieure au parametre startDate !");
				return 0;
			}

			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);

			int yearDiff = endCalendar.get(Calendar.YEAR)
					- startCalendar.get(Calendar.YEAR);
			monthsBetween = endCalendar.get(Calendar.MONTH)
					- startCalendar.get(Calendar.MONTH) + 12 * yearDiff;

			if (endCalendar.get(Calendar.DAY_OF_MONTH) >= startCalendar
					.get(Calendar.DAY_OF_MONTH))
				monthsBetween = monthsBetween + 1;
		} catch (Exception ex) {
			return 0;
		}

		return monthsBetween;
	}

	public static ArrayList<Integer> iterateBetweenMonthDotation(
			Date startDate, Date endDate) {
		Calendar startCalemder = Calendar.getInstance();
		startCalemder.setTime(startDate);
		startCalemder.set(Calendar.DAY_OF_MONTH, 1);
		startCalemder.add(Calendar.MONTH, 1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		endCalendar.setTime(endDate);
		endCalendar.set(Calendar.DAY_OF_MONTH, 1);

		ArrayList<Integer> monthsList = new ArrayList<Integer>();
		for (; startCalemder.compareTo(endCalendar) <= 0; startCalemder.add(
				Calendar.MONTH, 1)) {
			Log.i("DATE >> ", "" + startCalemder.getTime().toString());
			// write you main logic here
			monthsList.add(startCalemder.getTime().getMonth());
		}
		Log.i("monthsList >> startDate = [" + startDate + "], endDate = ["
				+ endDate + "]", "Month = [" + monthsList.toString() + "]");
		return monthsList;
	}// End fonction

	public static ArrayList<Integer> iterateBetweenMonth(Date startDate,
														 Date endDate) {
		Calendar startCalemder = Calendar.getInstance();
		startCalemder.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		ArrayList<Integer> monthsList = new ArrayList<Integer>();
		for (; startCalemder.compareTo(endCalendar) <= 0; startCalemder.add(
				Calendar.MONTH, 1)) {
			Log.i("DATE >> ", "" + startCalemder.getTime().toString());
			// write you main logic here
			monthsList.add(startCalemder.getTime().getMonth());
		}
		Log.i("monthsList >> startDate = [" + startDate + "], endDate = ["
				+ endDate + "]", "Month = [" + monthsList.toString() + "]");
		return monthsList;
	}// End fonction

	public static boolean isDateBetween(Date dateStart, Date dateEnd,
										Date dateInQuestion) {
		if (dateStart == null) {
			throw new IllegalArgumentException(
					"La date de début ne doit pas être null");
		}

		if (dateEnd == null) {
			throw new IllegalArgumentException(
					"La date de fin ne doit pas être null");
		}

		if (dateInQuestion == null) {
			throw new IllegalArgumentException(
					"La date à vérifier si compris dans l'intervalle ne doit pas être null");
		}
		return dateStart.compareTo(dateInQuestion)
				* dateInQuestion.compareTo(dateEnd) > 0;
	}


	public static String addDays(Date date, int days)
	{
		String dateResult = null;
		Calendar calendar = Calendar.getInstance();

		//calendar.setTime(Objects.requireNonNull(sdf.parse(dateDay)));
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);  // number of days to add
		dateResult = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());  // dt is now the new date
		Log.i("Utils.addDays: ", dateResult );

		return dateResult;
	}

	public static String getCurrentTimeStamp() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
}
