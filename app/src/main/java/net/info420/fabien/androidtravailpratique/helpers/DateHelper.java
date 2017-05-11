package net.info420.fabien.androidtravailpratique.helpers;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Classe contenant des méthodes pour facilier l'affichage de dates
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-27
 */
public class DateHelper {
  private static final String TAG = DateHelper.class.getName();

  /**
   * Méthode statique pour obtenir une date de petit format (EEEE d MMM)
   *
   * <ul>
   *   <li>Vérifie la Locale de l'{@link android.app.Application}, et renvoie la date</li>
   * </ul>
   *
   * @param   context   Context pour aller chercher la langue
   * @param   unixDate  Date en millisecondes (UnixTime)
   * @return  Date de format EEE d MMM
   *
   * @see PrefsHelper#getLangue(Context)
   * @see DateTimeFormat
   * @see DateTimeFormat#forPattern(String)
   * @see DateTime#withMillis(long)
   * @see StringHelper#capitalize(String)
   */
  public static String getDate(Context context, int unixDate) {
    if (PrefsHelper.getLangue(context).equals(PrefsHelper.PREFS_LANGUE_EN))
      return StringHelper.capitalize(DateTimeFormat.forPattern("EEEE, MMM d").print(new DateTime().withMillis(unixDate * 10000L)));

    return StringHelper.capitalize(DateTimeFormat.forPattern("EEEE d MMM").print(new DateTime().withMillis(unixDate * 10000L)));
  }

  /**
   * Méthode statique pour obtenir une date de long format (EEEE d MMMM yyyy)
   *
   * <ul>
   *   <li>Vérifie la Locale de l'{@link android.app.Application}, et renvoie la date</li>
   * </ul>
   *
   * @param   context   Context pour aller chercher la langue
   * @param   unixDate  Date en millisecondes (UnixTime)
   * @return  Date de format EEE d MMMM yyyy
   *
   * @see DateTimeFormat
   * @see DateTimeFormat#forPattern(String)
   * @see DateTime#withMillis(long)
   * @see StringHelper#capitalize(String)
   */
  public static String getLongueDate(Context context, int unixDate) {
    if (PrefsHelper.getLangue(context).equals(PrefsHelper.PREFS_LANGUE_EN)) {
      return StringHelper.capitalize(DateTimeFormat.forPattern("EEEE, MMM d, yyyy").print(new DateTime().withMillis(unixDate * 10000L)));
    }

    return StringHelper.capitalize(DateTimeFormat.forPattern("EEEE d MMMM yyyy").print(new DateTime().withMillis(unixDate * 10000L)));
  }

  /**
   * Méthode statique pour obtenir les millisecondes d'aujourd'hui
   *
   * @return String des millisecondes d'aujoud'hui
   *
   * @see LocalDate#toDateTime(LocalTime, DateTimeZone)
   * @see DateTime#getMillis()
   */
  public static String getAujourdhuiMillis() {
    return Long.toString(new LocalDate().toDateTime(LocalTime.MIDNIGHT, DateTimeZone.UTC).getMillis() / 10000);
  }

  /**
   * Méthode statique pour obtenir les millisecondes du lundi de la semaine
   *
   * @return String des millisecondes du lundi de la semaine
   *
   * @see LocalDate#toDateTime(LocalTime, DateTimeZone)
   * @see DateTime#getMillis()
   */
  public static String getLundiMillis() {
    return Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000);
  }

  /**
   * Méthode statique pour obtenir les millisecondes du dimanche de la semaine
   *
   * @return String des millisecondes du dimanche de la semaine
   *
   * @see LocalDate#toDateTime(LocalTime, DateTimeZone)
   * @see DateTime#getMillis()
   */
  public static String getDimancheMillis() {
    return Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.SUNDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000);
  }

  /**
   * Méthode statique pour obtenir les millisecondes du premier jour du mois
   *
   * @return String des millisecondes du premier jour du mois
   *
   * @see LocalDate#toDateTime(LocalTime, DateTimeZone)
   * @see DateTime#getMillis()
   */
  public static String getPremierJourDuMoisMillis() {
    return Long.toString(new LocalDateTime().dayOfMonth().withMinimumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000);
  }

  /**
   * Méthode statique pour obtenir les millisecondes du dernier jour du mois
   *
   * @return String des millisecondes du dernier jour du mois
   *
   * @see LocalDate#toDateTime(LocalTime, DateTimeZone)
   * @see DateTime#getMillis()
   */
  public static String getDernierJourDuMoisMillis() {
    return Long.toString(new LocalDateTime().dayOfMonth().withMaximumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000);
  }
}
