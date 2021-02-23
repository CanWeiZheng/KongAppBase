package kong.project.base.http.custom
import android.text.TextUtils
import com.google.gson.*
import java.lang.reflect.Type

class IntegerGsonTypeAdapter : JsonSerializer<Int?>, JsonDeserializer<Int> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Int {
        try {
            if (TextUtils.isEmpty(json.asString)) {
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
        return try {
            json.asInt
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(src: Int?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src)
    }
}