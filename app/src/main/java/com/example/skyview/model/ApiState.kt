package com.example.skyview.model

sealed class ApiState{
    class Success(val data : MyResponse) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}
