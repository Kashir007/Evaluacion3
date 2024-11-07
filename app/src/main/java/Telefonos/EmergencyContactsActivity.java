package Telefonos;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.niko.pruebaurgencias3.R;
import java.util.ArrayList;
import java.util.List;
import SQL.DBHelper;

public class EmergencyContactsActivity extends AppCompatActivity implements EmergencyContactAdapter.OnContactActionListener {

    private RecyclerView recyclerView;
    private EmergencyContactAdapter adapter;
    private List<EmergencyContact> contactsList;
    private FloatingActionButton fabAddContact;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.llamadas_emergencia);

        recyclerView = findViewById(R.id.recyclerViewEmergency);
        fabAddContact = findViewById(R.id.fabAddContact);  // Asegúrate de que el ID coincide
        contactsList = new ArrayList<>();
        dbHelper = new DBHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmergencyContactAdapter(contactsList, this, this);
        recyclerView.setAdapter(adapter);

        loadDefaultContacts();
        loadContactsFromDB();

        fabAddContact.setOnClickListener(v -> showAddContactDialog(null));  // Configura el botón de agregar contacto
    }

    private void loadDefaultContacts() {
        if (dbHelper.getAllContacts().getCount() == 0) {
            dbHelper.addContact("Carabineros", "133");
            dbHelper.addContact("Ambulancia", "131");
            dbHelper.addContact("Bomberos", "132");
            dbHelper.addContact("Policía de Investigaciones", "134");
            loadContactsFromDB(); // Cargar la lista actualizada
        }
    }

    private void showAddContactDialog(EmergencyContact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(contact == null ? "Agregar Contacto de Emergencia" : "Editar Contacto de Emergencia");

        View dialogView = getLayoutInflater().inflate(R.layout.agregar_contacto, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.editTextName);
        EditText phoneInput = dialogView.findViewById(R.id.editTextPhone);

        // Rellena los campos si es un contacto existente
        if (contact != null) {
            nameInput.setText(contact.getName());
            phoneInput.setText(contact.getPhoneNumber());
        }

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                if (contact == null) {
                    // Agregar un nuevo contacto
                    dbHelper.addContact(name, phone);
                } else {
                    // Editar el contacto existente
                    try {
                        dbHelper.updateContact(contact.getId(), name, phone);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error al actualizar el contacto", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                loadContactsFromDB();
                Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Por favor ingresa nombre y teléfono", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onEditContact(EmergencyContact contact) {
        showAddContactDialog(contact);  // Abre el diálogo de agregar con los datos del contacto a editar
    }

    @Override
    public void onDeleteContact(EmergencyContact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar contacto")
                .setMessage("¿Estás seguro de que deseas eliminar este contacto?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    dbHelper.deleteContact(contact.getId());
                    loadContactsFromDB();
                    Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void loadContactsFromDB() {
        contactsList.clear();
        Cursor cursor = dbHelper.getAllContacts();
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndexOrThrow("id");
            int nameIndex = cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME);
            int phoneIndex = cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PHONE);

            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String phone = cursor.getString(phoneIndex);
                contactsList.add(new EmergencyContact(id, name, phone));
            } while (cursor.moveToNext());

            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }
}
