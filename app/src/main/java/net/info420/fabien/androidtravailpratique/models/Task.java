package net.info420.fabien.androidtravailpratique.models;

/**
 * Created by fabien on 17-03-23.
 */

// Source : http://instinctcoder.com/android-studio-sqlite-database-example/

public class Task {
  // Nom de la table
  public static final String TABLE = "Task";

  // Noms des colonnes de la table
  public static final String KEY_ID                     = "_id";
  public static final String KEY_assigned_employee_ID   = "assigned_employee_id_fk";
  public static final String KEY_name                   = "name";
  public static final String KEY_description            = "description";
  public static final String KEY_completed              = "completed";
  public static final String KEY_date                   = "date";
  public static final String KEY_urgency_level          = "urgency_level";

  // Nom des propriétés
  public int      task_ID;
  public int      assigned_employee_ID;
  public String   name;
  public String   description;
  public boolean  completed;
  public int      date; // date est un integer "Unix Time"
  public int      urgency_level;
}