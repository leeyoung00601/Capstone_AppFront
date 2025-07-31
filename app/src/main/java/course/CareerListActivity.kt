package course

import android.os.Bundle
import android.util.Log
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

class CareerListActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var submitbtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_job)

        val code = intent.getStringExtra("departmentCode")
        val name = intent.getStringExtra("departmentName")

        spinner = findViewById<Spinner>(R.id.cl_spinner)
        submitbtn = findViewById<Button>(R.id.cl_submit)





    }
}