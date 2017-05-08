package net.info420.fabien.androidtravailpratique.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.data.TodoContentProvider;
import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

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

    Cursor cursor = context.getContentResolver().query( Uri.parse(TodoContentProvider.CONTENT_URI_EMPLOYE + "/" + id),
                                                        new String[] { Employe.KEY_nom },
                                                        Employe.KEY_ID + "=?",
                                                        new String[] { Integer.toString(id) },
                                                        null);

    if (cursor != null) {
      cursor.moveToFirst();

      retString = cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_nom));

      // Fermeture du curseur
      cursor.close();
    }

    return retString;
  }

  /**
   * Méthode statique pour obtenir le nombre de tâches d'un employé avec son id
   *
   * @param   context {@link Context} pour obtenir le {@link android.content.ContentResolver}
   * @param   id      Id de l'{@link Employe}
   * @return  Integer du compte de tâches de l'employé
   */
  public static Integer getEmployeNbTache(Context context, int id) {
    Cursor cursor = context.getContentResolver().query( TodoContentProvider.CONTENT_URI_TACHE,
                                                        new String[] { Tache.KEY_ID, Tache.KEY_employe_assigne_ID },
                                                        Tache.KEY_employe_assigne_ID + "=?",
                                                        new String[] { Integer.toString(id) },
                                                        null);

    return (cursor != null) ? cursor.getCount() : 0;
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
   * @param ajouterTousLesEmployes  boolean vrai si on ajoute 'Tous les employés'
   * @param ajouterAucunEmploye     boolean vrai si on ajoute 'Tous les employés
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
  public static void fillEmployesSpinner(Context context, Spinner spinner, Map<Integer, Integer> map,
                                         boolean ajouterTousLesEmployes, boolean ajouterAucunEmploye) {
    ArrayList<String> noms = new ArrayList<>();

    if (ajouterTousLesEmployes) noms.add(context.getString(R.string.tache_filtre_tous_les_employes));
    if (ajouterAucunEmploye)    noms.add(context.getString(R.string.tache_aucun_employe));

    Cursor cursor = context.getContentResolver().query( TodoContentProvider.CONTENT_URI_EMPLOYE,
                                                        new String[] { Employe.KEY_ID, Employe.KEY_nom},
                                                        null, null, null);

    if (cursor != null) {

      while (cursor.moveToNext()) {
        noms.add(cursor.getString(cursor.getColumnIndexOrThrow(Employe.KEY_nom)));

        if (map != null) {
          map.put(noms.size() - 1,
                  cursor.getInt(cursor.getColumnIndexOrThrow(Employe.KEY_ID)));
        }
      }

      // Fermeture du curseur
      cursor.close();
    }

    // Source : http://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android#5241720
    spinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, noms));
  }
}
