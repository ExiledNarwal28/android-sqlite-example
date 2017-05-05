package net.info420.fabien.androidtravailpratique.models;

/**
 * Définition d'un employé dans la base de données SQLite
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-03-23
 *
 * @see net.info420.fabien.androidtravailpratique.data.TodoContentProvider
 * @see net.info420.fabien.androidtravailpratique.data.DBHelper
 *
 * @see <a href="http://instinctcoder.com/android-studio-sqlite-database-example/"
 *      target="_blank">
 *      Source : Base de données SQLite et Android</a>
 */
public class Employe {
  // Nom de la table
  public static final String TABLE = "Employe";

  // Noms des colonnes de la table
  public static final String KEY_ID         = "_id";
  public static final String KEY_nom        = "nom";
  public static final String KEY_poste      = "poste";
  public static final String KEY_email      = "email";
  public static final String KEY_telephone  = "telephone";
}