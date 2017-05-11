package net.info420.fabien.androidtravailpratique.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.info420.fabien.androidtravailpratique.R;

/**
 * {@link BroadcastReceiver} pour afficher les {@link Toast} de nombre de tâches
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-04-24
 *
 * @see TempsService
 *
 * @see <a href="http://www.vogella.com/tutorials/AndroidServices/article.html"
 *      target="_blank">
 *      Source : Services d'Android</a>
 */
public class TempsReceiver extends BroadcastReceiver {
  private final static String TAG = TempsReceiver.class.getName();

  /**
   * Affiche un {@link Toast} en fonction des extras dans le {@link android.os.Bundle}
   *
   * <ul>
   *  <li>Vérifie le nombre de tâche afin de mettre un texte représentatif</li>
   * </ul>
   *
   * @param context  {@link Context} pour afficher le {@link Toast}
   * @param intent    {@link Intent} contenant le {@link android.os.Bundle}
   *
   * @see TempsService
   * @see BroadcastReceiver
   */
  @Override
  public void onReceive(Context context, Intent intent) {
    if(intent.getExtras() != null) {
      if (intent.getExtras().getInt(TempsService.TACHES_NB) == 0) {
        Toast.makeText( context,
                        String.format("%s %s %s %s.",
                                      context.getString(R.string.info_vous_avez_pas),
                                      context.getString(R.string.tache).toLowerCase(),
                                      intent.getExtras().getString(TempsService.URGENCE),
                                      intent.getExtras().getString(TempsService.LAPS_TEMPS)),
                                      (intent.getExtras().getInt(TempsService.AFFICHAGE) == Toast.LENGTH_SHORT) ?
                                        Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText( context,
                        String.format("%s %s %s %s %s.",
                                      context.getString(R.string.info_vous_avez),
                                      intent.getExtras().getInt(TempsService.TACHES_NB),
                                      ((intent.getExtras().getInt(TempsService.TACHES_NB) > 1) ? context.getString(R.string.taches) : context.getString(R.string.tache)).toLowerCase(),
                                      intent.getExtras().getString(TempsService.URGENCE),
                                      intent.getExtras().getString(TempsService.LAPS_TEMPS)),
                                      (intent.getExtras().getInt(TempsService.AFFICHAGE) == Toast.LENGTH_SHORT) ?
                                        Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
      }
    }
  }
}