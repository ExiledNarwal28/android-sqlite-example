package net.info420.fabien.androidtravailpratique.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fabien on 17-03-23.
 */

public class TaskRepo {
  private DBHelper dbHelper;

  public TaskRepo(Context context) {
    dbHelper = new DBHelper(context);
  }

  public int insert(Task task) {
    /*
    public int      task_ID;
    public int      assigned_employee_ID;
    public String   name;
    public String   description;
    public boolean  completed;
    public int      date; // date est un integer "Unix Time"
    public int      urgency_level;
     */

    // Ouvrir la connexion pour insérer des données
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Task.KEY_assigned_employee_ID, task.assigned_employee_ID);
    values.put(Task.KEY_name,                 task.name);
    values.put(Task.KEY_description,          task.description);
    values.put(Task.KEY_completed,            task.completed);
    values.put(Task.KEY_date,                 task.date);
    values.put(Task.KEY_urgency_level,        task.urgency_level);

    // Insertion
    long task_Id = db.insert(Task.TABLE, null, values);
    db.close(); // Fermeture de la connexion avec la base de données
    return (int) task_Id;
  }

  public void delete(int task_Id) {

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    // Il est préférable d'utiliser le paramètre ? au lieu d'une concatenation
    db.delete(Task.TABLE, Task.KEY_ID + "= ?", new String[] { String.valueOf(task_Id) });
    db.close(); // Fermeture de la connexion avec la base de données
  }

  public void update(Task task) {

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Task.KEY_assigned_employee_ID, task.assigned_employee_ID);
    values.put(Task.KEY_name,                 task.name);
    values.put(Task.KEY_description,          task.description);
    values.put(Task.KEY_completed,            task.completed);
    values.put(Task.KEY_date,                 task.date);
    values.put(Task.KEY_urgency_level,        task.urgency_level);

    // Il est préférable d'utiliser le paramètre ? au lieu d'une concatenation
    db.update(Task.TABLE, values, Task.KEY_ID + "= ?", new String[] { String.valueOf(task.task_ID) });
    db.close(); // Fermeture de la connexion avec la base de données
  }

  // Ceci sert à aller chercher une liste des tâches
  public ArrayList<HashMap<String, String>> getEmployeeList() {
    // Ouverture de la connexion en read only
    SQLiteDatabase db               = dbHelper.getReadableDatabase();
    String selectQuery              =  "SELECT  " +
      Task.KEY_ID                   + "," +
      Task.KEY_assigned_employee_ID + "," +
      Task.KEY_name                 + "," +
      Task.KEY_description          + "," +
      Task.KEY_completed            + "," +
      Task.KEY_date                 + "," +
      Task.KEY_urgency_level        +
      " FROM "                      + Employee.TABLE;

    // Liste des noms des employés
    ArrayList<HashMap<String, String>> taskList = new ArrayList<HashMap<String, String>>();

    Cursor cursor = db.rawQuery(selectQuery, null);
    // On itère dans les données en remplissant la liste

    if (cursor.moveToFirst()) {
      do {
        HashMap<String, String> task = new HashMap<String, String>();
        task.put("id", cursor.getString(cursor.getColumnIndex(Task.KEY_ID)));
        task.put("name", cursor.getString(cursor.getColumnIndex(Task.KEY_name)));
        taskList.add(task);

      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return taskList;

  }

  public Task getTaskById(int Id){
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    String selectQuery                =  "SELECT  "     +
      Task.KEY_ID                     + ","             +
      Task.KEY_assigned_employee_ID   + ","             +
      Task.KEY_name                   + ","             +
      Task.KEY_description            + ","             +
      Task.KEY_completed              + ","             +
      Task.KEY_date                   + ","             +
      Task.KEY_urgency_level          +
      " FROM "                        + Task.TABLE  +
      " WHERE " + Task.KEY_ID   + "=?"; // Il est préférable d'utiliser le paramètre ? au lieu d'une concatenation

    Task task = new Task();

    Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

    if (cursor.moveToFirst()) {
      do {
        task.task_ID              = cursor.getInt(cursor.getColumnIndex(Task.KEY_ID));
        task.assigned_employee_ID = cursor.getInt(cursor.getColumnIndex(Task.KEY_assigned_employee_ID));
        task.name                 = cursor.getString(cursor.getColumnIndex(Task.KEY_name));
        task.description          = cursor.getString(cursor.getColumnIndex(Task.KEY_description));
        task.completed            = (cursor.getInt(cursor.getColumnIndex(Task.KEY_completed)) == 1); // conversion en booléen
        task.date                 = cursor.getInt(cursor.getColumnIndex(Task.KEY_date));
        task.urgency_level        = cursor.getInt(cursor.getColumnIndex(Task.KEY_urgency_level));

      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return task;
  }
}