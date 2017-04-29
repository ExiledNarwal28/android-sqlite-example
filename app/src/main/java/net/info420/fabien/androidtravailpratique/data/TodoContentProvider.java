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
import android.text.TextUtils;

import net.info420.fabien.androidtravailpratique.models.Employe;
import net.info420.fabien.androidtravailpratique.models.Tache;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by fabien on 17-03-26.
 */

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class TodoContentProvider extends ContentProvider {
  private final static String TAG = TodoContentProvider.class.getName();

  // Base de données
  private DBHelper dbHelper;

  private static final String AUTHORITY = "net.info420.fabien.androidtravailpratique.data";

  // Utilisé pour le UriMatcher
  private static final int TASKS        = 10;
  private static final int TASK_ID      = 20;
  private static final int EMPLOYEES    = 30;
  private static final int EMPLOYEE_ID  = 40;

  private static final String BASE_PATH_TASK          = "tasks";
  private static final String BASE_PATH_EMPLOYEE      = "employees";
  public  static final Uri CONTENT_URI_TACHE = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_TASK);
  public  static final Uri CONTENT_URI_EMPLOYE = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_EMPLOYEE);

  public static final String CONTENT_TYPE_TASK          = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/tasks";
  public static final String CONTENT_ITEM_TYPE_TACHE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/task";
  public static final String CONTENT_TYPE_EMPLOYEE      = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/employees";
  public static final String CONTENT_ITEM_TYPE_EMPLOYE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/employee";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH_TASK,              TASKS);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH_TASK + "/#",       TASK_ID);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH_EMPLOYEE,         EMPLOYEES);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH_EMPLOYEE + "/#",  EMPLOYEE_ID);
  }

  @Override
  public boolean onCreate() {
    dbHelper = new DBHelper(getContext());

    return false;
  }

  @Override
  public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    // On utilise SQLiteQueryBuilder plutôt que query()
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // Log.d(TAG, String.format("Uri recu : %s", uri.getPath()));

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
      case TASKS:
        // On vérifie si la colonne existe (sécurité)
        checkColumnsTask(projection);

        // On prépare la table
        queryBuilder.setTables(Tache.TABLE);
        break;
      case TASK_ID:
        // On vérifie si la colonne existe (sécurité)
        checkColumnsTask(projection);

        // On prépare la table
        queryBuilder.setTables(Tache.TABLE);

        // On ajoute le ID au query
        queryBuilder.appendWhere(Tache.KEY_ID + "=" + uri.getLastPathSegment());
        break;
      case EMPLOYEES:
        // On vérifie si la colonne existe (sécurité)
        checkColumnsEmployee(projection);

        // On prépare la table
        queryBuilder.setTables(Employe.TABLE);
        break;
      case EMPLOYEE_ID:
        // On vérifie si la colonne existe (sécurité)
        checkColumnsEmployee(projection);

        // On prépare la table
        queryBuilder.setTables(Employe.TABLE);

        // On ajoute le ID au query
        queryBuilder.appendWhere(Employe.KEY_ID + "=" + uri.getLastPathSegment());
        break;
      default:
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    // S'assurer que tous les Listeners soient notifiés
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(@NonNull Uri uri) {
    return null;
  }

  @Override
  public Uri insert(@NonNull Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
    long id = 0;
    switch (uriType) {
      case TASKS:
        id = sqlDB.insert(Tache.TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);

        // Log.d(TAG, String.format("Insertion de la tâche #%s", id));

        return Uri.parse(BASE_PATH_TASK + "/" + id);
      case EMPLOYEES:
        id = sqlDB.insert(Employe.TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);

        // Log.d(TAG, String.format("Insertion de l'employé #%s", id));

        return Uri.parse(BASE_PATH_EMPLOYEE + "/" + id);
      default:
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }
  }

  @Override
  public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
    int rowsDeleted = 0;
    String id;
    switch (uriType) {
      case TASKS:
        rowsDeleted = sqlDB.delete(Tache.TABLE, selection, selectionArgs);
        break;
      case TASK_ID:
        id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          rowsDeleted = sqlDB.delete(Tache.TABLE, Tache.KEY_ID + "=" + id, null);
        } else {
          rowsDeleted = sqlDB.delete(Tache.TABLE, Tache.KEY_ID + "=" + id + " and " + selection, selectionArgs);
        }
        break;
      case EMPLOYEES:
        rowsDeleted = sqlDB.delete(Employe.TABLE, selection, selectionArgs);
        break;
      case EMPLOYEE_ID:
        id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          rowsDeleted = sqlDB.delete(Employe.TABLE, Employe.KEY_ID + "=" + id, null);
        } else {
          rowsDeleted = sqlDB.delete(Employe.TABLE, Employe.KEY_ID + "=" + id + " and " + selection, selectionArgs);
        }
        break;
      default:
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
    int rowsUpdated = 0;
    String id;
    switch (uriType) {
      case TASKS:
        rowsUpdated = sqlDB.update(Tache.TABLE, values, selection, selectionArgs);
        break;
      case TASK_ID:
        id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          rowsUpdated = sqlDB.update(Tache.TABLE, values, Tache.KEY_ID + "=" + id, null);
        } else {
          rowsUpdated = sqlDB.update(Tache.TABLE, values, Tache.KEY_ID + "=" + id + " and " + selection, selectionArgs);
        }
        break;
      case EMPLOYEES:
        rowsUpdated = sqlDB.update(Employe.TABLE, values, selection, selectionArgs);
        break;
      case EMPLOYEE_ID:
        id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          rowsUpdated = sqlDB.update(Employe.TABLE, values, Employe.KEY_ID + "=" + id, null);
        } else {
          rowsUpdated = sqlDB.update(Employe.TABLE, values, Employe.KEY_ID + "=" + id + " and " + selection, selectionArgs);
        }
        break;
      default:
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  private void checkColumnsTask(String[] projection) {
    String[] available = {  Tache.KEY_ID,
                            Tache.KEY_employe_assigne_ID,
                            Tache.KEY_nom,
                            Tache.KEY_description,
                            Tache.KEY_fait,
                            Tache.KEY_date,
                            Tache.KEY_urgence};

    checkColumns(projection, available);
  }

  private void checkColumnsEmployee(String[] projection) {
    String[] available = {  Employe.KEY_ID,
                            Employe.KEY_nom,
                            Employe.KEY_poste,
                            Employe.KEY_email,
                            Employe.KEY_telephone};

    checkColumns(projection, available);
  }

  private void checkColumns(String[] projection, String[] available) {
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(
        Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(
        Arrays.asList(available));
      // check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException(
          "Colonne inconnue dans la projection");
      }
    }
  }
}
