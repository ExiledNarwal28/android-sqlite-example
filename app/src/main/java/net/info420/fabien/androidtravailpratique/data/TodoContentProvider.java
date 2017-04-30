package net.info420.fabien.androidtravailpratique.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

import java.util.Arrays;
import java.util.HashSet;

/**
 * {@link ContentProvider} pour la sélection, l'insertion, la modification et la suppression de
 * tâches et d'employés
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-03-26
 *
 * @see Employe
 * @see Tache
 * @see DBHelper
 * @see ContentProvider
 *
 * @see <a href="http://instinctcoder.com/android-studio-sqlite-database-example/"
 *      target="_blank">
 *      Source : Base de données SQLite et Android</a>
 */
public class TodoContentProvider extends ContentProvider {
  private final static String TAG = TodoContentProvider.class.getName();

  // Base de données
  private DBHelper dbHelper;

  private static final String AUTHORITY = "net.info420.fabien.androidtravailpratique.data";

  // Utilisé pour le UriMatcher
  private static final int TACHES     = 10;
  private static final int TACHE_ID   = 20;
  private static final int EMPLOYES   = 30;
  private static final int EMPLOYE_ID = 40;

  private static final String BASE_PATH_TACHE     = "taches";
  private static final String BASE_PATH_EMPLOYE   = "employes";
  public  static final Uri    CONTENT_URI_TACHE   = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_TACHE);
  public  static final Uri    CONTENT_URI_EMPLOYE = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_EMPLOYE);

  public static final String CONTENT_TYPE_TACHES        = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/taches";
  public static final String CONTENT_ITEM_TYPE_TACHE    = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/tache";
  public static final String CONTENT_TYPE_EMPLOYES      = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/employes";
  public static final String CONTENT_ITEM_TYPE_EMPLOYE  = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/employe";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH_TACHE,          TACHES);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH_TACHE + "/#",   TACHE_ID);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH_EMPLOYE,        EMPLOYES);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH_EMPLOYE + "/#", EMPLOYE_ID);
  }

  /**
   * Exécuté à la création
   *
   * <ul>
   *  <li>Instancie le {@link DBHelper}</li>
   * </ul>
   *
   * @return un boolean
   */
  @Override
  public boolean onCreate() {
    dbHelper = new DBHelper(getContext());

    return false;
  }

  /**
   * Sélection dans la base de données
   *
   * <ul>
   *  <li>Vérifie l'Uri (tache/employé et tout sélectionné/un seul)</li>
   *  <li>Vérifie si les colonnes de la projection sont valides</li>
   *  <li>Choisit la table du {@link SQLiteQueryBuilder}</li>
   *  <li>Si on choisit une seule tâche/un seul employé, sélectionne l'item spécifique</li>
   *  <li>Exécute le SELECT</li>
   * </ul>
   *
   * @param   uri           Uri envoyé pour la sélection
   * @param   projection    Le SELECT         (ex. : "_id, date, employe_assigne")
   * @param   selection     Le WHERE          (ex. : "_id =?")
   * @param   selectionArgs Les "?" du WHERE  (ex. : { "1" })
   * @param   sortOrder     Le ORDER BY       (ex. : "date ASC")
   * @return  Le {@link Cursor} du SELECT
   *
   * @see Cursor
   * @see SQLiteQueryBuilder
   * @see SQLiteDatabase
   */
  @Override
  public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    // On utilise SQLiteQueryBuilder plutôt que query()
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    switch (sURIMatcher.match(uri)) {
      case TACHES:
        checkColumnsTache(projection);

        queryBuilder.setTables(Tache.TABLE);
        break;
      case TACHE_ID:
        checkColumnsTache(projection);

        queryBuilder.setTables(Tache.TABLE);

        // On ajoute le ID au query
        queryBuilder.appendWhere(Tache.KEY_ID + "=" + uri.getLastPathSegment());
        break;
      case EMPLOYES:
        checkColumnsEmploye(projection);

        queryBuilder.setTables(Employe.TABLE);
        break;
      case EMPLOYE_ID:
        checkColumnsEmploye(projection);

        queryBuilder.setTables(Employe.TABLE);

        // On ajoute le ID au query
        queryBuilder.appendWhere(Employe.KEY_ID + "=" + uri.getLastPathSegment());
        break;
      default:
        // Oui, c'est en français. C'est moins compliqué qu'appelé getString() avec un context.
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }

    SQLiteDatabase  db      = dbHelper.getWritableDatabase();
    Cursor          cursor  = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

    // S'assurer que tous les Listeners soient notifiés
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  /**
   * Ajout dans la base de données
   *
   * <ul>
   *  <li>Vérifie l'Uri (tache/employé)</li>
   *  <li>Exécute le INSERT dans la bonne table</li>
   *  <li>Notifie les Listeners</li>
   * </ul>
   *
   * @param   uri    Uri envoyé pour l'ajout
   * @param   values Valeurs à ajouter dans la base de données
   * @return  L'{@link Uri} du INSERT
   *
   * @see SQLiteDatabase
   */
  @Override
  public Uri insert(@NonNull Uri uri, ContentValues values) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    long id = 0;

    switch (sURIMatcher.match(uri)) {
      case TACHES:
        id = db.insert(Tache.TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH_TACHE + "/" + id);
      case EMPLOYES:
        id = db.insert(Employe.TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH_EMPLOYE + "/" + id);
      default:
        // Oui, c'est en français. C'est moins compliqué qu'appelé getString() avec un context.
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }
  }

  /**
   * Suppression dans la base de données
   *
   * <ul>
   *  <li>Vérifie l'Uri (tache/employé et tout sélectionné/un seul)</li>
   *  <li>Supprime l'item ou les items dans la base de données</li>
   * </ul>
   *
   * @param   uri           Uri envoyé pour la suppression
   * @param   selection     Le WHERE          (ex. : "_id =?")
   * @param   selectionArgs Les "?" du WHERE  (ex. : { "1" })
   * @return  Le nombre de rangée supprimées
   *
   * @see SQLiteDatabase
   */
  @Override
  public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    int rangeesSupprimees = 0;

    switch (sURIMatcher.match(uri)) {
      case TACHES:
        rangeesSupprimees = db.delete(Tache.TABLE, selection, selectionArgs);
        break;
      case TACHE_ID:
        if (selection.isEmpty()) {
          rangeesSupprimees = db.delete(Tache.TABLE, Tache.KEY_ID + "=" + uri.getLastPathSegment(),                       null);
        } else {
          rangeesSupprimees = db.delete(Tache.TABLE, Tache.KEY_ID + "=" + uri.getLastPathSegment() + " and " + selection, selectionArgs);
        }
        break;
      case EMPLOYES:
        rangeesSupprimees = db.delete(Employe.TABLE, selection, selectionArgs);
        break;
      case EMPLOYE_ID:
        if (selection.isEmpty()) {
          rangeesSupprimees = db.delete(Employe.TABLE, Employe.KEY_ID + "=" + uri.getLastPathSegment(),                       null);
        } else {
          rangeesSupprimees = db.delete(Employe.TABLE, Employe.KEY_ID + "=" + uri.getLastPathSegment() + " and " + selection, selectionArgs);
        }
        break;
      default:
        // Oui, c'est en français. C'est moins compliqué qu'appelé getString() avec un context.
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }

    // S'assurer que tous les Listeners soient notifiés
    getContext().getContentResolver().notifyChange(uri, null);

    return rangeesSupprimees;
  }

  /**
   * Modification dans la base de données
   *
   * <ul>
   *  <li>Vérifie l'Uri (tache/employé et tout sélectionné/un seul), modifie l'item ou les items
   *  dans la base de données</li>
   * </ul>
   *
   * @param  uri Uri envoyé pour la modification
   * @param  selection Le WHERE          (ex. : "_id =?")
   * @param  selectionArgs Les "?" du WHERE  (ex. : { "1" })
   * @return Le nombre de rangée modifiées
   *
   * @see SQLiteDatabase
   */
  @Override
  public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

    int rangeesModifiees = 0;

    switch (sURIMatcher.match(uri)) {
      case TACHES:
        rangeesModifiees = sqlDB.update(Tache.TABLE, values, selection, selectionArgs);
        break;
      case TACHE_ID:
        if (selection.isEmpty()) {
          rangeesModifiees = sqlDB.update(Tache.TABLE, values, Tache.KEY_ID + "=" + uri.getLastPathSegment(),                       null);
        } else {
          rangeesModifiees = sqlDB.update(Tache.TABLE, values, Tache.KEY_ID + "=" + uri.getLastPathSegment() + " and " + selection, selectionArgs);
        }
        break;
      case EMPLOYES:
        rangeesModifiees = sqlDB.update(Employe.TABLE, values, selection, selectionArgs);
        break;
      case EMPLOYE_ID:
        if (selection.isEmpty()) {
          rangeesModifiees = sqlDB.update(Employe.TABLE, values, Employe.KEY_ID + "=" + uri.getLastPathSegment(),                       null);
        } else {
          rangeesModifiees = sqlDB.update(Employe.TABLE, values, Employe.KEY_ID + "=" + uri.getLastPathSegment() + " and " + selection, selectionArgs);
        }
        break;
      default:
        // Oui, c'est en français. C'est moins compliqué qu'appelé getString() avec un context.
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }

    // S'assurer que tous les Listeners soient notifiés
    getContext().getContentResolver().notifyChange(uri, null);

    return rangeesModifiees;
  }

  /**
   * Retourne le type d'un {@link Uri}
   *
   * @param   uri {@link Uri} à vérifier le type
   * @return  String du type de l'Uri
   */
  @Override
  public String getType(@NonNull Uri uri) {
    switch (sURIMatcher.match(uri)) {
      case TACHES:
        return CONTENT_TYPE_TACHES;
      case TACHE_ID:
        return CONTENT_ITEM_TYPE_TACHE;
      case EMPLOYES:
        return CONTENT_TYPE_EMPLOYES;
      case EMPLOYE_ID:
        return CONTENT_ITEM_TYPE_EMPLOYE;
      default:
        return null;
    }
  }

  /**
   * Vérifie si les colonnes d'une projection de tâche sont valide
   *
   * @param projection Projection à vérifier
   */
  private void checkColumnsTache(String[] projection) {
    String[] available = {  Tache.KEY_ID,
                            Tache.KEY_employe_assigne_ID,
                            Tache.KEY_nom,
                            Tache.KEY_description,
                            Tache.KEY_fait,
                            Tache.KEY_date,
                            Tache.KEY_urgence};

    checkColumns(projection, available);
  }

  /**
   * Vérifie si les colonnes d'une projection d'employé sont valide
   *
   * @param projection Projection à vérifier
   */
  private void checkColumnsEmploye(String[] projection) {
    String[] available = {  Employe.KEY_ID,
                            Employe.KEY_nom,
                            Employe.KEY_poste,
                            Employe.KEY_email,
                            Employe.KEY_telephone};

    checkColumns(projection, available);
  }

  /**
   * Vérifie si les colonnes d'une projection sont valides avec un tableau de String à cet effet
   *
   * @param projection  Projection à vérifier
   * @param available   Tableau de String pour comparer à la projection
   */
  private void checkColumns(String[] projection, String[] available) {
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<>( Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<>( Arrays.asList(available));

      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException(
          // Oui, c'est en français. C'est moins compliqué qu'appelé getString() avec un context.
          "Colonne inconnue dans la projection");
      }
    }
  }
}
