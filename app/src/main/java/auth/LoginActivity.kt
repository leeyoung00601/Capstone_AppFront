package auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.youngtaek.capstone_app.R
import course.DepartmentListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.RetrofitClient

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.la_login_area)
        passwordField = findViewById(R.id.la_passs_word_area)
        loginBtn = findViewById(R.id.la_login_btn)
        signupBtn = findViewById(R.id.la_sign_up_btn)

        loginBtn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            login(email, password)
        }

        signupBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun login(email: String, password: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("LOGIN_REQ", "요청 시작: $email / $password") // ✅ 요청 직전 로그

                val response = RetrofitClient.api.login(email, password)

                Log.d("LOGIN_RES", "응답 코드: ${response.code()}") // ✅ 응답 코드 확인
                Log.d("LOGIN_RES", "응답 바디: ${response.body()}") // ✅ 응답 바디 확인

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        Toast.makeText(this@LoginActivity, "${body?.msg}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, DepartmentListActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception){
                Log.e("LOGIN_ERR", "예외 발생", e) // ✅ 예외 출력
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoginActivity, "예외 발생: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


