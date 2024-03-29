package com.marco.proyectonew

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServApi {
    @GET("/v2/directions/driving-car")
    /*Esta funcion va a llamar a la web de Open Route con las Query que podemos ver en la misma Web*/
    /*Para que una corutina funcione, es obligatorio poner suspend primero*/
    suspend fun conseguirRuta(
        @Query("api_key") key:String,
        /*Se pone encoded = true para que acepte las comas en la url, como string, sino pone caracteres raros
        Podríamos ver el error en la url destino, debuggeando*/
        @Query("start", encoded = true) start:String,
        /*encode permite generar string para la pagina web*/
        @Query("end", encoded= true) end:String
    ): Response<RespdeRuta>
}