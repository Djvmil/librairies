package com.djamil.contactlist

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.djamil.contactlist.interfaces.OnClickContactListener
import com.djamil.contactlist.interfaces.OnMultipleActive
import com.djamil.fastscroll.SectionTitleProvider
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
class ContactAdapter internal constructor(private val activity: Activity, dataList: ArrayList<ContactsInfo>) : RecyclerView.Adapter<ContactAdapter.ContactVH>(), SectionTitleProvider, Filterable {
    private val dataList: List<ContactsInfo>
    private var dataListFiltered: List<ContactsInfo>?
    private var listMultiple: ArrayList<ContactsInfo>? = null
    private var letter: String? = null
    private val generator = ColorGenerator.MATERIAL
    private var onClickContactListener: OnClickContactListener? = null
    var onMultipleActive: OnMultipleActive? = null
    private var sDialog: SweetAlertDialog? = null
    private var isMutiple = false


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ContactVH {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.contact_list_item, viewGroup, false)
        return ContactVH(view)
    }

    override fun onBindViewHolder(contactVH: ContactVH, i: Int) {
        contactVH.contactName.text = dataListFiltered!![i].displayName


        if (dataListFiltered!![i].phoneNumberList != null) {
            var nums = ""
            dataListFiltered!![i].phoneNumberList.forEach{ item->
                nums += item.plus("/")
            }
            contactVH.phoneNumber.text = nums.substring(0, nums.length - 1)
            dataListFiltered!![i].phoneNumber = dataListFiltered!![i].phoneNumberList[0]
        }
        //checkCountryCode(dataList.get(i));

//        Get the first letter of list item
        letter = dataListFiltered!![i].displayName[0].toString()


//        Create a new TextDrawable for our image's background
        val drawable = TextDrawable.builder().buildRound(letter, generator.randomColor)
        contactVH.letter.setImageDrawable(drawable)


        contactVH.checkBox.visibility = if (isMutiple) View.VISIBLE else View.GONE

        contactVH.linearLayout.setOnLongClickListener {
            if (ContactList.multiContact){
                onMultipleActive?.isActive(true)
                isMutiple = true
                listMultiple = ArrayList()
                notifyDataSetChanged()
            }

            return@setOnLongClickListener true
        }

        contactVH.linearLayout.setOnClickListener { v ->
            if (!isMutiple && ContactList.onClickContactListener != null) {
                if (dataListFiltered!![i].phoneNumberList != null && dataListFiltered!![i].phoneNumberList.size > 1){
                    numberChoiceDialog(v, dataListFiltered!![i])

                }else{
                    ContactList.onClickContactListener.onClickContact(v, dataListFiltered!![i])
                    activity.finish()
                }
            }else{
                contactVH.checkBox.isChecked = !contactVH.checkBox.isChecked

                if (contactVH.checkBox.isChecked){
                    if (dataListFiltered!![i].phoneNumberList != null && dataListFiltered!![i].phoneNumberList.size > 1){
                        numberChoiceDialog(v, dataListFiltered!![i], contactVH.checkBox.isChecked)
                    }else
                        listMultiple?.add(dataListFiltered!![i])

                }else
                    listMultiple?.remove(dataListFiltered!![i])

            }
        }
    }

    fun cleanSelect(){
        isMutiple = false
        listMultiple?.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (dataListFiltered == null) 0 else dataListFiltered!!.size
    }

    override fun getSectionTitle(position: Int): String {
        //this String will be shown in a bubble for specified position
        return dataListFiltered!![position].displayName[0].toString()
    }

    inner class ContactVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contactName: TextView
        var phoneNumber: TextView
        var letter: ImageView
        var checkBox: CheckBox
        var linearLayout: LinearLayout

        init {
            letter = itemView.findViewById(R.id.gmailitem_letter)
            contactName = itemView.findViewById(R.id.item_title)
            phoneNumber = itemView.findViewById(R.id.item_number)
            linearLayout = itemView.findViewById(R.id.layout)
            checkBox = itemView.findViewById(R.id.checkbox)
        }
    }

    fun setOnClickCantactListener(onClickContactListener: OnClickContactListener?) {
        this.onClickContactListener = onClickContactListener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                dataListFiltered = if (charString.isEmpty()) {
                    dataList
                } else {
                    val filteredList: MutableList<ContactsInfo> = ArrayList()
                    for (row in dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.displayName != null && row.displayName.toLowerCase().contains(charString.toLowerCase()) || row.phoneNumber != null && row.phoneNumber.replace("\\s".toRegex(), "").replace("-", "").contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = dataListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                dataListFiltered = filterResults.values as ArrayList<ContactsInfo>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }


    private fun numberChoiceDialog(view: View, contactsInfo: ContactsInfo, isChecked: Boolean = false){
        sDialog = SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE)
        //sDialog?.titleText = activity.getString(R.string.tirage_lbl)
        sDialog?.setCancelable(true)

        val layout = activity.layoutInflater.inflate(R.layout.layout_number_list, null)

        val listView = layout.findViewById<ListView>(R.id.list_item)
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, contactsInfo.phoneNumberList)
        listView.adapter = adapter

        sDialog?.setCustomView(layout)

        listView.setOnItemClickListener{ _, _, i: Int, _ ->
            contactsInfo.phoneNumber = contactsInfo.phoneNumberList[i]

            if (isMutiple){
                if(isChecked)
                    listMultiple?.add(dataListFiltered!![i])
                else
                    listMultiple?.remove(dataListFiltered!![i])
            }else{
                ContactList.onClickContactListener.onClickContact(view, contactsInfo)
                activity.finish()
            }

            sDialog?.dismissWithAnimation()
        }

        activity.runOnUiThread {
            sDialog?.show()
            sDialog?.getButton(SweetAlertDialog.BUTTON_CONFIRM)?.visibility = View.GONE}
    }

    fun doneSelect(v: View) {
        if (listMultiple?.get(0) != null)
            ContactList.onClickContactListener.onClickContact(v, listMultiple?.get(0) )

        ContactList.onClickContactListener.onSelectClickContact(listMultiple)
        activity.finish()
    }


    /*int colors[] = {R.color.red, R.color.pink, R.color.purple, R.color.deep_purple,
            R.color.indigo, R.color.blue, R.color.light_blue, R.color.cyan, R.color.teal, R.color.green,
            R.color.light_green, R.color.lime, R.color.yellow, R.color.amber, R.color.orange, R.color.deep_orange};*/
    init {
        this.dataList = dataList
        dataListFiltered = dataList
    }
}