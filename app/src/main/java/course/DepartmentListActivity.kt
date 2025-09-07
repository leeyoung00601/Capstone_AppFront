package course

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.youngtaek.capstone_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.RetrofitClient
import kotlin.collections.map

class DepartmentListActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var submitBtn: Button
    private lateinit var selectedCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_list)

        spinner = findViewById<Spinner>(R.id.dl_spinner)
        submitBtn = findViewById<Button>(R.id.dl_submit)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("DEPARTMENT", "서버로 학과 리스트 요청 시작")
            try {
                val response = RetrofitClient.api.departmentlist()
                val departments = response.body()

                Log.d("DEPARTMENT", "응답 성공 여부: ${response.isSuccessful}") // 응답 성공 여부 확인
                Log.d("DEPARTMENT", "응답 바디: ${departments}") // 실제 받아온 데이터 확인

                if (response.isSuccessful && departments != null){
                    withContext(Dispatchers.Main){
                        Log.d("DEPARTMENT", "Spinner에 데이터 설정 중")

                        val adapter = ArrayAdapter(
                            this@DepartmentListActivity,
                            android.R.layout.simple_spinner_item,
                            departments.map { it.name }// 학과명만 표시, code는 표시되지 않음
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter

                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                selectedCode = departments[position].code // 사용자가 선택한 학과의 code 저장
                                Log.d("DEPARTMENT", "선택된 학과: ${departments[position].name}, 코드: $selectedCode") // 사용자 선택 확인
                            }
                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    }
                } else{
                    Log.d("DEPARTMENT", "서버 응답 실패. 코드: ${response.code()}") // 실패 처리
                }
            } catch (e: Exception){
                Log.d("DEPARTMENT", "에러 발생: ${e.localizedMessage}") // 네트워크 예외 처리
            }
        }

        submitBtn.setOnClickListener {
            val selectedDepartCode = selectedCode
            val selectedDepartmentName = spinner.selectedItem.toString()

            val intent = Intent(this, CareerListActivity::class.java)
            intent.putExtra("departmentCode", selectedDepartCode)
            intent.putExtra("departmentName", selectedDepartmentName)
            startActivity(intent)

            Log.d("DEPARTMENT", "학과데이터 조회 : $selectedDepartmentName($selectedDepartCode)")
        }

//        val departments = listOf("학과 선택", "IT경영", "경영", "데이터사이언스 경영")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, departments)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter
    }
}