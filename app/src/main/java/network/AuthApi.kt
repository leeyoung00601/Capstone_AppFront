package network

import model.LoginRequest
import model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("studentsignin/{id}/{password}")
    suspend fun login(
        @Path("id") id: String,
        @Path("password") password: String
    ): Response<LoginResponse>


}