package com.djamil.dynamic_form.models

import android.os.Parcelable
import android.view.View
import android.widget.CheckBox
import androidx.annotation.LayoutRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import com.squareup.moshi.Json
import com.djamil.dynamic_form.annotations.InputTypeDF
import com.djamil.dynamic_form.annotations.TypeField
import kotlinx.android.parcel.RawValue
import java.util.*

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
@Parcelize
@Entity(tableName = "io_fields")
data class IOFieldsItem(
    @PrimaryKey(autoGenerate = true)
    @field:Json(name = "id")
    public var id: Int = 0,

    @field:Json(name = "isRequired")
    public var isRequired: Boolean = false,

    @Ignore
    @field:Json(name = "children")
    public var children: @RawValue Any? = null,

    @field:Json(name = "field")
    public var field: String? = null,

    @field:Json(name = "method")
    public var method: String? = null,

    @field:Json(name = "label")
    public var label: String? = null,

    @field:Json(name = "type")
    public var type: String? = null,

    @field:Json(name = "url")
    public var url: String? = null,

    @field:Json(name = "order")
    public var order: Int = 0,

    @field:Json(name = "isReadOnly")
    public var isReadOnly: Boolean = false,

    @field:Json(name = "shouldBeShown")
    public var shouldBeShown: Boolean = true,

    @field:Json(name = "value")
    public var value: String? = null,

    @field:Json(name = "color")
    public var color: Int = 0,

    @field:Json(name = "devise")
    public var devise: String? = "XOF",

    @field:Json(name = "pays_alpha2")
    public var paysAlpha2: String? = null,

    @field:Json(name = "msg_hint")
    public var msgHint: String? = "",
    public var idOperationBiller: Int = -1,

    @Ignore public var listItemDF: @RawValue ArrayList<ItemDF>? = null,
    @Ignore public var listItemDFSelected: @RawValue  ArrayList<ItemDF>? = null,
    @Ignore public var itemDFSelected: @RawValue  ItemDF? = null,

    public var idView: Int = 0,
    public var indicatif: Int = 0,
    public var numPage: Int = 0,
    public var idBillerFields: Int = 0,
    public var isFormatter: Boolean = false,
    public var isMoney: Boolean = false,

    @LayoutRes
    public var template: Int = 0,

    @TypeField
    @field:Json(name = "type_field")
    public var type_field: String? = null,

    var biller_field_parent_id: Int? = null

): Comparable<IOFieldsItem> , Parcelable{

    public constructor(label: String?, field: String?, @InputTypeDF type: String?) : this() {
        this.label = label
        this.field = field
        this.type = type
    }

    public  constructor(
        label: String?,
        field: String?,
        @InputTypeDF type: String?,
        isMoney: Boolean
    ) : this() {
        this.label = label
        this.field = field
        this.type = type
        this.isMoney = isMoney
    }

    public constructor(
        label: String?,
        field: String?,
        @InputTypeDF type: String?,
        @LayoutRes template: Int
    ) : this() {
        this.label = label
        this.field = field
        this.type = type
        value = type
        this.template = template
    }

    public constructor(
        label: String?,
        field: String?,
        @InputTypeDF type: String?,
        value: String?
    ) : this() {
        this.label = label
        this.field = field
        this.type = type
        this.value = value
    }

    public constructor(
        isRequired: Boolean,
        field: String?,
        label: String?,
        @InputTypeDF type: String?,
        order: Int,
        isReadOnly: Boolean,
        shouldBeShown: Boolean,
        value: String?,
        color: Int,
        devise: String?,
        paysAlpha2: String?,
        msgHint: String?,
        indicatif: Int,
        isMoney: Boolean,
        template: Int
    ) : this() {
        this.isRequired = isRequired
        this.field = field
        this.label = label
        this.type = type
        this.order = order
        this.isReadOnly = isReadOnly
        this.shouldBeShown = shouldBeShown
        this.value = value
        this.color = color
        this.devise = devise
        this.paysAlpha2 = paysAlpha2
        this.msgHint = msgHint
        this.indicatif = indicatif
        this.isMoney = isMoney
        this.template = template
    }

    public fun getItemDFByIdView(idView: Int) {
        for (item in listItemDF!!) if (item.idView == idView) itemDFSelected = item
    }

    public fun getListItemDFByView(view: View) {
        listItemDFSelected = ArrayList()
        for (item in listItemDF!!) {
            if ((view.findViewById<View>(item.idView) as CheckBox).isChecked)
                listItemDFSelected!!.add(item)
        }
    }

    public override fun compareTo(other: IOFieldsItem): Int {
        return order - other.order
    }

    override fun toString(): String {
        return "IOFieldsItem(id=$id, isRequired=$isRequired, children=$children, field=$field, method=$method, label=$label, type=$type, url=$url, order=$order, isReadOnly=$isReadOnly, shouldBeShown=$shouldBeShown, value=$value, color=$color, devise=$devise, paysAlpha2=$paysAlpha2, msgHint=$msgHint, idOperationBiller=$idOperationBiller, listItemDF=$listItemDF, listItemDFSelected=$listItemDFSelected, itemDFSelected=$itemDFSelected, idView=$idView, indicatif=$indicatif, numPage=$numPage, idBillerFields=$idBillerFields, isFormatter=$isFormatter, isMoney=$isMoney, template=$template, type_field=$type_field, biller_field_parent_id=$biller_field_parent_id)"
    }


}