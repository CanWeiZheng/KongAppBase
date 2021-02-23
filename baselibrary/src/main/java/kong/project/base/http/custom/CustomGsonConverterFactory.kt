package kong.project.base.http.custom

import android.content.Intent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.Buffer
import okio.internal.commonAsUtf8ToByteArray
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type


/**
 * @author: Kong
 * @date: 2020/9/11
 */
class CustomGsonConverterFactory : Converter.Factory() {
    val gson: Gson = GsonBuilder().registerTypeAdapter(Intent::class.java, IntegerGsonTypeAdapter()).registerTypeAdapter(Int::class.javaPrimitiveType, IntegerGsonTypeAdapter())
        .registerTypeAdapter(
            Long::class.java, IntegerGsonTypeAdapter()
        )
        .registerTypeAdapter(Long::class.javaPrimitiveType, IntegerGsonTypeAdapter()).create()

    companion object {
        fun create(): CustomGsonConverterFactory {
            return CustomGsonConverterFactory()
        }

    }


    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonRequestConverter(adapter)
    }


    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonResponseBodyConverter(adapter)
    }


    inner class CustomGsonRequestConverter<T>(private val adapter: TypeAdapter<T>) :
        Converter<T, RequestBody> {
        override fun convert(value: T): RequestBody? {
            val buffer = Buffer()
            val writer = OutputStreamWriter(buffer.outputStream(), Charsets.UTF_8)
            val jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return buffer.readByteString()
                .toRequestBody("application/json; charset=UTF-8".toMediaType())
        }
    }


    inner class CustomGsonResponseBodyConverter<T>(private val adapter: TypeAdapter<T>) :
        Converter<ResponseBody, T> {

        override fun convert(value: ResponseBody): T? {
            val response = value.string()
            val contentType = value.contentType()
            val charset = contentType.let { it?.charset(Charsets.UTF_8) } ?: Charsets.UTF_8
            val inputStream = ByteArrayInputStream(response.commonAsUtf8ToByteArray())
            val reader = InputStreamReader(inputStream, charset)
            val jsonReader = gson.newJsonReader(reader)
            value.use {
                return adapter.read(jsonReader)
            }

        }

    }
}