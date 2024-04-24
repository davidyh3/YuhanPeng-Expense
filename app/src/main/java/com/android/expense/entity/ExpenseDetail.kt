package com.android.expense.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_detail")
data class ExpenseDetail(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
) : Parcelable {

    @ColumnInfo(name = "category_id")
    var categoryId: Long = 0

    @ColumnInfo(name = "date")
    var date: String = ""

    @ColumnInfo(name = "amount")
    var amount: String = ""

    constructor(parcel: Parcel) : this(parcel.readLong()) {
        categoryId = parcel.readLong()
        date = parcel.readString() ?: ""
        amount = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(categoryId)
        parcel.writeString(date)
        parcel.writeString(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExpenseDetail> {
        override fun createFromParcel(parcel: Parcel): ExpenseDetail {
            return ExpenseDetail(parcel)
        }

        override fun newArray(size: Int): Array<ExpenseDetail?> {
            return arrayOfNulls(size)
        }
    }
}