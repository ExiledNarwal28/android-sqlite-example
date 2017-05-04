package net.info420.fabien.androidtravailpratique.application;

import android.app.Application;

// TODO : DOC : Ajouter l'organisation des fichiers dans la doc http://blog.smartlogic.io/2013-07-09-organizing-your-android-development-code-structure

// ***************************************************************
// *                                                             *
// *                  INDEX DE LA DOCUMENTATION                  *
// *                                                             *
// ***************************************************************
//
//    JAVA
//
//      net.info420.fabien.androidtravailpratique
//
//        ACTIVITIES
//          [X]--ModifierEmployeActivity-------------(Fait)--<==<<
//          [X]--ModifierTacheActivity---------------(Fait)--<==<<
//          [X]--EmployeActivity---------------------(Fait)--<==<<
//          [X]--AjouterEmployeActivity--------------(Fait)--<==<<
//          [X]--AjouterTacheActivity----------------(Fait)--<==<<
//          [X]--PrincipaleActivity------------------(Fait)--<==<<
//          [X]--TacheActivity-----------------------(Fait)--<==<<
//
//        ADAPTERS
//          [X]--EmployeAdapter----------------------(Fait)--<==<<
//          [X]--TacheAdapter------------------------(Fait)--<==<<
//
//        APPLICATION
//          [X]--TodoApplication---------------------(Fait)--<==<<
//
//        DATA
//          [X]--DBHelper----------------------------(Fait)--<==<<
//          [X]--TodoContentProvider-----------------(Fait)--<==<<
//
//        FRAGMENTS
//          [X]--DatePickerFragment------------------(Fait)--<==<<
//          [X]--EmployesListeFragment---------------(Fait)--<==<<
//          [X]--PrefsFragment-----------------------(Fait)--<==<<
//          [X]--TachesListeFragment-----------------(Fait)--<==<<
//          [X]--MettreAJourEmployeFragment----------(Fait)--<==<<
//          [X]--MettreAJourTacheFragment------------(Fait)--<==<<
//
//        HELPERS
//          [X]--ColorHelper-------------------------(Fait)--<==<<
//          [X]--DateHelper--------------------------(Fait)--<==<<
//          [X]--LocaleHelper------------------------(Fait)--<==<<
//          [X]--StringHelper------------------------(Fait)--<==<<
//
//        INTERFACES
//          [X]--OnTacheDateChangeListener-----------(Fait)--<==<<
//
//        MODELS
//          [X]--Employe-----------------------------(Fait)--<==<<
//          [X]--Tache-------------------------------(Fait)--<==<<
//
//        UTILS
//          [X]--TempsReceiver-----------------------(Fait)--<==<<
//          [X]--TempsService------------------------(Fait)--<==<<
//
//    res
//
//        LAYOUT
//          [X]--activity_modifier_employe.xml-------(Fait)--<==<<
//          [X]--activity_modifier_tache.xml---------(Fait)--<==<<
//          [X]--activity_employe.xml----------------(Fait)--<==<<
//          [X]--activity_principale.xml-------------(Fait)--<==<<
//          [X]--activity_ajouter_employe.xml--------(Fait)--<==<<
//          [X]--activity_ajouter_tache.xml----------(Fait)--<==<<
//          [X]--activity_tache.xml------------------(Fait)--<==<<
//          [X]--employe_row.xml---------------------(Fait)--<==<<
//          [X]--fragment_employes_liste.xml---------(Fait)--<==<<
//          [X]--fragment_taches_liste.xml-----------(Fait)--<==<<
//          [X]--fragment_mettre_a_jour_employe.xml--(Fait)--<==<<
//          [X]--fragment_mettre_a_jour_tache.xml----(Fait)--<==<<
//          [X]--tache_row.xml-----------------------(Fait)--<==<<
//          [X]--toolbar.xml-------------------------(Fait)--<==<<
//
//        MENU
//          [X]--menu_employe_liste.xml--------------(Fait)--<==<<
//          [X]--menu_item.xml-----------------------(Fait)--<==<<
//          [X]--menu_prefs.xml----------------------(Fait)--<==<<
//          [X]--menu_taches_liste.xml---------------(Fait)--<==<<
//          [X]--menu_mettre_a_jour_item.xml---------(Fait)--<==<<
//
//        VALUES
//          [X]--attrs.xml---------------------------(Fait)--<==<<
//          [X]--colors.xml--------------------------(Fait)--<==<<
//          [X]--dimens.xml--------------------------(Fait)--<==<<
//          [X]--strings.xml-------------------------(Fait)--<==<<
//          [X]--styles.xml--------------------------(Fait)--<==<<
//
//        XML
//          [X]--prefs_items.xml---------------------(Fait)--<==<<
//
//    [X]--------AndroidManifest.xml-----------------(Fait)--<==<<

/**
 * L'application de base
 *
 * <p>Cette classe est le point d'entrée dans l'application Android.
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

  /**
   * S'exécute lors de la création de la classe
   */
  @Override
  public void onCreate() {
    super.onCreate();
  }
}
