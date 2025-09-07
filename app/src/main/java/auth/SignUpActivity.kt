package auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.youngtaek.capstone_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.SignUpRequest
import network.RetrofitClient

class SignUpActivity : AppCompatActivity() {

    private lateinit var suaIdField: EditText
    private lateinit var suaPasswordField: EditText
    private lateinit var suaPasswordCheakField: EditText
//    private lateinit var suaIdCheakBtn: Button
    private lateinit var suaSignUpBtn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        suaIdField = findViewById(R.id.sa_id_area)
        suaPasswordField = findViewById(R.id.sa_pass_word_area)
        suaPasswordCheakField = findViewById(R.id.sa_pass_word_area_cheak)
        suaSignUpBtn = findViewById(R.id.sa_sign_up_btn)
//        suaIdCheakBtn = findViewById(R.id.sa_check_id_button)


//        suaIdCheakBtn.setOnClickListener {
//            Toast.makeText(this, "이후 구현 에정", Toast.LENGTH_SHORT).show()
//
//            val id = suaIdField.text.toString()
//
//            if(id.length != 10){
//                Toast.makeText(this, "학번은 10자리여야 합니다.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//        }

        suaSignUpBtn.setOnClickListener {

            val id = suaIdField.text.toString()
            val password = suaPasswordField.text.toString()
            val passwordCheak = suaPasswordCheakField.text.toString()

            if(password != passwordCheak){
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val signUpRequest = SignUpRequest(
                id = id,
                password = password
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.d("SIGN_UP", "회원가입 요청 시작")
                    val response = RetrofitClient.api.signUp(signUpRequest)
                    val body = response.body()

                    withContext(Dispatchers.Main) {
                        Log.d("SIGN_UP", "응답 코드: ${response.code()}")
                        Log.d("SIGN_UP", "응답 바디: ${body}")

                        if (response.isSuccessful && body != null) {
                            Toast.makeText(this@SignUpActivity, body["msg"] ?: "성공", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@SignUpActivity, "회원가입 실패: ${body?.get("msg")}", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("SIGN_UP", "에러 발생", e)
                        Toast.makeText(this@SignUpActivity, "에러 발생: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}