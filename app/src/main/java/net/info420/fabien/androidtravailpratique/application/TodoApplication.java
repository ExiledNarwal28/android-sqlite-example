package net.info420.fabien.androidtravailpratique.application;

import android.app.Application;

/**
 * L'application de base
 *
 * <p>Cette classe est le point d'entrée dans l'application Android.
 * Elle contient des variables utilisées partout dans l'application.
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-03-26
 */
public class TodoApplication extends Application {
  private static final String TAG = TodoApplication.class.getName();

  /**
   * S'exécute lors de la création de la classe
   */
  @Override
  public void onCreate() {
    super.onCreate();
  }
}
