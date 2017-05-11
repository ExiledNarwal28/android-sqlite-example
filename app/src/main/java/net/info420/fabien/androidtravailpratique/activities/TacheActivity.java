package net.info420.fabien.androidtravailpratique.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.helpers.DateHelper;
import net.info420.fabien.androidtravailpratique.helpers.EmployeHelper;
import net.info420.fabien.androidtravailpratique.helpers.StringHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

/**
 * {@link android.app.Activity} pour voir les détails d'une entrée de tâche dans la base de donnée
 *
 * @see Tache
 * @see TodoContentProvider
 *
 * @see <a href="http://www.vogella.com/tutorials/AndroidSQLite/article.html"
 *      target="_blank">
 *      Source : SQLite</a>
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   ?
 */
public class TacheActivity extends Activity {
  private final static String TAG = TacheActivity.class.getName();

  // Views pour stocker les données des employés et bouton
  private TextView  tvTacheNom;
  private CheckBox  cbTacheFait;
  private TextView  tvTacheUrgence;
  private TextView  tvTacheDescription;
  private TextView  tvTacheDate;
  private TextView  tvTacheEmployeAssigne;
  private Button    btnTacheEmployeAssigne;

  private Uri tacheUri;

  // Données de l'employé assigné
  private int employeAssigneId;

  /**
   * Exécuté à la création de l'activité
   *
   * <ul>
   *   <li>Instancie l'interface</li>
   *   <li>Va chercher les données de la tâche</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle} pouvant contenir des données
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();

    // On va chercher les données...
    // Depuis l'instance sauvegarder
    tacheUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE_TACHE);
    Bundle extras = getIntent().getExtras();

    // Ou passée depuis une autre activité
    if (extras != null) {
      tacheUri = extras.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE_TACHE);

      rempliData(tacheUri);
    }
  }

  /**
   * Initialisation de l'interface
   *
   * <ul>
   *   <li>Ajoute le bon layout</li>
   *   <li>Met le bon texte et la bonne couleur dans la {@link Toolbar}</li>
   *   <li>Instancie les {@link View}</li>
   *   <li>Ajoute les Listeners</li>
   * </ul>
   */
  private void initUI() {
    setContentView(R.layout.activity_tache);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.titre_tache);

    ColorHelper.setStatusBarColor(this);

    tvTacheNom              = (TextView)  findViewById(R.id.tv_tache_nom);
    cbTacheFait             = (CheckBox)  findViewById(R.id.cb_tache_fait);
    tvTacheUrgence          = (TextView)  findViewById(R.id.tv_tache_urgence);
    tvTacheDescription      = (TextView)  findViewById(R.id.tv_tache_description);
    tvTacheDate             = (TextView)  findViewById(R.id.tv_tache_date);
    tvTacheEmployeAssigne   = (TextView)  findViewById(R.id.tv_tache_employe_assigne);
    btnTacheEmployeAssigne  = (Button)    findViewById(R.id.btn_tache_employe_assigne);

    btnTacheEmployeAssigne.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Va voir l'activité d'employé
        Intent i = new Intent(getApplicationContext(), EmployeActivity.class);
        Uri employeeUri = Uri.parse(TodoContentProvider.CONTENT_URI_EMPLOYE + "/" + employeAssigneId);
        i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE, employeeUri);

        startActivity(i);
      }
    });
  }

  /**
   * Envoie les données pour supprimer l'Employé
   *
   * <ul>
   *   <li>Supprime la tâche de la base de données</li>
   *   <li>Affiche un {@link Toast}</li>
   *   <li>Termine l'activité</li>
   * </ul>
   *
   * @see TodoContentProvider
   */
  private void supprimerTache() {
    getContentResolver().delete(tacheUri, null, null);

    Toast.makeText(this, getString(R.string.tache_supprimee), Toast.LENGTH_SHORT).show();

    finish();
  }

  /**
   * Rempli les Views des données
   *
   * <ul>
   *   <li>Construit un tableau de String, c'est le SELECT du {@link Cursor}</li>
   *   <li>Construit le {@link Cursor}</li>
   *   <li>Remplit les {@link View} (voir le nom de l'employé, on a besoin d'un deuxième
   *   {@link Cursor})</li>
   * </ul>
   *
   * @param tacheUri l'Uri vers l'employé à modifier
   *
   * @see Employe
   * @see DateHelper
   * @see StringHelper
   * @see EmployeHelper
   */
  private void rempliData(Uri tacheUri) {
    String[] projection = { Tache.KEY_employe_assigne_ID,
                            Tache.KEY_nom,
                            Tache.KEY_description,
                            Tache.KEY_fait,
                            Tache.KEY_date,
                            Tache.KEY_urgence};

    Cursor cursor = getContentResolver().query(tacheUri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      // Données de l'employé
      if (cursor.isNull(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID))) {
        btnTacheEmployeAssigne.setVisibility(View.GONE);
        tvTacheEmployeAssigne.setText(getString(R.string.tache_aucun_employe));
      } else {
        employeAssigneId = cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_employe_assigne_ID));

        btnTacheEmployeAssigne.setVisibility(View.VISIBLE);
        btnTacheEmployeAssigne.setText( getString(R.string.info_voir) + " " +
                                        EmployeHelper.getEmployeNom(this, employeAssigneId));
        tvTacheEmployeAssigne.setText(getString(R.string.tache_employe_assigne) + " : " +
                                      EmployeHelper.getEmployeNom(this, employeAssigneId));
      }

      // On mets les données dans l'UI
      tvTacheNom.setText(cursor.getString(cursor.getColumnIndexOrThrow(Tache.KEY_nom)));
      cbTacheFait.setChecked((cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_fait))) == 1); // Conversion en boolean
      tvTacheDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(Tache.KEY_description)));

      // Conversion en date
      tvTacheDate.setText(getString(R.string.tache_due_pour) + " : " + DateHelper.getLongueDate(this, cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_date))));

      // Conversion en niveau d'urgence textuel
      tvTacheUrgence.setText(StringHelper.getUrgence(cursor.getInt(cursor.getColumnIndexOrThrow(Tache.KEY_urgence)), this));

      // Fermeture du curseur
      cursor.close();
    }
  }

  /**
   * Rafraîchit quand on revient dans l'Activity (ex. : en revenant d'ModifierEmployeActivity)
   */
  @Override
  public void onResume() {
    super.onResume();
    rempliData(tacheUri);
  }

  /**
   * Ajout des options de menus appropriées
   *
   * @param   menu  Le {@link Menu}
   * @return  Booléen signifiant la réussite de l'opération
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_item, menu);
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
      case R.id.menu_modifier:
        // Démarre l'activité de modification
        Intent i = new Intent(this, ModifierTacheActivity.class);
        i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_TACHE, tacheUri);
        startActivity(i);
        break;
      case R.id.menu_supprimer:
        supprimerTache();
        break;
      default:
        break;
    }
    return true;
  }
}
