package network


import model.career.CareerResponse
import model.DepartmentResponse
import model.LoginResponse
import model.SignUpRequest
import model.course.CourseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @POST("studentsignin/{id}/{password}")
    suspend fun login(
        @Path("id") id: String,
        @Path("password") password: String
    ): Response<LoginResponse>

    @POST("studentsignup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<Map<String, String>>

    @GET("departmentlist")
    suspend fun departmentlist():
            Response<List<DepartmentResponse>>

    @POST("careerlist/{departmentcd}")
    suspend fun getCareer(
        @Path("departmentcd") departmentCode: String
    ): Response<CareerResponse>

    @POST("careerpathlist/{careercd}")
    suspend fun getCourseList(
        @Path("careercd") careerCode: String
    ): Response<CourseResponse>




}