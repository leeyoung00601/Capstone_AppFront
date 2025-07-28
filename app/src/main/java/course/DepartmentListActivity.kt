package course

import android.os.Bundle
import android.util.Log
import android.util.Log.e
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
            try {
                val response = RetrofitClient.authApi.departmentlist()
                val departments = response.body()

                if (response.isSuccessful && departments != null){
                    withContext(Dispatchers.Main){
                        val adapter = ArrayAdapter(
                            this@DepartmentListActivity,
                            android.R.layout.simple_spinner_item,
                            departments.map { it.name }// 이름만 표시
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter

                        spinner.onItemClickListener = object : AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                selectedCode = departments[position].code // 사용자가 선택한 학과의 code 저장
                            }
                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    }
                }
            } catch (e: Exception){
                Log.e("DEPARTMENT", "에러: ${e.localizedMessage}")
            }
        }

//        val departments = listOf("학과 선택", "IT경영", "경영", "데이터사이언스 경영")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, departments)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter

    }
}