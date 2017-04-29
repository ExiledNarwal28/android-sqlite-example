package net.info420.fabien.androidtravailpratique.helpers;

import org.joda.time.DateTime;
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
   * @param unixDate Date en millisecondes (UnixTime)
   * @return Date de format EEE d MMM
   */
  public static String getDate(int unixDate) {
    return DateTimeFormat.forPattern("EEEE d MMM").print(new DateTime().withMillis(unixDate * 10000L));
  }

  /**
   * Méthode statique pour obtenir une date de long format (EEEE d MMMM yyyy)
   *
   * @param unixDate Date en millisecondes (UnixTime)
   * @return Date de format EEE d MMMM yyyy
   */
  public static String getLongueDate(int unixDate) {
    return DateTimeFormat.forPattern("EEEE d MMMM yyyy").print(new DateTime().withMillis(unixDate * 10000L));
  }
}
