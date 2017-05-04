package net.info420.fabien.androidtravailpratique.utils;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.application.TodoApplication;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.models.Tache;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@link Service} pour construire le message de {@link TempsReceiver}
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-04-24
 *
 * @see Tache
 * @see TempsReceiver
 * @see TodoApplication
 *
 * @see <a href="http://www.vogella.com/tutorials/AndroidServices/article.html"
 *      target="_blank">
 *      Source : Services d'Android</a>
 */
public class TempsService extends Service {
  public static final String TAG = TempsService.class.getName();

  // Données necéssaires à de TempsReceiver et son Bundle
  public static final String NOTIFICATION = TempsReceiver.class.getCanonicalName();
  public static final String TACHES_NB    = "tachesNb";
  public static final String LAPS_TEMPS   = "lapsTemps";
  public static final String URGENCE      = "urgence";
  public static final String AFFICHAGE    = "affichage";

  // Variables pour aller chercher le nombre de tâches
  private boolean           toastsActive    = true;
  private String            selection       = null;
  private ArrayList<String> selectionArgs   = new ArrayList<>();
  private int               frequence       = 0;
  private int               affichage       = 0;
  private String            lapsTempsTexte  = null;
  private String            urgenceTexte    = null;

  /**
   * Commande exécuté au démarrage du {@link Service}
   *
   * <ul>
   *  <li>Va chercher les informations des préférences</li>
   *  <li>Crée un {@link Timer} avec un {@link TimerTask} qui envoie un broadcast à {@link TempsReceiver}</li>
   *  <li>Suite au {@link Timer}, redémarre le {@link Service}</li>
   * </ul>
   *
   * @param   intent  {@link Intent} contenant le {@link android.os.Bundle}
   * @param   flags   Flags du {@link Service}
   * @param   startId Id du {@link Service}
   * @return  boolean qui détermine le comportement du service
   */
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    getInfoFromPrefs();

    // Si la fréquence est "jamais", on ne veut pas starter de Timer
    if (frequence == 0) {
      return Service.START_STICKY; // Ceci permet de redémarrer le service s'il est terminé
    }

    if (toastsActive) {
      Timer timer         = new Timer();
      TimerTask timeTask  = new TempsTask();

      timer.scheduleAtFixedRate(timeTask, 0, frequence * 1000); // Ceci s'exécute, puis attend X secondes avant de continuer le programme
    }

    return Service.START_STICKY; // Ceci permet de redémarrer le service s'il est terminé
  }

  /**
   * S'exécute lorsque bindé
   *
   * @param   intent  {@link Intent} contenant le {@link android.os.Bundle}
   * @return  {@link IBinder}
   */
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  /**
   * Va chercher les informations depuis les préférences
   *
   * <ul>
   *  <li>Place les informations des préférences dans les variables</li>
   *  <li>Construit la sélection en fonction des préférences</li>
   *  <li>Ajoute les textes en fonction des préférences</li>
   * </ul>
   *
   * <p>Infos à aller chercher : </p>
   *
   * <ul>
   *  <li>Booléen pour l'activation des toasts</li>
   *  <li>Laps de temps (aujourd'hui, semaine, mois)</li>
   *  <li>Temps d'affichage des Toasts</li>
   *  <li>Niveau d'urgence minimum</li>
   *  <li>Fréquence des notifications</li>
   * </ul>
   *
   * @see SharedPreferences
   * @see net.info420.fabien.androidtravailpratique.data.TodoContentProvider
   *
   * @see <a href="http://stackoverflow.com/questions/21820031/getting-value-from-edittext-preference-in-preference-screen"
   *      target="_blank">
   *      Source : Aller chercher les données des préférences</a>
   */
  private void getInfoFromPrefs() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    toastsActive  = prefs.getBoolean(TodoApplication.PREFS_TOASTS, true);
    frequence     = Integer.parseInt(prefs.getString( TodoApplication.PREFS_TOASTS_FREQUENCE,
                                                      TodoApplication.PREFS_TOASTS_FREQUENCE_DEFAUT));

    affichage = (Integer.parseInt(prefs.getString( TodoApplication.PREFS_TOASTS_AFFICHAGE,
                                                   TodoApplication.PREFS_TOASTS_AFFICHAGE_DEFAUT)) == 0) ?
                Toast.LENGTH_SHORT : Toast.LENGTH_LONG;

    // On vide les arguments de sélection
    selectionArgs.removeAll(selectionArgs);

    // Laps de temps
    switch (Integer.parseInt(prefs.getString( TodoApplication.PREFS_TOASTS_LAPS_TEMPS,
                                              TodoApplication.PREFS_TOATS_LAPS_TEMPS_DEFAUT))) {
      case 0:
        // Aujourd'hui

        selection = "(" + Tache.KEY_date + " =?) AND (" + Tache.KEY_urgence + " >=?) AND (" + Tache.KEY_fait + " =?)";

        selectionArgs.add(Long.toString(new LocalDate().toDateTime(LocalTime.MIDNIGHT, DateTimeZone.UTC).getMillis() / 10000));

        lapsTempsTexte = getString(R.string.info_a_faire_aujoudhui);
        break;
      case 1:
        // Semaine

        selection = "(" + Tache.KEY_date + " BETWEEN ? AND ?) AND (" + Tache.KEY_urgence + " >=?) AND (" + Tache.KEY_fait + " =?)";

        selectionArgs.add(Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));
        selectionArgs.add(Long.toString(new LocalDateTime().withDayOfWeek(DateTimeConstants.SUNDAY).toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));

        lapsTempsTexte = getString(R.string.info_a_faire_cette_semaine);
        break;
      case 2:
        // Mois

        selection = "(" + Tache.KEY_date + " BETWEEN ? AND ?) AND (" + Tache.KEY_urgence + " >=?) AND (" + Tache.KEY_fait + " =?)";

        selectionArgs.add(Long.toString(new LocalDateTime().dayOfMonth().withMinimumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));
        selectionArgs.add(Long.toString(new LocalDateTime().dayOfMonth().withMaximumValue().toDateTime(DateTimeZone.getDefault()).getMillis() / 10000));

        lapsTempsTexte = getString(R.string.info_a_faire_ce_mois);
        break;
    }

    // On va ensuite chercher le texte qui décrit le niveau d'urgence
    switch (Integer.parseInt(prefs.getString( TodoApplication.PREFS_TOASTS_URGENCE,
                                              TodoApplication.PREFS_TOATS_URGENCE_DEFAUT))) {
      case 0:
        urgenceTexte = getString(R.string.info_urgence_bas_et_plus);
        break;
      case 1:
        urgenceTexte = getString(R.string.info_urgence_moyen_et_plus);
        break;
      case 2:
        urgenceTexte = getString(R.string.info_urgence_haut);
        break;
    }

    // Pour afficher un petit descriptif du genre : (bas et +)
    selectionArgs.add(prefs.getString(TodoApplication.PREFS_TOASTS_URGENCE,
                                      TodoApplication.PREFS_TOATS_URGENCE_DEFAUT)); // Ajout du niveau d'urgence minimum pour recevcoir les Toasts
    selectionArgs.add(Integer.toString(0)); // Ajout du niveau de complétion (tâches non-complétées)
  }

  /**
   * Retourne le nombre de tâches en fonction du niveau d'urgence minimum et de la période de temps
   *
   * <ul>
   *  <li>Construit un {@link android.database.Cursor} avec la sélection</li>
   *  <li>Va chercher le nombre de rangée dans la sélection</li>
   * </ul>
   *
   * @return le nombre de tâches, en fonction de la sélection
   *
   * @see net.info420.fabien.androidtravailpratique.data.TodoContentProvider
   */
  public int getTachesNb() {
    Cursor cursor = getContentResolver().query( TodoContentProvider.CONTENT_URI_TACHE,
                                                new String[] { Tache.KEY_ID },
                                                selection,
                                                (selection != null) ? selectionArgs.toArray(new String[selectionArgs.size()]) : null,
                                                null);

    return (cursor != null) ? cursor.getCount() : 0;
  }

  // Ce qui sera exécuté chaque X seconde

  /**
   * {@link TimerTask} avec la commande à exécuté à chaque fréquence
   *
   * <ul>
   *  <li>Construit un {@link Intent} avec les informations des préférences</li>
   *  <li>Envoie un broadcast à {@link TempsReceiver}</li>
   * </ul>
   *
   * @see TempsReceiver
   */
  private class TempsTask extends TimerTask {
    public void run() {
      Intent timeIntent = new Intent(NOTIFICATION);

      timeIntent.putExtra(TACHES_NB,  getTachesNb());   // Nombre de tâche
      timeIntent.putExtra(LAPS_TEMPS, lapsTempsTexte);  // Texte en lien avec la période de temps
      timeIntent.putExtra(URGENCE,    urgenceTexte);    // Texte en lien avec le niveau d'urgence
      timeIntent.putExtra(AFFICHAGE,  affichage);       // Longueur de l'affichage du Toast

      sendBroadcast(timeIntent);
    }
  }
}