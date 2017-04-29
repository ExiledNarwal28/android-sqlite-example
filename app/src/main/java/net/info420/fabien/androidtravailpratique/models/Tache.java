package net.info420.fabien.androidtravailpratique.models;

/**
 * Définition d'une tâche dans la base de données SQLite
 *
 * @author   Fabien Roy
 * @version  1.0
 * @since    17-03-23
 *
 * @see net.info420.fabien.androidtravailpratique.data.TodoContentProvider
 * @see net.info420.fabien.androidtravailpratique.data.DBHelper
 *
 * {@link <a href="http://instinctcoder.com/android-studio-sqlite-database-example/">Base de données SQLite et Android</a>}
 */
public class Tache {
  // Nom de la table
  public static final String TABLE = "Tache";

  // Noms des colonnes de la table
  public static final String KEY_ID                 = "_id";
  public static final String KEY_employe_assigne_ID = "employe_assigne_id_fk";
  public static final String KEY_nom                = "nom";
  public static final String KEY_description        = "description";
  public static final String KEY_fait               = "fait";
  public static final String KEY_date               = "date";
  public static final String KEY_urgence            = "urgence";

  // Types des propriétés
  public int      tache_ID;
  public int      employe_assigne_ID;
  public String   nom;
  public String   description;
  public boolean  fait;
  public int      date; // date est un integer "Unix Time"
  public int      urgence;
}