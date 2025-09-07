package course

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.youngtaek.capstone_app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.career.Career
import network.RetrofitClient

class CareerListActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CAREER"
    }

    private lateinit var spinner: Spinner
    private lateinit var submitBtn: Button
    private var selectedCareerCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_job)

        val departmentCode = intent.getStringExtra("departmentCode") ?: run {
            Log.e(TAG, "departmentCode is null")
            Toast.makeText(this, "학과 코드가 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        spinner = findViewById(R.id.cl_spinner)
        submitBtn = findViewById(R.id.cl_submit)

        // 직무 리스트 호출
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "요청 시작: /careerlist/$departmentCode")
                val response = RetrofitClient.api.getCareer(departmentCode)

                if (!response.isSuccessful) {
                    Log.e(TAG, "HTTP 실패: ${response.code()}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CareerListActivity, "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                        submitBtn.isEnabled = false
                    }
                    return@launch
                }

                val body = response.body()
                val resultVal = body?.resultVal
                val careers = body?.careers ?: emptyList()

                Log.d(TAG, "resultVal=$resultVal, size=${careers.size}")

                withContext(Dispatchers.Main) {
                    if (resultVal == 200 && careers.isNotEmpty()) {
                        val adapter = ArrayAdapter(
                            this@CareerListActivity,
                            android.R.layout.simple_spinner_item,
                            careers.map { it.name }
                        ).also {
                            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                        spinner.adapter = adapter

                        // 기본 선택값
                        selectedCareerCode = careers[0].code

                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                selectedCareerCode = careers[position].code
                            }
                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }

                        submitBtn.isEnabled = true
                    } else {
                        Toast.makeText(this@CareerListActivity, body?.resultMsg ?: "직무 목록이 없습니다.", Toast.LENGTH_SHORT).show()
                        submitBtn.isEnabled = false
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "예외: ${e.localizedMessage}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CareerListActivity, "네트워크 오류: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    submitBtn.isEnabled = false
                }
            }
        }

        // 제출 → 다음 화면으로 전달
        submitBtn.setOnClickListener {
            if (selectedCareerCode.isBlank()) {
                Toast.makeText(this, "직무를 선택하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val name = spinner.selectedItem?.toString().orEmpty()

            val intent = Intent(this, CareerPathListActivity::class.java).apply {
                putExtra(CareerPathListActivity.EXTRA_CAREER_CODE, selectedCareerCode)
                putExtra("careerName", name)
            }
            Log.d(TAG, "직무 선택 → code=$selectedCareerCode, name=$name")
            startActivity(intent)
        }
    }
}