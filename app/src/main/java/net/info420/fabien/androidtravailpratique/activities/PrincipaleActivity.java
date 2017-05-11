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
import net.info420.fabien.androidtravailpratique.fragments.EmployesListeFragment;
import net.info420.fabien.androidtravailpratique.fragments.PrefsFragment;
import net.info420.fabien.androidtravailpratique.fragments.TachesListeFragment;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.helpers.LocaleHelper;
import net.info420.fabien.androidtravailpratique.helpers.PrefsHelper;
import net.info420.fabien.androidtravailpratique.utils.TempsReceiver;
import net.info420.fabien.androidtravailpratique.utils.TempsService;

/**
 * {@link android.app.Activity} principale
 *
 * Elle contrôle les trois fragments principaux :
 * <ul>
 *  <li>{@see TachesListeFragment}</li>
 *  <li>{@see EmployesListeFragment}</li>
 *  <li>{@see PrefsFragment}</li>
 * </ul>
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    ?
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
   * Exécuté à la création de l'{@link Activity}
   *
   * <ul>
   *  <li>Vérifie les permissions</li>
   *  <li>Démarre le TempsService</li>
   *  <li>Initialise le LocaleHelper</li>
   *  <li>Instancie l'interface</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle} pouvant contenir des données
   *
   * @see TempsService
   * @see LocaleHelper
   */
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityCompat.requestPermissions(this,
                                      new String[] {  Manifest.permission.CALL_PHONE,
                                                      Manifest.permission.SEND_SMS },
                                      1);

    // Création et démarrage de l'Intent pour le service de temps
    tempsServiceIntent = new Intent(this, TempsService.class);
    startService(tempsServiceIntent);

    LocaleHelper.initialize(this);

    initUI();
  }

  /**
   * Initialisation de l'interface
   *
   * <ul>
   *  <li>Ajoute le bon layout</li>
   *  <li>Met le fragment de base (TachesListeFragment)</li>
   *  <li>Met le bon texte et la bonne couleur dans la {@link Toolbar}</li>
   * </ul>
   *
   * @see TachesListeFragment
   */
  private void initUI() {
    setContentView(R.layout.activity_principale);

    // Met le fragment d'une liste (ou n'importe quel autre fragment) dans un conteneur à cet effet.
    getFragmentManager().beginTransaction().add(R.id.fragment_container, new TachesListeFragment()).commit();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.titre_taches_liste);

    ColorHelper.setStatusBarColor(this);
  }

  /**
   * Change le fragment dans {@link PrincipaleActivity}
   *
   * <ul>
   *  <li>Enlève le contenu du menu</li>
   *  <li>En fonction de fragmentId, change le menu, le titre de la toolbar et le fragment</li>
   * </ul>
   *
   * @param fragmentId Id du fragment
   */
  private void setFragment(int fragmentId) {
    menu.clear();

    // Variables pour la modification du fragment
    Toolbar toolbar                 = (Toolbar) findViewById(R.id.toolbar);
    FragmentTransaction transaction = getFragmentManager().beginTransaction();

    // Change le menu, le titre de la toolbar et le fragment
    switch(fragmentId) {
      case FRAGMENT_TACHES_LISTE:
        getMenuInflater().inflate(R.menu.menu_taches_liste, menu);
        toolbar.setTitle(R.string.titre_taches_liste);
        transaction.replace(R.id.fragment_container, new TachesListeFragment());
        break;
      case FRAGMENT_EMPLOYES_LISTE:
        getMenuInflater().inflate(R.menu.menu_employes_liste, menu);
        toolbar.setTitle(R.string.titre_employes_liste);
        transaction.replace(R.id.fragment_container, new EmployesListeFragment());
        break;
      case FRAGMENT_PREFS:
        getMenuInflater().inflate(R.menu.menu_prefs, menu);
        toolbar.setTitle(R.string.titre_prefs);
        transaction.replace(R.id.fragment_container, new PrefsFragment());
        break;
    }

    // Confirmation de la transaction (interchange les fragments)
    transaction.commit();
  }

  /**
   * Enregistre le nécéssaire lorsqu'on revient à l'{@link Activity}
   *
   * <ul>
   *  <li>Enregistre l'activité en temps que {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener}</li>
   *  <li>Enregistre le {@link TempsReceiver}</li>
   * </ul>
   *
   * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener
   * @see TempsService
   */
  @Override
  protected void onResume() {
    super.onResume();
    PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    registerReceiver(tempsReceiver, new IntentFilter(TempsService.NOTIFICATION));
  }

  /**
   * Désenregistre le nécéssaire lorsqu'on quitte l'{@link Activity}
   *
   * <ul>
   *  <li>Désenregistre l'activité en temps que {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener}</li>
   *  <li>Désenregistre le {@link TempsReceiver}</li>
   * </ul>
   *
   * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener
   * @see TempsService
   */
  @Override
  protected void onPause() {
    super.onPause();
    PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    unregisterReceiver(tempsReceiver);
  }

  /**
   * Ajout des options de menus appropriées
   *
   * @param   menu  Le {@link Menu}
   * @return  Booléen signifiant la réussite de l'opération
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_taches_liste, menu);

    this.menu = menu;

    return true;
  }

  /**
   * Fait les actions appropriées lorsqu'on clique dans le menu
   *
   * @param   item Le {@link MenuItem} sélectionné
   * @return  Booléen signifiant la réussite de l'opération
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
   * Appelé lorsqu'une préférence est modifiée
   *
   * <ul>
   *  <li>Lorsqu'une préférence de toast est modifiée, on recommence le TempsService</li>
   *  <li>Lorsqu'une préférence de locale est modifiée, on change la locale et on redémarre le fragment</li>
   * </ul>
   *
   * @param sharedPreferences La {@link SharedPreferences} qui a été modifiée
   * @param key               La clé (nom) de la {@link SharedPreferences} qui a été modifiée
   */
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // Si la préférence concerne les toasts...
    if (PrefsHelper.isToasts(key)) {

      // Recommence le service
      stopService(tempsServiceIntent);
      startService(tempsServiceIntent);
    }

    // Si la préférence concerne la langue...
    if (PrefsHelper.isLangue(key)) {
      // Change la locale
      LocaleHelper.setLocale(this, PrefsHelper.getLangue(this));

      // Redémarre le fragment, afin d'y appliquer les changements
      setFragment(FRAGMENT_PREFS);
    }
  }
}
