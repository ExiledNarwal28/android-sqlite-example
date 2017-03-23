package net.info420.fabien.androidtravailpratique.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

// Source : http://instinctcoder.com/android-studio-sqlite-database-example/

public class EmployeeRepo {
  private DBHelper dbHelper;

  public EmployeeRepo(Context context) {
    dbHelper = new DBHelper(context);
  }

  public int insert(Employee employee) {

    // Ouvrir la connexion pour insérer des données
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Employee.KEY_name,   employee.name);
    values.put(Employee.KEY_job,    employee.job);
    values.put(Employee.KEY_email,  employee.email);
    values.put(Employee.KEY_phone,  employee.phone);

    // Insertion
    long employee_Id = db.insert(Employee.TABLE, null, values);
    db.close(); // Fermeture de la connexion avec la base de données
    return (int) employee_Id;
  }

  public void delete(int employee_Id) {

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    // Il est préférable d'utiliser le paramètre ? au lieu d'une concatenation
    db.delete(Employee.TABLE, Employee.KEY_ID + "= ?", new String[] { String.valueOf(employee_Id) });
    db.close(); // Fermeture de la connexion avec la base de données
  }

  public void update(Employee employee) {

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Employee.KEY_name,   employee.name);
    values.put(Employee.KEY_job,    employee.job);
    values.put(Employee.KEY_email,  employee.email);
    values.put(Employee.KEY_phone,  employee.phone);

    // Il est préférable d'utiliser le paramètre ? au lieu d'une concatenation
    db.update(Employee.TABLE, values, Employee.KEY_ID + "= ?", new String[] { String.valueOf(employee.employee_ID) });
    db.close(); // Fermeture de la connexion avec la base de données
  }

  // Ceci sert à aller chercher une liste des employés (les noms) pour les Spinners
  public ArrayList<HashMap<String, String>> getEmployeeList() {
    // Ouverture de la connexion en read only
    SQLiteDatabase db     = dbHelper.getReadableDatabase();
    String selectQuery    =  "SELECT  " +
      Employee.KEY_ID     + "," +
      Employee.KEY_name   + "," +
      Employee.KEY_job    + "," +
      Employee.KEY_email  + "," +
      Employee.KEY_phone  +
      " FROM "            + Employee.TABLE;

    // Liste des noms des employés
    ArrayList<HashMap<String, String>> employeeList = new ArrayList<HashMap<String, String>>();

    Cursor cursor = db.rawQuery(selectQuery, null);
    // On itère dans les données en remplissant la liste

    if (cursor.moveToFirst()) {
      do {
        HashMap<String, String> employee = new HashMap<String, String>();
        employee.put("id", cursor.getString(cursor.getColumnIndex(Employee.KEY_ID)));
        employee.put("name", cursor.getString(cursor.getColumnIndex(Employee.KEY_name)));
        employeeList.add(employee);

      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return employeeList;

  }

  public Employee getEmployeeById(int Id){
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    String selectQuery    =  "SELECT  "     +
      Employee.KEY_ID     + ","             +
      Employee.KEY_name   + ","             +
      Employee.KEY_job    + ","             +
      Employee.KEY_email  + ","             +
      Employee.KEY_phone  +
      " FROM "            + Employee.TABLE  +
      " WHERE "           + Employee.KEY_ID + "=?"; // Il est préférable d'utiliser le paramètre ? au lieu d'une concatenation

    Employee employee = new Employee();

    Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

    if (cursor.moveToFirst()) {
      do {
        employee.employee_ID  = cursor.getInt(cursor.getColumnIndex(Employee.KEY_ID));
        employee.name         = cursor.getString(cursor.getColumnIndex(Employee.KEY_name));
        employee.job          = cursor.getString(cursor.getColumnIndex(Employee.KEY_job));
        employee.email        = cursor.getString(cursor.getColumnIndex(Employee.KEY_email));
        employee.phone        = cursor.getString(cursor.getColumnIndex(Employee.KEY_phone));

      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return employee;
  }
}