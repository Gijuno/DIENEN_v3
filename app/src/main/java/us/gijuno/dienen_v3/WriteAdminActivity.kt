package us.gijuno.dienen_v3

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.register_register_btn
import kotlinx.android.synthetic.main.activity_write_admin.*
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import us.gijuno.dienen_v3.data.Warning
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WriteAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_admin)
        val fireStore = FirebaseFirestore.getInstance()

        write_admin_num.addTextChangedListener(textWatcher)
        write_admin_name.addTextChangedListener(textWatcher)
        write_admin_content.addTextChangedListener(textWatcher)

        write_admin_btn.setOnClickListener {
            val num = write_admin_num.text.toString()
            val name = write_admin_name.text.toString()
            val content = write_admin_content.text.toString()
            val currentDateTime = Calendar.getInstance().time
            val date_time = SimpleDateFormat("yyyy.MM.dd; HH:mm:ss", Locale.KOREA).format(currentDateTime)

            val warningUser = Warning()

            warningUser.num = num
            warningUser.name = name
            warningUser.date = date_time
            warningUser.content = content

            var addFirestore = fireStore.collection("warning").document("${warningUser.num} ${warningUser.name}").set(warningUser)
            addFirestore.addOnSuccessListener {
                Log.d("firebase","addOnSuccessListener")
            }
            addFirestore.addOnFailureListener {
                Log.d("firebase", "addOnFailureListener")
            }
        }

    }

    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (TextUtils.isEmpty(write_admin_num.getText()) || TextUtils.isEmpty(write_admin_name.getText())) {
                write_admin_btn.setEnabled(false)
                write_admin_btn.setTextColor(ContextCompat.getColor(this@WriteAdminActivity, R.color.colorPrimary))
            } else {
                write_admin_btn.setEnabled(true)
                write_admin_btn.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {
            // TODO Auto-generated method stub
        }

        override fun afterTextChanged(s: Editable) {
            // TODO Auto-generated method stub
        }
    }

    class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
            val delegate = retrofit!!.nextResponseBodyConverter<Any>(this, type!!, annotations!!)
            return Converter<ResponseBody, Any> {
                if (it.contentLength() == 0L) return@Converter EmptyResponse()
                delegate.convert(it)
            }
        }

    }

}