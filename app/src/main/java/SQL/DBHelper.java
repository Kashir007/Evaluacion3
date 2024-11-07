package SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_data.db"; // Nombre de la base de datos
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_NAME = "nombre";
    public static final String COLUMN_PHONE = "telefono";
    private static final String TABLE_CONTACTS = "contactos";

    // Sentencia SQL para crear la tabla
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_CONTACTS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si la base de datos cambia de versión, elimina la tabla y la vuelve a crear
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Método para insertar un nuevo contacto de emergencia
    public void addContact(String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // Método para obtener los contactos de emergencia
    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CONTACTS,
                new String[]{"id", COLUMN_NAME, COLUMN_PHONE}, // Agrega el campo "id" aquí
                null, null, null, null, null);
    }

    // Método para actualizar un contacto de emergencia
    public boolean updateContact(int id, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);

        int rowsAffected = db.update(TABLE_CONTACTS, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected > 0;
    }

    // Método para eliminar un contacto de emergencia
    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
