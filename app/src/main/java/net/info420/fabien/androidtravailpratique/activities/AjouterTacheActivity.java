package net.info420.fabien.androidtravailpratique.activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.fragments.DatePickerFragment;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.helpers.DateHelper;
import net.info420.fabien.androidtravailpratique.helpers.EmployeHelper;
import net.info420.fabien.androidtravailpratique.interfaces.OnTacheDateChangeListener;
import net.info420.fabien.androidtravailpratique.models.Tache;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link android.app.Activity} pour modifier une entrée d'employé dans la base de donnée
 *
 * @see Tache
 * @see TacheActivity
 * @see FragmentActivity
 * @see TodoContentProvider
 * @see OnTacheDateChangeListener
 *
 * @see <a href="http://www.vogella.com/tutorials/AndroidSQLite/article.html"
 *      target="_blank">
 *      Source : SQLite</a>
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   ?
 */
public class AjouterTacheActivity extends FragmentActivity implements OnTacheDateChangeListener {
  private final static String TAG = AjouterTacheActivity.class.getName();

  // Liste des noms des employés
  private ArrayAdapter<String> adapterTacheEmployeeAssigne;

  // Views pour stocker les données des employés et bouton
  private EditText  etTacheNom;
  private EditText  etTacheDescription;
  private CheckBox  cbTacheFait;
  private Spinner   spTacheUrgence;
  private Button    btnTacheDate;
  private Button    btnValider;
  private Spinner   spTacheEmployeAssigne;

  // Sert à lié la position dans le Spinner et le Id
  private Map<Integer, Integer> spTacheEmployeAssigneMap;

  // Date de la tâche en millisecondes
  public long tacheDate = 0;

  /**
   * Exécuté à la création de l'activité
   *
   * <ul>
   *   <li>Instancie l'interface</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle} pouvant contenir des données
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
  }

  /**
   * Initialisation de l'interface
   *
   * <ul>
   *   <li>Ajoute le bon layout</li>
   *   <li>Met le bon texte et la bonne couleur dans la {@link Toolbar}</li>
   *   <li>Instancie les Views</li>
   *   <li>Remplie le Spinner des employés avec les noms des employés</li>
   *   <li>Ajoute les Listeners</li>
   * </ul>
   */
  private void initUI() {
    setContentView(R.layout.activity_ajouter_tache);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.titre_activity_ajout_tache);

    ColorHelper.setStatusBarColor(this);

    etTacheNom            = (EditText)  findViewById(R.id.et_tache_nom);
    etTacheDescription    = (EditText)  findViewById(R.id.et_tache_description);
    cbTacheFait           = (CheckBox)  findViewById(R.id.cb_tache_fait);
    spTacheUrgence        = (Spinner)   findViewById(R.id.sp_tache_urgence);
    btnTacheDate          = (Button)    findViewById(R.id.btn_tache_date);
    btnValider            = (Button)    findViewById(R.id.btn_valider);
    spTacheEmployeAssigne = (Spinner)   findViewById(R.id.sp_tache_employe_assigne);

    // C'est l'heure d'aller chercher les noms des employés
    spTacheEmployeAssigneMap = new HashMap<>();
    EmployeHelper.fillEmployesSpinner(this, spTacheEmployeAssigne, spTacheEmployeAssigneMap, false, true);
    spTacheEmployeAssigne.setSelection(0); // 'Aucun employé assigné'

    btnTacheDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePickerDialog(view);
      }
    });

    btnValider.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ajouterTache();
      }
    });
  }

  /**
   * Envoie les données pour ajouter la Tâche
   *
   * <ul>
   *   <li>Va chercher les valeurs dans les Views</li>
   *   <li>Vérifie si tous les champs obligatoires sont là</li>
   *   <li>Ajoute les valeurs dans une liste de valeurs</li>
   *   <li>Ajoute la tâche</li>
   *   <li>Affiche un {@link Toast}</li>
   *   <li>Termine l'activité</li>
   * </ul>
   *
   * @see TodoContentProvider
   */
  public void ajouterTache() {
    String nom          = etTacheNom.getText().toString();
    String description  = etTacheDescription.getText().toString();
    int fait            = cbTacheFait.isChecked() ? 1 : 0;
    int date            = (int) tacheDate;
    int urgence         = (int) spTacheUrgence.getSelectedItemId();

    // Pour l'employé assigné, je vérifie d'abord si quelque chose a été choisi dans le Spinner. Dans ce cas, j'ajoute le bon Id d'employé. Sinon, null.
    int assinedEmployee = ((spTacheEmployeAssigne.getSelectedItem() != null) && (spTacheEmployeAssigne.getSelectedItemId() != 0)) ? spTacheEmployeAssigneMap.get((int) spTacheEmployeAssigne.getSelectedItemId()) : 0;

    if (nom.length() == 0 || tacheDate == 0) {
      Toast.makeText(getApplicationContext(), getString(R.string.attention_champs_vides), Toast.LENGTH_LONG).show();
      return;
    }

    ContentValues values = new ContentValues();
    values.put(Tache.KEY_nom,         nom);
    values.put(Tache.KEY_description, description);
    values.put(Tache.KEY_fait,        fait);
    values.put(Tache.KEY_date,        date);
    values.put(Tache.KEY_urgence,     urgence);

    // Si l'employé assigné a un Id de 0, alors il sera null
    if (assinedEmployee != 0) {
      values.put(Tache.KEY_employe_assigne_ID, assinedEmployee);
    } else {
      values.putNull(Tache.KEY_employe_assigne_ID);
    }

    // Nouvelle tâche
    getContentResolver().insert(TodoContentProvider.CONTENT_URI_TACHE, values);

    Toast.makeText(this, getString(R.string.tache_ajoutee), Toast.LENGTH_SHORT).show();

    finish();
  }

  /**
   * Modification de la variable de date de la tache
   *
   * <ul>
   *   <li>Modifie la date</li>
   *   <li>Appelle onTacheDateChange</li>
   * </ul>
   *
   * @param tacheDate Nouvelle date de la tache en millisecondes
   *
   * @see OnTacheDateChangeListener
   */
  public void setTacheDate(int tacheDate) { this.tacheDate = tacheDate; onTacheDateChange(); }

  /**
   * Affiche un DatePickerDialog
   *
   * @param view La vue qui appelle le {@link android.app.DatePickerDialog } (peut être this pour
   *             une {@link android.app.Activity})
   */
  public void showDatePickerDialog(View view) {
    // Afin de mettre la date comme date par défaut dans le calendrier
    if (tacheDate != 0) {
      DatePickerFragment.newInstance((int) tacheDate).show(getFragmentManager(), "datePicker");
    } else {
      DatePickerFragment.newInstance().show(getFragmentManager(), "datePicker");
    }
  }

  /**
   * Modifie le texte du bouton de date lorsque la date change
   */
  public void onTacheDateChange() {
    if (tacheDate != 0) {
      btnTacheDate.setText(DateHelper.getLongueDate(this, (int) tacheDate));
    }
  }

  /**
   * Ajout des options de menus appropriées
   *
   * @param   menu  Le {@link Menu}
   * @return  Booléen signifiant la réussite de l'opération
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_mettre_a_jour_item, menu);
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
      case R.id.menu_annuler:
        finish();
        break;
    }
    return true;
  }
}