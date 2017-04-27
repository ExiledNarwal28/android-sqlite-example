package net.info420.fabien.androidtravailpratique.activities;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.application.TodoApplication;
import net.info420.fabien.androidtravailpratique.fragments.EmployeeListFragment;
import net.info420.fabien.androidtravailpratique.fragments.PrefsFragment;
import net.info420.fabien.androidtravailpratique.fragments.TachesListeFragment;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.helpers.LocaleHelper;
import net.info420.fabien.androidtravailpratique.utils.TempsReceiver;
import net.info420.fabien.androidtravailpratique.utils.TempsService;

/**
 * Activité principale
 * Elle contrôle les trois fragments principaux :
 *  @See TachesListeFragment
 *  @See EmployeeListFragment
 *  @See PrefsFragment
 *
 *  @author   Fabien Roy
 *  @version  1.0
 *  @since    ?
 */
public class PrincipaleActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
  private final static String TAG = PrincipaleActivity.class.getName();

  // BroadcastReceiver qui affiche des Toasts de tâches
  private TempsReceiver tempsReceiver = new TempsReceiver();

  // Menu
  private Menu menu;

  // Intent de TempsService
  private Intent tempsServiceIntent;

  // Utile pour le changement de fragments
  private final static int FRAGMENT_TACHES_LISTE    = 0;
  private final static int FRAGMENT_EMPLOYES_LISTE  = 1;
  private final static int FRAGMENT_PREFS           = 2;

  /**
   * Exécuté à la création de l'activité
   * @param savedInstanceState {@link Bundle} pouvant contenir des données
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // On demande les permissions
    ActivityCompat.requestPermissions(this,
                                      new String[] { Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS },
                                      1);

    // Création et démarrage de l'Intent pour le service de temps
    tempsServiceIntent = new Intent(this, TempsService.class);
    startService(tempsServiceIntent);

    // Initialisation de la locale, en fonction des préférences
    LocaleHelper.initialize(this);

    // Démarrage de l'interface utilisateur
    initUI();
  }

  /**
   * Ajoute le bon layout
   * Place le fragment initiale (liste des tâches)
   * Met le bon texte dans la {@link Toolbar}
   * Met la bonne couleur à la {@link Toolbar}
   */
  private void initUI() {
    setContentView(R.layout.activity_main);

    // Fragment initiale
    TachesListeFragment tachesListeFragment = new TachesListeFragment();

    // Met le fragment d'une liste (ou n'importe quel autre fragment) dans un conteneur à cet effet.
    getFragmentManager().beginTransaction().add(R.id.fragment_container, tachesListeFragment).commit();

    // Change le texte de la Toolbar
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_task_list);

    // Change la couleur de la barre de statut
    ColorHelper.setStatusBarColor(this);
  }

  /**
   * Change le fragment dans {@link PrincipaleActivity}
   * @param fragmentId Id du fragment
   */
  private void setFragment(int fragmentId) {
    // TODO : REDESIGN : Ne pas changer le fragment si c'est le fragment actuel

    // Enlève le contenu du menu
    menu.clear();

    // Variables pour la modification du fragment
    Toolbar toolbar                 = (Toolbar) findViewById(R.id.toolbar);
    FragmentTransaction transaction = getFragmentManager().beginTransaction();

    // Change le menu, le titre de la toolbar et le fragment
    switch(fragmentId) {
      case FRAGMENT_TACHES_LISTE:
        getMenuInflater().inflate(R.menu.menu_taches_liste, menu);
        toolbar.setTitle(R.string.title_task_list);
        transaction.replace(R.id.fragment_container, new TachesListeFragment());
        break;
      case FRAGMENT_EMPLOYES_LISTE:
        getMenuInflater().inflate(R.menu.menu_employee_list, menu);
        toolbar.setTitle(R.string.title_employee_list);
        transaction.replace(R.id.fragment_container, new EmployeeListFragment());
        break;
      case FRAGMENT_PREFS:
        getMenuInflater().inflate(R.menu.menu_prefs, menu);
        toolbar.setTitle(R.string.title_prefs);
        transaction.replace(R.id.fragment_container, new PrefsFragment());
        break;
    }

    // Confirmation de la transaction (interchange les fragments)
    transaction.commit();
  }

  /**
   * Enregistre l'activité en temps que {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener}
   * Enregistre le {@link TempsReceiver}
   */
  @Override
  protected void onResume() {
    super.onResume();
    PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    registerReceiver(tempsReceiver, new IntentFilter(TempsService.NOTIFICATION));
  }

  /**
   * Désenregistre l'activité en temps que {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener}
   * Désenregistre le {@link TempsReceiver}
   */
  @Override
  protected void onPause() {
    super.onPause();
    PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    unregisterReceiver(tempsReceiver);
  }

  /**
   * Ajout des options de menus appropriées
   * @param menu  Le {@link Menu}
   * @return      Booléen signifiant la réussite de l'opération
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_taches_liste, menu);

    this.menu = menu;

    return true;
  }

  /**
   * Change le fragment lorsqu'une option du menu est sélectionnée
   * @param item Le {@link MenuItem} sélectionné
   * @return     Booléen signifiant la réussite de l'opération
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.ic_taches_liste:
        setFragment(FRAGMENT_TACHES_LISTE);
        break;
      case R.id.ic_employes_liste:
        setFragment(FRAGMENT_EMPLOYES_LISTE);
        break;
      case R.id.ic_prefs:
        setFragment(FRAGMENT_PREFS);
        break;
    }

    return true;
  }

  /**
   * Lorsqu'une préférence de toast est modifiée, on recommence le TempsService
   * Lorsqu'une préférence de locale est modifiée, on change la locale et on redémarre le fragment
   * @param sharedPreferences La {@link SharedPreferences} qui a été modifiée
   * @param key               La clé (nom) de la {@link SharedPreferences} qui a été modifiée
   */
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // Si la préférence concerne les toasts...
    if (key.equals(TodoApplication.PREFS_TOASTS)            ||
        key.equals(TodoApplication.PREFS_TOASTS_FREQUENCE)  ||
        key.equals(TodoApplication.PREFS_TOASTS_LAPS_TEMPS) ||
        key.equals(TodoApplication.PREFS_TOASTS_URGENCE)) {

      // Recommence le service
      stopService(tempsServiceIntent);
      startService(tempsServiceIntent);
    }

    // Si la préférence concerne la langue...
    if (key.equals(TodoApplication.PREFS_LANGUE)) {
      // Change la locale
      LocaleHelper.setLocale(this, PreferenceManager.getDefaultSharedPreferences(this).getString(TodoApplication.PREFS_LANGUE, "fr"));

      // Redémarre le fragment, afin d'y appliquer les changements
      setFragment(FRAGMENT_PREFS);
    }
  }
}
