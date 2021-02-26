package com.kongappbase.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * @author: Kong
 * @date: 2020/10/9
 */
data class CityInfo(
    @SerializedName("city_name")
    var cityName: String,
    var groupId: String = "",
    var _groupTitle: String,
    @SerializedName("city_name_py")
    var cityNameSpell:String
) : Comparable<CityInfo>, Serializable {
    @SerializedName("city_first_code")
    var groupTitle: String = ""
        get() {
            return field.toUpperCase(Locale.getDefault())
        }

    override fun compareTo(other: CityInfo): Int {
        return cityNameSpell.compareTo(other.cityNameSpell)
    }

}