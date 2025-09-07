// course/CareerPathListActivity.kt
package course

import adapter.CourseAdapter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youngtaek.capstone_app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.course.Course
import model.course.CourseResponse
import network.RetrofitClient

class CareerPathListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CAREER_CODE = "careerCode"   // 직전 화면에서 넣어준 키와 동일
        private const val TAG = "CPATH"
    }

    private lateinit var recycler: RecyclerView

    // 버튼 콜백 포함 어댑터(필요 없으면 람다 비워둬도 됨)
    private val courseAdapter = CourseAdapter(
        items = emptyList(),
        onAddClick = { /* TODO: 클릭 로직 비워둠 */ },
        onDetailClick = { /* TODO: 클릭 로직 비워둠 */ }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caree_path_list)

        // 1) 인텐트에서 값 받기
        val careerCode = intent.getStringExtra(EXTRA_CAREER_CODE)
        if (careerCode.isNullOrBlank()) {
            Toast.makeText(this, "직무 코드가 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 선택한 직무명 상단 표시 (레이아웃에 tvSelectedCareer(TextView) 가 있어야 함)
        findViewById<TextView?>(R.id.tvSelectedCareer)?.text =
            intent.getStringExtra("careerName")?.let { "직무: $it" } ?: "추천 과목"

        // 2) 리사이클러뷰 세팅
        recycler = findViewById(R.id.rvCourses)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = courseAdapter

        // 3) 데이터 요청
        fetchCourses(careerCode)
    }

    private fun fetchCourses(careerCode: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "요청 시작: /careerpathlist/$careerCode")
                val res = RetrofitClient.api.getCourseList(careerCode)

                if (!res.isSuccessful) {
                    Log.e(TAG, "HTTP 실패: code=${res.code()}, msg=${res.message()}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@CareerPathListActivity,
                            "서버 오류: ${res.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        courseAdapter.update(emptyList())
                    }
                    return@launch
                }

                val body: CourseResponse? = res.body()
                Log.d(TAG, "resultVal=${body?.resultVal}, msg=${body?.resultMsg}")

                withContext(Dispatchers.Main) {
                    val list = body?.careerPathDtos ?: emptyList()
                    if (body?.resultVal == 200 && list.isNotEmpty()) {
                        Log.d(TAG, "목록 크기: ${list.size}")
                        courseAdapter.update(list)
                    } else {
                        Log.w(TAG, "추천 과목 없음 또는 실패: ${body?.resultMsg}")
                        Toast.makeText(
                            this@CareerPathListActivity,
                            body?.resultMsg ?: "추천 과목이 없습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        courseAdapter.update(emptyList())
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "예외: ${e.localizedMessage}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CareerPathListActivity,
                        "네트워크 오류: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    courseAdapter.update(emptyList())
                }
            }
        }
    }
}