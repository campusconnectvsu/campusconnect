package com.example.campusconnectproject

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import androidx.fragment.app.setFragmentResult

class ExploreFragment : Fragment() {

    private var _map: MapView? = null
    private val map get() = _map!!
    private var searchView: SearchView? = null
    private val vsuPoint = GeoPoint(37.2343, -77.4191)

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Corrected VSU Campus Locations with High Precision Coordinates
    private val campusLocations = mapOf(
        "Johnston Memorial Library" to (GeoPoint(37.2358, -77.41885) to "Library"),
        "Jones Dining Hall" to (GeoPoint(37.23485, -77.41955) to "Dining"),
        "Foster Hall" to (GeoPoint(37.23525, -77.41985) to "Academic"),
        "Memorial Hall" to (GeoPoint(37.23535, -77.42085) to "Academic"),
        "Gandy Hall" to (GeoPoint(37.23615, -77.41985) to "Academic"),
        "Virginia State University" to (GeoPoint(37.2343, -77.4191) to "Campus"),
        "Rogers Stadium" to (GeoPoint(37.2305, -77.4182) to "Sports"),
        "Gateway Residence Hall" to (GeoPoint(37.2385, -77.4215) to "Residence"),
        "Hunter-McDaniel Hall" to (GeoPoint(37.23585, -77.41755) to "Academic"),
        "Trinkle Hall" to (GeoPoint(37.23455, -77.41785) to "Academic"),
        "Virginia Hall" to (GeoPoint(37.23425, -77.42055) to "Academic"),
        "Lockett Hall" to (GeoPoint(37.23625, -77.41855) to "Academic"),
        "Multi-Purpose Center (MPC)" to (GeoPoint(37.2405, -77.4210) to "Sports"),
        "Whiting Hall" to (GeoPoint(37.2348, -77.4170) to "Residence"),
        "Langston Hall" to (GeoPoint(37.2370, -77.4180) to "Residence"),
        "Seward Hall" to (GeoPoint(37.2350, -77.4210) to "Residence"),
        "Vawter Hall" to (GeoPoint(37.2340, -77.4215) to "Residence")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        _map = view.findViewById(R.id.map)
        searchView = view.findViewById(R.id.searchView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.post {
            if (_map == null) return@post

            map.setTileSource(TileSourceFactory.MAPNIK)
            map.setMultiTouchControls(true)
            map.isTilesScaledToDpi = true
            map.tilesScaleFactor = 1.3f

            map.controller.setZoom(17.5)
            map.controller.setCenter(vsuPoint)

            val vsuBounds = BoundingBox(37.2500, -77.4000, 37.2200, -77.4400)
            map.setScrollableAreaLimitDouble(vsuBounds)
            map.minZoomLevel = 14.0
            map.maxZoomLevel = 21.0

            setupSearch()
            setupChips(view)
            setupFab(view)
        }
    }

    private fun setupFab(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fabCenter).setOnClickListener {
            map.controller.animateTo(vsuPoint)
            map.controller.setZoom(17.5)
            map.overlays.clear()
            map.invalidate()
        }
    }

    private fun setupChips(view: View) {
        view.findViewById<Chip>(R.id.chipLibrary)
            .setOnClickListener { performCategorySearch("Library") }
        view.findViewById<Chip>(R.id.chipDining)
            .setOnClickListener { performCategorySearch("Dining") }
        view.findViewById<Chip>(R.id.chipAcademic)
            .setOnClickListener { performCategorySearch("Academic") }
        view.findViewById<Chip>(R.id.chipResidence)
            .setOnClickListener { performCategorySearch("Residence") }
        view.findViewById<Chip>(R.id.chipSports)
            .setOnClickListener { performCategorySearch("Sports") }
    }

    private fun performCategorySearch(category: String) {
        map.overlays.clear()

        campusLocations.filter { it.value.second == category }.forEach { (name, data) ->
            val marker = Marker(map)
            marker.position = data.first
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = name
            marker.snippet = "VSU Campus"
            map.overlays.add(marker)
        }

        if (map.overlays.isNotEmpty()) {
            val firstMatch = (map.overlays[0] as Marker).position
            map.controller.animateTo(firstMatch)
            map.controller.setZoom(18.0)
            Toast.makeText(requireContext(), "Showing $category locations", Toast.LENGTH_SHORT)
                .show()
        }

        map.invalidate()
    }

    private fun setupSearch() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    performSearch(query)
                }
                searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun performSearch(query: String) {
        val lowercaseQuery = query.lowercase().trim()

        // Find match: prioritize exact matches, then substrings
        val match = campusLocations.entries.find { it.key.lowercase() == lowercaseQuery }
            ?: campusLocations.entries.find { it.key.lowercase().contains(lowercaseQuery) }

        if (match != null) {
            val destination = match.value.first

            // Clear previous markers
            map.overlays.clear()

            // Add the marker
            val marker = Marker(map)
            marker.position = destination
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = match.key
            marker.snippet = "VSU Campus"
            map.overlays.add(marker)

            // Move camera and show the correct label
            map.controller.animateTo(destination)
            map.controller.setZoom(20.0)
            marker.showInfoWindow()

            Toast.makeText(requireContext(), "Found: ${match.key}", Toast.LENGTH_SHORT).show()
        } else {
            val currentUid = auth.currentUser?.uid ?: return
            db.collection("users")
                .orderBy("name")
                .startAt(lowercaseQuery)
                .endAt(lowercaseQuery + "\uf8ff")
                .get()
                .addOnSuccessListener { result ->
                    val users = result.documents.filter { it.getString("uid") != currentUid }
                    if (users.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            " No locations or people found",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val uid = users[0].getString("uid") ?: return@addOnSuccessListener
                        val intent = Intent(requireContext(), UserProfActivity::class.java)
                        intent.putExtra("USER_ID", uid)
                        startActivity(intent)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Search failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onResume() {
        super.onResume()
        _map?.onResume()
    }

    override fun onPause() {
        super.onPause()
        _map?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _map = null
    }
}
