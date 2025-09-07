package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youngtaek.capstone_app.R
import model.course.Course

class CourseAdapter(
    private var items: List<Course>,
    private val onAddClick: (Course) -> Unit = {},
    private val onDetailClick: (Course) -> Unit = {}
) : RecyclerView.Adapter<CourseAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvSubjectName: TextView = v.findViewById(R.id.tvSubjectName)
        val tvProfessorName: TextView = v.findViewById(R.id.tvProfessorName)
        val tvCredit: TextView = v.findViewById(R.id.tvCredit)
        val tvClassTime: TextView = v.findViewById(R.id.tvClassTime)
        val btnAdd: Button = v.findViewById(R.id.btnAdd)
        val btnDetail: Button = v.findViewById(R.id.btnDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val item = items[position]

        // 🔗 여기서 바인딩
        h.tvSubjectName.text   = item.sname                  // 과목명
        h.tvProfessorName.text = "교수명: ${item.pname}"      // 교수명
        h.tvCredit.text        = "학점: ${item.credit}"       // 학점
        h.tvClassTime.text     = item.classtime              // 시간

        // 버튼 클릭 콜백
        h.btnAdd.setOnClickListener { onAddClick(item) }
        h.btnDetail.setOnClickListener { onDetailClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<Course>) {
        items = newItems
        notifyDataSetChanged()
    }
}