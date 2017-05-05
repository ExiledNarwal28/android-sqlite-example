package net.info420.fabien.androidtravailpratique.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import net.info420.fabien.androidtravailpratique.R;

/**
 * Classe contenant des méthodes pour facilier la recherche de couleurs
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-27
 */
public class ColorHelper {

  /**
   * Méthode statique pour recherche une couleur en fonction du niveau d'urgence
   *
   * @param   urgence  Id du niveau d'urgence
   * @param   context Context pour appeler la méthode getColor()
   * @return  Couleur en lien avec le niveau d'urgence
   */
  public static int getUrgencyLevelColor(Context context, int urgence) {
    switch (urgence) {
      case 0:
        return context.getColor(R.color.colorUrgenceBas);
      case 1:
        return context.getColor(R.color.colorUrgenceMoyen);
      case 2:
        return context.getColor(R.color.colorUrgenceHaut);
      default:
        return context.getColor(R.color.colorPrimaryText);
    }
  }

  /**
   * Change la couleur de la barre de statut pour qu'elle marche bien avec
   * les couleurs de base de l'application
   *
   * @param activite L'activité qui change la couleur de la barre de statut
   *
   * @see <a href="http://stackoverflow.com/questions/22192291/how-to-change-the-status-bar-color-in-android"
   *      target="_blank">
   *      Source : Changer la couleur de la barre de statut</a>
   */
  public static void setStatusBarColor(Activity activite) {
    Window window = activite.getWindow();
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(ContextCompat.getColor(activite, R.color.colorPrimaryDark));
  }
}
