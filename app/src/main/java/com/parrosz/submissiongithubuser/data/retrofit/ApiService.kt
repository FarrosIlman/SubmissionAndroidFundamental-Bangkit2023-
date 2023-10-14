package com.parrosz.submissiongithubuser.data.retrofit
import com.parrosz.submissiongithubuser.data.response.ItemsItem
import com.parrosz.submissiongithubuser.data.response.ResponseUsers
import com.parrosz.submissiongithubuser.data.response.SearchGithub
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("users")
    fun User(): Call<List<ItemsItem>>

    @GET("search/users")
    fun searchUser(
        @Query("q") query: String
    ): Call<SearchGithub>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<ResponseUsers>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

}