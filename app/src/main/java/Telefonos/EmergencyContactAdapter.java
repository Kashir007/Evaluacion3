package Telefonos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.niko.pruebaurgencias3.R;

import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder> {

    private List<EmergencyContact> contactsList;
    private Context context;
    private OnContactActionListener listener;

    // Constructor del adaptador
    public EmergencyContactAdapter(List<EmergencyContact> contactsList, Context context, OnContactActionListener listener) {
        this.contactsList = contactsList;
        this.context = context;
        this.listener = listener;
    }

    // Interfaz para manejar acciones en los contactos
    public interface OnContactActionListener {
        void onEditContact(EmergencyContact contact);
        void onDeleteContact(EmergencyContact contact);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyContact contact = contactsList.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText(contact.getPhoneNumber());

        // Configura el toque para iniciar una llamada
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
            context.startActivity(intent);
        });

        // Configura el clic prolongado para abrir opciones de editar y eliminar
        holder.itemView.setOnLongClickListener(v -> {
            // Crear el PopupMenu y asociarlo con el ítem de vista
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenuInflater().inflate(R.menu.contact_options, popupMenu.getMenu());  // Usar el archivo de menú XML
            popupMenu.setOnMenuItemClickListener(item -> {
                // Cambié el switch por un if-else
                if (item.getItemId() == R.id.action_edit) {
                    listener.onEditContact(contact);  // Llamar a la acción de editar
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    listener.onDeleteContact(contact);  // Llamar a la acción de eliminar
                    return true;
                }
                return false;
            });
            popupMenu.show();  // Mostrar el menú emergente
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
        }
    }
}
