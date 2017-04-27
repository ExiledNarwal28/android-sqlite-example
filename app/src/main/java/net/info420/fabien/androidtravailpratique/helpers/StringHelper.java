package net.info420.fabien.androidtravailpratique.helpers;

import android.content.Context;
import android.util.Log;

import net.info420.fabien.androidtravailpratique.R;

import static android.content.ContentValues.TAG;

/**
 * Classe contenant des méthodes pour facilier la recherche de Strings
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-27
 */
public class StringHelper {

  /**
   * Méthode statique pour obtenir le nom d'un niveau d'urgence
   * @param urgence  Id du niveau d'urgence
   * @param contexte Contexte pour appeler la méthode getString()
   * @return         Le nom du niveau d'urgence, en fonction de la locale
   */
  public static String getUrgence(int urgence, Context contexte) {
    Log.d(TAG, "text : " + Integer.toString(urgence));
    switch (urgence) {
      case 0:
        return contexte.getString(R.string.task_urgency_level_low);
      case 1:
        return contexte.getString(R.string.task_urgency_level_medium);
      case 2:
        return contexte.getString(R.string.task_urgency_level_high);
      default:
        return contexte.getString(R.string.error);
    }
  }
}
