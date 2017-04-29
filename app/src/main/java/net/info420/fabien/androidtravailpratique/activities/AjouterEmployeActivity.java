package net.info420.fabien.androidtravailpratique.activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;

/**
 * {@link android.app.Activity} pour ajouter une entrée d'employé dans la base de donnée
 *
 * @see Employe
 * @see EmployeActivity
 * @see FragmentActivity
 * @see TodoContentProvider
 *
 * {@link <a href="http://www.vogella.com/tutorials/AndroidSQLite/article.html">Source SQLite</a>}
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   ?
 */
public class AjouterEmployeActivity extends FragmentActivity {
  private final static String TAG = AjouterEmployeActivity.class.getName();

  // Views pour stocker les données des employés et bouton
  private EditText  etEmployeeNom;
  private EditText  etEmployeePoste;
  private EditText  etEmployeeEmail;
  private EditText  etEmployeeTelephone;
  private Button    btnValider;

  /**
   * Exécuté à la création de l'activité
   *
   * Instancie l'interface
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
   * Ajoute le bon layout
   * Met le bon texte et la bonne couleur dans la {@link Toolbar}
   * Instancie les Views
   * Ajoute les Listeners
   */
  private void initUI() {
    setContentView(R.layout.activity_ajouter_employe);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.titre_activity_ajouter_employe);

    ColorHelper.setStatusBarColor(this);

    etEmployeeNom       = (EditText) findViewById(R.id.et_employe_nom);
    etEmployeePoste     = (EditText) findViewById(R.id.et_employe_poste);
    etEmployeeEmail     = (EditText) findViewById(R.id.et_employe_email);
    etEmployeeTelephone = (EditText) findViewById(R.id.et_employe_telephone);
    btnValider          = (Button)   findViewById(R.id.btn_valider);

    btnValider.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ajouterEmploye();
      }
    });
  }


  /**
   *  Envoie les données pour modifier l'Employé
   *
   * Va chercher les textes dans les EditTexts
   * Vérifie si tous les champs obligatoires sont là
   * Ajoute les valeurs dans une liste de valeurs
   * Ajoute l'employé
   * Termine l'activité
   *
   * @see TodoContentProvider
   *
   * {@link <a href="http://stackoverflow.com/questions/6358380/phone-number-validation-android#6359128">Validation du numéro de téléphone</a>}
   * {@link <a href="http://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext">Validation de l'adresse e-mail</a>}
   */
  public void ajouterEmploye() {
    String nom        = etEmployeeNom.getText().toString();
    String poste      = etEmployeePoste.getText().toString();
    String email      = etEmployeeEmail.getText().toString();
    String telephone  = etEmployeeTelephone.getText().toString();

    if (nom.length() == 0) {
      // Notification Toast
      Toast.makeText(getApplicationContext(), getString(R.string.attention_champs_vides), Toast.LENGTH_LONG).show();
      return;
    }

    // Validation du numéro de téléphone
    // Source : http://stackoverflow.com/questions/6358380/phone-number-validation-android#6359128
    if (telephone.length() != 0 && !PhoneNumberUtils.isGlobalPhoneNumber(telephone)) {
      Toast.makeText(getApplicationContext(), getString(R.string.attention_mauvais_format_telephone), Toast.LENGTH_LONG).show();
      return;
    }

    // Validation de l'adresse e-mail
    // Source : http://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
    if (email.length() != 0 && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      Toast.makeText(getApplicationContext(), getString(R.string.attention_mauvais_format_email), Toast.LENGTH_LONG).show();
      return;
    }

    ContentValues values = new ContentValues();
    values.put(Employe.KEY_nom,       nom);
    values.put(Employe.KEY_poste,     poste);
    values.put(Employe.KEY_telephone, telephone);
    values.put(Employe.KEY_email,     email);

    // Nouvel employé
    getContentResolver().insert(TodoContentProvider.CONTENT_URI_EMPLOYE, values);

    finish();
  }

  /**
   * Ajout des options de menus appropriées
   *
   * @param menu  Le {@link Menu}
   * @return      Booléen signifiant la réussite de l'opération
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_mettre_a_jour_item, menu);
    return true;
  }

  /**
   * Change le fragment lorsqu'une option du menu est sélectionnée
   *
   * @param item Le {@link MenuItem} sélectionné
   * @return     Booléen signifiant la réussite de l'opération
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_annuler:
        finish();
        break;
      default:
        break;
    }
    return true;
  }
}