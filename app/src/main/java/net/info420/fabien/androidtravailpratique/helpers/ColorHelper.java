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
   * @param urgence  Id du niveau d'urgence
   * @param contexte      Contexte pour appeler la méthode getColor()
   * @return              Couleur en lien avec le niveau d'urgence
   */
  public static int getUrgencyLevelColor(int urgence, Context contexte) {
    switch (urgence) {
      case 0:
        return contexte.getColor(R.color.colorUrgencyLevelLow);
      case 1:
        return contexte.getColor(R.color.colorUrgencyLevelMedium);
      case 2:
        return contexte.getColor(R.color.colorUrgencyLevelHigh);
      default:
        return contexte.getColor(R.color.colorPrimaryText);
    }
  }

  /**
   * Change la couleur de la barre de statut pour qu'elle marche bien avec
   * les couleurs de base de l'application
   * {link <a href="http://stackoverflow.com/questions/22192291/how-to-change-the-status-bar-color-in-android">Source</a>}
   * @param activite L'activité qui change la couleur de la barre de statut
   */
  public static void setStatusBarColor(Activity activite) {
    Window window = activite.getWindow();
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(ContextCompat.getColor(activite, R.color.colorPrimaryDark));
  }
}
