package net.info420.fabien.androidtravailpratique.helpers;

import android.content.Context;

import net.info420.fabien.androidtravailpratique.R;

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
   *
   * @param urgence  Id du niveau d'urgence
   * @param context Context pour appeler la méthode getString()
   * @return         Le nom du niveau d'urgence, en fonction de la locale
   */
  public static String getUrgence(int urgence, Context context) {
    switch (urgence) {
      case 0:
        return context.getString(R.string.tache_urgence_bas);
      case 1:
        return context.getString(R.string.tache_urgence_moyen);
      case 2:
        return context.getString(R.string.tache_urgence_haut);
      default:
        return context.getString(R.string.erreur);
    }
  }
}
