package com.example.campusconnectproject

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ExploreFragment : Fragment() {

    private var _map: MapView? = null
    private val map get() = _map!!

    private lateinit var searchProfile: SearchView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        _map = view.findViewById(R.id.map)


        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val vsuPoint = GeoPoint(37.2343, -77.4191)
        val mapController = map.controller
        mapController.setZoom(17.0)
        mapController.setCenter(vsuPoint)

        val vsuBounds = BoundingBox(37.2400, -77.4100, 37.2280, -77.4300)
        map.setScrollableAreaLimitDouble(vsuBounds)
        map.minZoomLevel = 15.0
        map.maxZoomLevel = 19.0


    }


    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _map = null
    }
}
