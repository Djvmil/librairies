package com.djamil.contactlist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactVH> implements SectionTitleProvider {
    private List<ContactsInfo> dataList;
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

    }

    @Override
    public ContactVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list_item, viewGroup, false);
        return new ContactVH(view);
    }

    @Override
    public void onBindViewHolder(ContactVH contactVH, final int i) {
        contactVH.contactName.setText(dataList.get(i).getDisplayName());

        if (dataList.get(i).getPhoneNumberList() != null && dataList.get(i).getPhoneNumberList().get(0) != null){
            contactVH.phoneNumber.setText(dataList.get(i).getPhoneNumberList().get(0));
            dataList.get(i).setPhoneNumber(dataList.get(i).getPhoneNumberList().get(0));
        }
        //checkCountryCode(dataList.get(i));

//        Get the first letter of list item
        letter = String.valueOf(dataList.get(i).getDisplayName().charAt(0));

//        Create a new TextDrawable for our image's background
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());

        contactVH.letter.setImageDrawable(drawable);

        contactVH.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContactList.onClickCantactListener != null){
                    ContactList.onClickCantactListener.onClickCantact(v, dataList.get(i));
                    activity.finish();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public String getSectionTitle(int position) {
        //this String will be shown in a bubble for specified position
        return String.valueOf(dataList.get(position).getDisplayName().charAt(0));
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

    void checkCountryCode(ContactsInfo contactsInfo){
        String number = contactsInfo.getPhoneNumber();
        String code = number.substring(0, 4);

        if (code.contains("221")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+221", "") : number.replace("221", ""));
            contactsInfo.setIndicatif("+221");

        }else if (code.contains("+222") || code.contains("222")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+222", "") : number.replace("222", ""));
            contactsInfo.setIndicatif("+222");

        }else if (code.contains("+223") || code.contains("223")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+223", "") : number.replace("223", ""));
            contactsInfo.setIndicatif("+223");

        }else if (code.contains("+224") || code.contains("224")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+224", "") : number.replace("224", ""));
            contactsInfo.setIndicatif("+224");

        }else if (code.contains("+225") || code.contains("225")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+225", "") : number.replace("225", ""));
            contactsInfo.setIndicatif("+225");

        }else if (code.contains("226")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+226", "") : number.replace("226", ""));
            contactsInfo.setIndicatif("+226");

        }else if (code.contains("228")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+228", "") : number.replace("228", ""));
            contactsInfo.setIndicatif("+228");

        }else if (code.contains("229")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+229", "") : number.replace("229", ""));
            contactsInfo.setIndicatif("+229");

        }else if (code.contains("234")){
            contactsInfo.setPhoneNumber(code.contains("+") ? number.replace("+234", "") : number.replace("234", ""));
            contactsInfo.setIndicatif("+234");

        }
    }

}


