package net.info420.fabien.androidtravailpratique.models;

/**
 * Created by fabien on 17-03-23.
 */

// Source : http://instinctcoder.com/android-studio-sqlite-database-example/

public class Employee {
  // Nom de la table
  public static final String TABLE = "Employee";

  // Noms des colonnes de la table
  public static final String KEY_ID     = "_id";
  public static final String KEY_name   = "name";
  public static final String KEY_job    = "job";
  public static final String KEY_email  = "email";
  public static final String KEY_phone  = "phone";

  // Nom des propriétés
  public int    employee_ID;
  public String name;
  public String job;
  public String email;
  public String phone;
}