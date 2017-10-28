package dgapmipt.druncatorg

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Shad on 27.10.2017.
 */

open class User(
        val token: String = "",
        val id: Int,
        val cardId: String,
        val coefficent: Float = 1.0F,
        val name: String,
        val surname: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readFloat(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(token)
        writeInt(id)
        writeString(cardId)
        writeFloat(coefficent)
        writeString(name)
        writeString(surname)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}