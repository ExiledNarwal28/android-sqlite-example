package net.info420.fabien.androidtravailpratique.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.models.Employe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe contenant des méthodes pour facilier l'obtention d'informations sur les employés
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-05-01
 */
public class EmployeHelper {
  private final static String TAG = EmployeHelper.class.getName();

  /**
   * Méthode statique pour obtenir le nom d'un employé avec son id
   *
   * @param   context {@link Context} pour obtenir le {@link android.content.ContentResolver}
   * @param   id      Id de l'{@link Employe}
   * @return  String du nom de l'employé
   *
   * @see Employe
   * @see TodoContentProvider
   */
  public static String getEmployeNom(Context context, int id) {
    String retString = null;

    // Projection, Uri et curseur
    String[] employeeProjection = { Employe.KEY_nom};
    Uri employeeUri = Uri.parse(TodoContentProvider.CONTENT_URI_EMPLOYE + "/" + id);
    Cursor employeeCursor = context.getContentResolver().query(employeeUri, employeeProjection, null, null, null);

    if (employeeCursor != null) {
      employeeCursor.moveToFirst();

      retString = employeeCursor.getString(employeeCursor.getColumnIndexOrThrow(Employe.KEY_nom));

      // Fermeture du curseur
      employeeCursor.close();
    }

    return retString;
  }

  /**
   * Méthode qui appelle fillEmployesSpinner
   *
   * @param context {@link Context} pour obtenir le {@link android.content.ContentResolver}
   * @param spinner {@link Spinner} pour ajouter les noms d'employés
   *
   * @see #fillEmployesSpinner(Context, Spinner, Map, boolean)
   */
  public static void fillEmployesSpinner(Context context, Spinner spinner) {
    fillEmployesSpinner(context, spinner, null, false);
  }

  /**
   * Méthode qui appelle fillEmployesSpinner
   *
   * @param context {@link Context} pour obtenir le {@link android.content.ContentResolver}
   * @param spinner {@link Spinner} pour ajouter les noms d'employés
   * @param map     {@link HashMap} pour associé le Id de l'{@link Employe} et sa position dans le
   *                {@link Spinner}
   *
   * @see #fillEmployesSpinner(Context, Spinner, Map, boolean)
   */
  public static void fillEmployesSpinner(Context context, Spinner spinner, Map<Integer, Integer> map) {
    fillEmployesSpinner(context, spinner, map, false);
  }

  /**
   * Ajoute la liste des employés au Spinner approprié
   *
   * <ul>
   *  <li>Met l'option de base (Tous les employés), si désirée</li>
   *  <li>Ajoute les employés dans une liste de noms</li>
   *  <li>Construit un {@link java.util.HashMap} pour faire le lien entre l'Id d'un employé et sa
   *  position dans le {@link Spinner}</li>
   *  <li>Ajoute l'{@link android.widget.Adapter} au {@link Spinner}</li>
   * </ul>
   *
   * @param context                 {@link Context} pour obtenir le {@link android.content.ContentResolver}
   * @param spinner                 {@link Spinner} pour ajouter les noms d'employés
   * @param map                     {@link HashMap} pour associé le Id de l'{@link Employe} et sa position dans le
   *                                {@link Spinner}
   * @param ajouterTousLesEmployes  boolean vrai si on ajoute 'Tous les employés
   *
   * @see Employe
   * @see TodoContentProvider
   * @see java.util.HashMap
   *
   * @see <a href="http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720"
   *      target="_blank">
   *      Source : Ajout manuel d'item dans un Spinner</a>
   * @see <a href="http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720"
   *      target="_blank">
   *      Ajout d'items à un Spinner</a>
   */
  public static void fillEmployesSpinner(Context context, Spinner spinner, Map<Integer, Integer> map, boolean ajouterTousLesEmployes) {
    ArrayList<String> employeeNoms = new ArrayList<>();

    // Seule option actuelle dans le filtre des employés
    if (ajouterTousLesEmployes) employeeNoms.add(context.getString(R.string.tache_filtre_tous_les_employes));

    Cursor employeCursor = context.getContentResolver().query(TodoContentProvider.CONTENT_URI_EMPLOYE, new String[] { Employe.KEY_ID, Employe.KEY_nom}, null, null, null);

    if (employeCursor != null) {
      Integer position = (ajouterTousLesEmployes) ? 1 : 0;

      while (employeCursor.moveToNext()) {
        employeeNoms.add(employeCursor.getString(employeCursor.getColumnIndexOrThrow(Employe.KEY_nom)));

        if (map != null) {
          map.put(  position,
                    employeCursor.getInt(employeCursor.getColumnIndexOrThrow(Employe.KEY_ID)));
        }

        position++;
      }

      // Fermeture du curseur
      employeCursor.close();
    }

    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, employeeNoms));
  }
}
