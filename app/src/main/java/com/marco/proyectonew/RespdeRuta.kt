package com.marco.proyectonew

import com.google.gson.annotations.SerializedName

data class RespdeRuta(@SerializedName("features") val features:List<Feature>)
data class Feature(@SerializedName("geometry") val geometry:Geometry)
data class Geometry(@SerializedName("coordinates") val coordinates:List<List<Double>>)

