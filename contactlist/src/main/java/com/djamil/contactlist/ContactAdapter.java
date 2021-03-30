package com.djamil.contactlist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.djamil.contactlist.interfaces.OnClickCantactListener;
import com.djamil.fastscroll.SectionTitleProvider;
import java.util.ArrayList;
import java.util.List;
/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactVH> implements SectionTitleProvider, Filterable {
    private List<ContactsInfo> dataList;
    private List<ContactsInfo> dataListFiltered;
    private String letter;
    private Activity activity;
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private OnClickCantactListener onClickCantactListener;


    /*int colors[] = {R.color.red, R.color.pink, R.color.purple, R.color.deep_purple,
            R.color.indigo, R.color.blue, R.color.light_blue, R.color.cyan, R.color.teal, R.color.green,
            R.color.light_green, R.color.lime, R.color.yellow, R.color.amber, R.color.orange, R.color.deep_orange};*/

    ContactAdapter(Activity activity, ArrayList<ContactsInfo> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        this.dataListFiltered = dataList;

    }

    @Override
    public ContactVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list_item, viewGroup, false);
        return new ContactVH(view);
    }

    @Override
    public void onBindViewHolder(ContactVH contactVH, final int i) {
        contactVH.contactName.setText(dataListFiltered.get(i).getDisplayName());

        if (dataListFiltered.get(i).getPhoneNumberList() != null && dataListFiltered.get(i).getPhoneNumberList().get(0) != null){
            contactVH.phoneNumber.setText(dataListFiltered.get(i).getPhoneNumberList().get(0));
            dataListFiltered.get(i).setPhoneNumber(dataListFiltered.get(i).getPhoneNumberList().get(0));
        }
        //checkCountryCode(dataList.get(i));

//        Get the first letter of list item
        letter = String.valueOf(dataListFiltered.get(i).getDisplayName().charAt(0));

//        Create a new TextDrawable for our image's background
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());

        contactVH.letter.setImageDrawable(drawable);

        contactVH.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContactList.onClickCantactListener != null){
                    ContactList.onClickCantactListener.onClickCantact(v, dataListFiltered.get(i));
                    activity.finish();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataListFiltered == null ? 0 : dataListFiltered.size();
    }

    @Override
    public String getSectionTitle(int position) {
        //this String will be shown in a bubble for specified position
        return String.valueOf(dataListFiltered.get(position).getDisplayName().charAt(0));
    }

    class ContactVH extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView phoneNumber;
        ImageView letter;
        LinearLayout linearLayout;

        ContactVH(View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.gmailitem_letter);
            contactName = itemView.findViewById(R.id.item_title);
            phoneNumber = itemView.findViewById(R.id.item_number);
            linearLayout = itemView.findViewById(R.id.layout);
        }
    }

    void setOnClickCantactListener(OnClickCantactListener onClickCantactListener) {
        this.onClickCantactListener = onClickCantactListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = dataList;
                } else {
                    List<ContactsInfo> filteredList = new ArrayList<>();
                    for (ContactsInfo row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if ((row.getDisplayName() != null && row.getDisplayName().toLowerCase().contains(charString.toLowerCase())) || (row.getPhoneNumber() != null && row.getPhoneNumber().replaceAll("\\s", "").replace("-", "").contains(charSequence))) {
                            filteredList.add(row);
                        }
                    }

                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList<ContactsInfo>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}


