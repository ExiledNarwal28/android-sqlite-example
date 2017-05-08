package net.info420.fabien.androidtravailpratique.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.helpers.ColorHelper;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

/**
 * {@link android.app.Activity} pour voir les détails d'une entrée d'employé dans la base de donnée
 *
 * @see Employe
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
public class EmployeActivity extends Activity {
  private final static String TAG = EmployeActivity.class.getName();

  // Views pour stocker les données des employés et bouton
  private TextView  tvEmployeNom;
  private TextView  tvEmployePoste;
  private TextView  tvEmployeEmail;
  private TextView  tvEmployeTelephone;
  private Button    btnEmployeEnvoyerSMS;
  private Button    btnEmployeAppeler;

  private Uri employeUri;

  // Données de l'employé
  private int employeId;
  private String employeTelephone;

  /**
   * Exécuté à la création de l'activité
   *
   * <ul>
   *   <li>Instancie l'interface</li>
   *   <li>Va chercher les données d'Employé</li>
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
    employeUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE);

    // Ou passée depuis une autre activité
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      employeUri = extras.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE);

      rempliData(employeUri);
    }
  }

  /**
   * Initialisation de l'interface
   *
   * <ul>
   *   <li>Ajoute le bon layout</li>
   *   <li>Met le bon texte et la bonne couleur dans la {@link Toolbar}</li>
   *   <li>Instancie les Views</li>
   *   <li>Ajoute les Listeners</li>
   * </ul>
   */
  private void initUI() {
    setContentView(R.layout.activity_employe);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.titre_activity_employe);

    ColorHelper.setStatusBarColor(this);

    tvEmployeNom          = (TextView)  findViewById(R.id.tv_employe_nom);
    tvEmployePoste        = (TextView)  findViewById(R.id.tv_employe_poste);
    tvEmployeEmail        = (TextView)  findViewById(R.id.tv_employe_email);
    tvEmployeTelephone    = (TextView)  findViewById(R.id.tv_employe_telephone);
    btnEmployeEnvoyerSMS  = (Button)    findViewById(R.id.btn_employe_envoyer_sms);
    btnEmployeAppeler     = (Button)    findViewById(R.id.btn_employe_appeler);

    btnEmployeEnvoyerSMS.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        envoyerSMS();
      }
    });

    btnEmployeAppeler.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        appeler();
      }
    });
  }

  /**
   *  Envoie les données pour supprimer l'Employé
   *
   * <ul>
   *   <li>ime l'employé de la base de données</li>
   *   <li>e l'employé des tâches qui lui sont assignées</li>
   *   <li>Termine l'activité</li>
   * </ul>
   *
   * @see Tache
   * @see TodoContentProvider
   *
   * @see <a href="http://stackoverflow.com/questions/6234171/how-do-i-update-an-android-sqlite-database-column-value-to-null-using-contentval"
   *      target="_blank">
   *      Source : Ajouter une valeur nulle</a>
   * @see <a href="https://developer.android.com/guide/topics/providers/content-provider-basics.html"
   *      target="_blank">
   *      Source : Mettre à jour des items</a>
   */
  private void supprimerEmploye() {
    getContentResolver().delete(employeUri, null, null); // Suppression de l'employé

    // Il faut aussi enlever cet employé de toutes les tâches
    // Source : http://stackoverflow.com/questions/6234171/how-do-i-update-an-android-sqlite-database-column-value-to-null-using-contentval
    ContentValues values = new ContentValues();
    values.putNull(Tache.KEY_employe_assigne_ID);

    // On n'a besoin que des tâches qui ont cet employé
    // Source : https://developer.android.com/guide/topics/providers/content-provider-basics.html
    String    selection     = Tache.KEY_employe_assigne_ID + " = ?";
    String[]  selectionArgs = {Integer.toString(employeId)};

    // Modification des tâches qui n'auront plus l'employé assigné
    getContentResolver().update(TodoContentProvider.CONTENT_URI_TACHE,
                                values,
                                selection,
                                selectionArgs);

    finish();
  }

  /**
   * Rempli les EditTexts des données
   *
   * <ul>
   *   <li>Construit un tableau de String, c'est le SELECT du {@link Cursor}</li>
   *   <li>Construit le {@link Cursor}</li>
   *   <li>Rempli les EditTexts</li>
   * </ul>
   *
   * @param employeUri l'Uri vers l'employé à modifier
   */
  private void rempliData(Uri employeUri) {
    String[] projection = {Employe.KEY_ID,
      Employe.KEY_nom,
      Employe.KEY_poste,
      Employe.KEY_email,
      Employe.KEY_telephone};

    Cursor cursor = getContentResolver().query(employeUri, projection, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();

      // Données de l'employé
      employeId = cursor.getInt(cursor.getColumnIndexOrThrow(Employe.KEY_ID));
      employeTelephone = cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_telephone));

      // On mets les données dans l'UI
      tvEmployeNom.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_nom)));
      tvEmployePoste.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_poste)));
      tvEmployeEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_email)));
      tvEmployeTelephone.setText(employeTelephone);

      // Fermeture du curseur
      cursor.close();
    }
  }

  /**
   * Envoie un SMS à l'employé à l'aide de son numéro de téléphone
   *
   * <ul>
   *   <li>Vérifie si le numéro de téléphone est valide et si la permission est accordée</li>
   *   <li>Construit un {@link AlertDialog} contenant l'EditText pour le message</li>
   *   <li>Ajoute les Listeners de réponse au {@link AlertDialog} (si l'utilisateur accepter, envoie
   *   le SMS)</li>
   *   <li>Affiche l'{@link AlertDialog}</li>
   * </ul>
   *
   * @see AlertDialog
   * @see SmsManager
   * @see PendingIntent
   *
   * @see <a href="http://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog#29048271"
   *      target="_blank">
   *      Source : Mettre un EditText dans un Dialog</a>
   * @see <a href="http://stackoverflow.com/questions/10752394/smsmanager-sendtextmessage-is-not-working"
   *      target="_blank">
   *      Source : Faire fonctionner l'envoie de SMS</a>
   */
  private void envoyerSMS() {
    if ((employeTelephone != null) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)) {
      // Source : http://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog#29048271
      AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

      // Titre et message
      alertDialog.setTitle(getString(R.string.info_envoie_dun_sms));
      alertDialog.setMessage(getString(R.string.info_entrer_votre_message));

      // Ajout de l'EditText
      final EditText input = new EditText(this);
      input.setLayoutParams(new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
      alertDialog.setView(input);

      // Si la réponse est positive...
      alertDialog.setPositiveButton(getString(R.string.action_envoyer),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            String message = input.getText().toString();

            if (!message.isEmpty()) {
              // Il est possible que cela ne marche pas sans PendingIntent
              // Source : http://stackoverflow.com/questions/10752394/smsmanager-sendtextmessage-is-not-working
              SmsManager.getDefault().sendTextMessage(employeTelephone,
                                                      null,
                                                      message,
                                                      PendingIntent.getBroadcast(getBaseContext(), 0, new Intent("SMS_SENT"), 0),
                                                      null);
            }
          }
        });

      // Si la réponse est négative, on annule
      alertDialog.setNegativeButton(getString(R.string.action_annuler),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

      alertDialog.show();
    }
  }

  /**
   * Appelle l'employé
   *
   * <ul>
   *   <li>Vérifie si le numéro de téléphone est valide et si la permission est accordée</li>
   *   <li>Appelle l'employé avec un nouveau {@link Intent}</li>
   *   <li>Pour appeler, il suffit d'ajouter "tel:" au {@link Uri} et d'ajouter Intent.ACTION_CALL
   *   à l'{@link Intent}</li>
   * </ul>
   *
   * @see Uri
   * @see Intent
   *
   * @see <a href="http://stackoverflow.com/questions/5230912/android-app-to-call-a-number-on-button-click"
   *      target="_blank">
   *      Source : Appeler un numéro</a>
   */
  private void appeler() {
    // On vérifie qu'il y a bien un numéro de téléphone
    // On vérifie aussi qu'on a bien la permission d'appeler le contact
    if ((employeTelephone != null) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
      // Source : Vhttp://stackoverflow.com/questions/5230912/android-app-to-call-a-number-on-button-click
      Intent callIntent = new Intent(Intent.ACTION_CALL);
      callIntent.setData(Uri.parse("tel:" + employeTelephone));
      startActivity(callIntent);
    }
  }

  /**
   * Rafraîchit quand on revient dans l'Activity (ex. : en revenant d'ModifierEmployeActivity)
   */
  @Override
  public void onResume() {
    super.onResume();
    rempliData(employeUri);
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
        Intent i = new Intent(this, ModifierEmployeActivity.class);
        i.putExtra(TodoContentProvider.CONTENT_ITEM_TYPE_EMPLOYE, employeUri);
        startActivity(i);
        break;
      case R.id.menu_supprimer:
        supprimerEmploye();
        break;
      default:
        break;
    }
    return true;
  }
}
