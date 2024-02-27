package com.marco.proyectonew

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragmen4 : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var map: GoogleMap
    private var start:String = ""
    private var end:String = ""
    var poly: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_fragmen4, container, false)

        crearFragment()

        // Inflate the layout for this fragment
        return root
    }



    private fun crearFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googlemap: GoogleMap) {
        map=googlemap
        map.setOnMapClickListener(this)
    }
    /*Con esta función recogemos el objeto Retrofit. Este objeto sirve para hacer llamadas a servicios Web
    y recoger valores. Lo usaremos para recoger la ruta de la web de OpenRoute*/
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create()) //es la que inicia la web
            .build()//ES LA FINALIZACION DE LA TAREA
    }
    /*Ahora necesitamos crear una API Service, que será la interfaz que llame al método GET de ese endpoint
    La hemos creado como interfaz (Kotlin Class), ServicioApi*/
    fun crearRuta(){
        Log.i("MARCO CREAR RUTA START: ",start)
        Log.i("MARCO CREAR RUTA END: ",end)
        /*Las llamadas a internet no se pueden hacer en el hilo principal, hay que hacerlo en corutinas QUE LLAMAN A LA PAGINA WEB
        para ello ejecutamos CoroutineScope(Dispatcher)  */
        CoroutineScope(Dispatchers.IO).launch {
            /*Lanzamos el hilo a través de la interfaz que hemos creado y la func conseguirRuta de la misma*/
            val llamada = getRetrofit().create(ServApi::class.java).conseguirRuta("5b3ce3597851110001cf6248114399674cb443a6b002b37dd39bfdd9",start,end)
            if(llamada.isSuccessful){
                /*Llama en la función un objeto*/
                /*Si la llamada ha recogido la ruta, ya la tengo en esta variable*/
                /*Necesitamos permiso de internet en el manifest, para poder acceder a la web de rutas*/
                Log.i("MARCO TENGO RUTA:", "OK")
                dibujarRuta(llamada.body())
                /* body es el cuerpo de la pagina web  */
            }else{
                Log.i("MARCO TENGO RUTA:","NO")
            }
        }
    }

    private fun dibujarRuta(respuestaDeRuta: RespdeRuta?) {
        /*Rellenamos el poliline con los datos que nos ha devuelto el servicio*/
        val polilineOptions = PolylineOptions()
        respuestaDeRuta?.features?.first()?.geometry?.coordinates?.forEach{
            polilineOptions.add(LatLng(it[1],it[0]))
        /* para cada linea de la lista agregamos a la variable polilinea, el valor capturado (lat y long  */
        /*Ponemos 1 primero, pq la api devuelve primero la latitud y no la longitud*/
        }
        activity?.runOnUiThread{
            /* Ejecutamos la acción de dibujado en la corutina  */
            val poly = map.addPolyline(polilineOptions)
        }
    }
/*   */
    override fun onMapClick(p0: LatLng) {
        if(::map.isInitialized){
            map.setOnMapClickListener {
                /*Con esto capturaremos el listener de cada click*/
                if(start.isEmpty()){
                    /*significa que estamos en el primer click en el mapa*/
                    /*necesitamos el siguiente formado de coordenada: long,lati en string*/
                    start = "${it.longitude},${it.latitude}"
                }else if(end.isEmpty()){
                    end = "${it.longitude},${it.latitude}"
                    crearRuta()
                }else{
                    /*Si entra aqui será la tercera vez que se clica en el mapa*/

                }
            }
        }
    }
}