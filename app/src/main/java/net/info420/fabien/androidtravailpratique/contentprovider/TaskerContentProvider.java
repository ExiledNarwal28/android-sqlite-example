package net.info420.fabien.androidtravailpratique.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import net.info420.fabien.androidtravailpratique.utils.DBHelper;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.Task;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by fabien on 17-03-26.
 */

// Source : http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class TaskerContentProvider extends ContentProvider {
  private final static String TAG = TaskerContentProvider.class.getName();

  // Base de données
  private DBHelper database;

  private static final String AUTHORITY = "net.info420.fabien.androidtravailpratique.contentprovider";

  // Utilisé pour le UriMatcher
  private static final int TASKS        = 10;
  private static final int TASK_ID      = 20;
  private static final int EMPLOYEES    = 30;
  private static final int EMPLOYEE_ID  = 40;

  private static final String BASE_PATH_TASK      = "tasks";
  private static final String BASE_PATH_EMPLOYEE  = "employees";
  public static final Uri CONTENT_URI_TASK        = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_TASK);
  public static final Uri CONTENT_URI_EMPLOYEES   = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_EMPLOYEE);

  public static final String CONTENT_TYPE_TASK          = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/tasks";
  public static final String CONTENT_ITEM_TYPE_TASK     = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/task";
  public static final String CONTENT_TYPE_EMPLOYEE      = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/employees";
  public static final String CONTENT_ITEM_TYPE_EMPLOYEE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/employee";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

   static {
     sURIMatcher.addURI(AUTHORITY, BASE_PATH_TASK,              TASKS);
     sURIMatcher.addURI(AUTHORITY, BASE_PATH_TASK + "/#",       TASK_ID);
     sURIMatcher.addURI(AUTHORITY, BASE_PATH_EMPLOYEE,         EMPLOYEES);
     sURIMatcher.addURI(AUTHORITY, BASE_PATH_EMPLOYEE + "/#",  EMPLOYEE_ID);
  }

  @Override
  public boolean onCreate() {
    database = new DBHelper(getContext());
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    // On utilise SQLiteQueryBuilder plutôt que query()
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();



    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
      case TASKS:
        // On vérifie si la colonne existe (sécurité)
        checkColumnsTask(projection);

        // On prépare la table
        queryBuilder.setTables(Task.TABLE);
        break;
      case TASK_ID:
        // On vérifie si la colonne existe (sécurité)
        checkColumnsTask(projection);

        // On prépare la table
        queryBuilder.setTables(Task.TABLE);

        // On ajoute le ID au query
        queryBuilder.appendWhere(Task.KEY_ID + "=" + uri.getLastPathSegment());
        break;
      case EMPLOYEES:
        // On vérifie si la colonne existe (sécurité)
        checkColumnsEmployee(projection);

        // On prépare la table
        queryBuilder.setTables(Employee.TABLE);
        break;
      case EMPLOYEE_ID:
        // On vérifie si la colonne existe (sécurité)
        checkColumnsEmployee(projection);

        // On prépare la table
        queryBuilder.setTables(Employee.TABLE);

        // On ajoute le ID au query
        queryBuilder.appendWhere(Employee.KEY_ID + "=" + uri.getLastPathSegment());
        break;
      default:
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    // S'assurer que tous les Listeners soient notifiés
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Nullable
  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    long id = 0;
    switch (uriType) {
      case TASKS:
        id = sqlDB.insert(Task.TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH_TASK + "/" + id);
      case EMPLOYEES:
        id = sqlDB.insert(Employee.TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH_EMPLOYEE + "/" + id);
      default:
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    String id;
    switch (uriType) {
      case TASKS:
        rowsDeleted = sqlDB.delete(Task.TABLE, selection, selectionArgs);
        break;
      case TASK_ID:
        id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          rowsDeleted = sqlDB.delete(Task.TABLE, Task.KEY_ID + "=" + id, null);
        } else {
          rowsDeleted = sqlDB.delete(Task.TABLE, Task.KEY_ID + "=" + id + " and " + selection, selectionArgs);
        }
        break;
      case EMPLOYEES:
        rowsDeleted = sqlDB.delete(Employee.TABLE, selection, selectionArgs);
        break;
      case EMPLOYEE_ID:
        id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          rowsDeleted = sqlDB.delete(Employee.TABLE, Employee.KEY_ID + "=" + id, null);
        } else {
          rowsDeleted = sqlDB.delete(Employee.TABLE, Employee.KEY_ID + "=" + id + " and " + selection, selectionArgs);
        }
        break;
      default:
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    String id;
    switch (uriType) {
      case TASKS:
        rowsUpdated = sqlDB.update(Task.TABLE, values, selection, selectionArgs);
        break;
      case TASK_ID:
        id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          rowsUpdated = sqlDB.update(Task.TABLE, values, Task.KEY_ID + "=" + id, null);
        } else {
          rowsUpdated = sqlDB.update(Task.TABLE, values, Task.KEY_ID + "=" + id + " and " + selection, selectionArgs);
        }
        break;
      case EMPLOYEES:
        rowsUpdated = sqlDB.update(Employee.TABLE, values, selection, selectionArgs);
        break;
      case EMPLOYEE_ID:
        id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          rowsUpdated = sqlDB.update(Employee.TABLE, values, Employee.KEY_ID + "=" + id, null);
        } else {
          rowsUpdated = sqlDB.update(Employee.TABLE, values, Employee.KEY_ID + "=" + id + " and " + selection, selectionArgs);
        }
        break;
      default:
        throw new IllegalArgumentException("URI inconnu : " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  private void checkColumnsTask(String[] projection) {
    String[] available = {  Task.KEY_ID,
                            Task.KEY_assigned_employee_ID,
                            Task.KEY_name,
                            Task.KEY_description,
                            Task.KEY_completed,
                            Task.KEY_date,
                            Task.KEY_urgency_level};

    checkColumns(projection, available);
  }

  private void checkColumnsEmployee(String[] projection) {
    String[] available = {  Employee.KEY_ID,
                            Employee.KEY_name,
                            Employee.KEY_job,
                            Employee.KEY_email,
                            Employee.KEY_phone};

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
