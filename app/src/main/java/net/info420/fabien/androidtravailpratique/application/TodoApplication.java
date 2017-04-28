package net.info420.fabien.androidtravailpratique.application;

import android.app.Application;
import android.content.ContentValues;

import net.info420.fabien.androidtravailpratique.data.DBHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

import org.joda.time.DateTime;

// TODO : Ajouter l'organisation des fichiers dans la doc http://blog.smartlogic.io/2013-07-09-organizing-your-android-development-code-structure

// TODO : Traduire, documenter et parfaire les fichiers suivants :
//
//    JAVA
//
//      net.info420.fabien.androidtravailpratique
//
//        ACTIVITIES
//          [X]--ModifierEmployeActivity--(Fait)--<==<<
//          [X]--ModifierTacheActivity----(Fait)--<==<<
//          [X]--EmployeActivity----------(Fait)--<==<<
//          [X]--AjouterEmployeActivity---(Fait)--<==<<
//          [X]--AjouterTacheActivity-----(Fait)--<==<<
//          [X]--PrincipaleActivity-------(Fait)--<==<<
//          [X]--TacheActivity------------(Fait)--<==<<
//
//        ADAPTERS
//          [ ]--EmployeAdapter
//          [ ]--TacheAdapter
//
//        APPLICATION
//          [ ]--TodoApplication
//
//        DATA
//          [ ]--DBHelper
//          [ ]--TodoContentProvider
//
//        FRAGMENTS
//          [X]--DatePickerFragment-----(Fait    )--<==<<
//          [X]--EmployesListeFragment--(Fait    )--<==<<
//          [ ]--PrefsFragment
//          [ ]--TachesListeFragment----(En cours)--<==<<
//          [ ]--TacheFragment
//          [ ]--MettreAJourEmployeFragment
//          [ ]--MettreAJourTacheFragment
//
//        HELPERS
//          [ ]--ColorHelper
//          [ ]--DateHelper
//          [ ]--LocaleHelper
//          [ ]--StringHelper
//
//        INTERFACES
//         [ ]--OnTacheDateChangeListener
//
//        MODELS
//          [ ]--Employe
//          [ ]--Tache
//
//        UTILS
//          [ ]--TempsReceiver
//          [ ]--TempsService
//
//    res
//
//        LAYOUT
//          [ ]--activity_edit_employee.xml
//          [ ]--activity_edit_task.xml
//          [ ]--activity_employee.xml
//          [ ]--activity_main.xml
//          [ ]--activity_new_employee.xml
//          [ ]--activity_new_task.xml
//          [ ]--activity_task.xml
//          [ ]--employee_row.xml
//          [ ]--fragment_employee_list.xml
//          [ ]--fragment_task.xml
//          [ ]--fragment_task_list.xml
//          [ ]--fragment_update_employee.xml
//          [ ]--fragment_update_task.xml
//          [ ]--task_row.xml
//          [ ]--toolbar.xml
//
//        MENU
//          [ ]--menu_employee_list.xml
//          [ ]--menu_item.xml
//          [ ]--menu_prefs.xml
//          [ ]--menu_taches_liste.xml
//          [ ]--menu_update_item.xml
//
//        VALUES
//          [ ]--attrs.xml
//          [ ]--colors.xml
//          [ ]--dimens.xml
//          [ ]--strings.xml
//          [ ]--styles.xml
//
//        VALUES
//          [ ]--attrs.xml
//          [ ]--colors.xml
//          [ ]--dimens.xml
//          [ ]--strings.xml
//          [ ]--styles.xml
//
//        XML
//          [ ]--prefs_items.xml
//
//    [ ]--------AndroidManifest.xml

/**
 * L'application de base
 * Cette classe est le point d'entrée dans l'application Android.
 * Elle contient des variables utilisées partout dans l'application.
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-03-26
 */
public class TodoApplication extends Application {
  private static final String TAG = TodoApplication.class.getName();

  // Les noms des préférences
  public static final String PREFS_TOASTS             = "toasts";
  public static final String PREFS_TOASTS_FREQUENCE   = "toasts_frequence";
  public static final String PREFS_TOASTS_LAPS_TEMPS  = "toasts_laps_temps";
  public static final String PREFS_TOASTS_URGENCE     = "toasts_urgence";
  public static final String PREFS_LANGUE             = "langue";

  // TODO : Prod : Enlever ceci
  // Variables de développement
  public boolean  recreationDb         = false;
  public boolean  creationTestTaches   = false;
  public boolean  creationTestEmployes = false;

  /**
   * S'exécute lors de la création de la classe
   */
  @Override
  public void onCreate() {
    super.onCreate();

    // Pour le développement, recréation de la base de données
    if (recreationDb)
      recreerDb();

    if (creationTestTaches)
      creerTestTaches();

    if (creationTestEmployes)
      creerTestEmployes();
  }

  // TODO : Enlever les méthodes de développement que la base de données fonctionne
  private void recreerDb() {
    DBHelper dbHelper = new DBHelper(this);
    dbHelper.recreateDB(dbHelper.getWritableDatabase());
  }

  private void creerTestTaches() {
    String[]  taskNames           = { "Test0",                                    "Test1",                                                      "Test2",                                                      "Test3" };
    String[]  taskDescriptions    = { "Description0",                             "Description1",                                               "Description2",                                               "Description3" };
    Boolean[] taskCompleteds      = { false,                                      false,                                                        true,                                                         false };
    int[]     taskDates           = { (int) (new DateTime().getMillis() / 10000), (int) (new DateTime(2017, 4, 20, 0, 0).getMillis() / 10000),  (int) (new DateTime(2017, 4, 22, 0, 0).getMillis() / 10000),  (int) (new DateTime(2017, 4, 28, 0, 0).getMillis() / 10000) };
    int[]     taskUrgencyLevels   = { 0,                                          2,                                                            1,                                                            0 };

    for (int i = 0; i < taskNames.length; i++) {
      ContentValues values = new ContentValues();

      // La tâche #4 n'a pas d'employé assigné (pour des tests)
      if (i <= 2) {
        values.put(Tache.KEY_employe_assigne_ID, i + 1); // Employés auto-généré (en bas)
      } else {
        values.putNull(Tache.KEY_employe_assigne_ID);
      }

      values.put(Tache.KEY_nom,                 taskNames[i]);
      values.put(Tache.KEY_description,          taskDescriptions[i]);
      values.put(Tache.KEY_fait,            taskCompleteds[i]);
      values.put(Tache.KEY_date,                 taskDates[i]);
      values.put(Tache.KEY_urgence,        taskUrgencyLevels[i]);
    }
  }

  private void creerTestEmployes() {
    String[] employeeNames  = {"Fabien Roy",            "William Leblanc",      "Jean-Sébastien Giroux"};
    String[] employeeJobs   = {"Programmeur-analyste",  "PDG de BlazeIt inc.",  "Icône de l'Internet"};
    String[] employeeEmails = {"fabien@cognitio.ca",    "william@blazeit.org",  "giroux@twitch.com"};
    String[] employeePhones = {"418-409-6568",          "420-420-4242",         "123-456-7890"};

    for (int i = 0; i < employeeNames.length; i++) {
      ContentValues values = new ContentValues();

      values.put(Employe.KEY_nom, employeeNames[i]);
      values.put(Employe.KEY_poste, employeeJobs[i]);
      values.put(Employe.KEY_email, employeeEmails[i]);
      values.put(Employe.KEY_telephone, employeePhones[i]);
    }
  }
}
