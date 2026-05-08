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
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class ExploreFragment : Fragment() {

    // nullable map reference
    private var _map: MapView? = null
    private val map get() = _map!!
    // search bar
    private var searchView: SearchView? = null
    // center the map to vsu campus
    private val vsuPoint = GeoPoint(37.2343, -77.4191)

    // firestore db instance
    private val db = FirebaseFirestore.getInstance()
    // auth instance
    private val auth = FirebaseAuth.getInstance()
    // container for search result list
    private var searchResultsC: View? = null
    // Recycler View of result card
    private var searchResultRe: RecyclerView? = null
    // adapter for search result list
    private var searchReAdap: SearchResultsAdapter? = null
    // show no results when noting is found
    private var no_Res: TextView? = null
    // label search results
    private var prof_Res: TextView? = null


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
        // config before mapview is used
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        // bind map and search bar views
        _map = view.findViewById(R.id.map)
        searchView = view.findViewById(R.id.searchView)

        // bind search result views
        searchResultsC = view.findViewById(R.id.search_Card)
        searchResultRe = view.findViewById(R.id.search_ResVList)
        no_Res = view.findViewById(R.id.No_search_found)
        prof_Res = view.findViewById(R.id.userProf_acc)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // set up the search result recyclerView
        searchReAdap = SearchResultsAdapter(emptyList())
        searchResultRe?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchReAdap
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        // defer map setup
        view.post {
            if (_map == null) return@post

            // use OpenStreet map tiles
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.setMultiTouchControls(true)
            // scale titles to match screen
            map.isTilesScaledToDpi = true
            map.tilesScaleFactor = 1.3f

            // center the map to canvas
            map.controller.setZoom(17.5)
            map.controller.setCenter(vsuPoint)

            // restrict map to vsu campus
            val vsuBounds = BoundingBox(37.2500, -77.4000, 37.2200, -77.4400)
            map.setScrollableAreaLimitDouble(vsuBounds)
            map.minZoomLevel = 14.0
            map.maxZoomLevel = 21.0

            setupSearch()
            setupChips(view)
            setupFab(view)
        }
    }

    // clear and rest map
    private fun setupFab(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fabCenter).setOnClickListener {
            hide_Search()
            map.controller.animateTo(vsuPoint)
            map.controller.setZoom(17.5)
            map.overlays.clear()
            map.invalidate()
        }
    }

    // map marker matches category
    private fun setupChips(view: View) {
        view.findViewById<Chip>(R.id.chipLibrary)
            .setOnClickListener { hide_Search(); performCategorySearch("Library") }
        view.findViewById<Chip>(R.id.chipDining)
            .setOnClickListener { hide_Search(); performCategorySearch("Dining") }
        view.findViewById<Chip>(R.id.chipAcademic)
            .setOnClickListener { hide_Search(); performCategorySearch("Academic") }
        view.findViewById<Chip>(R.id.chipResidence)
            .setOnClickListener { hide_Search(); performCategorySearch("Residence") }
        view.findViewById<Chip>(R.id.chipSports)
            .setOnClickListener { hide_Search(); performCategorySearch("Sports") }
    }

    // place marker for all location in category
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

        // pan to first result if marker were added
        if (map.overlays.isNotEmpty()) {
            val firstMatch = (map.overlays[0] as Marker).position
            map.controller.animateTo(firstMatch)
            map.controller.setZoom(18.0)
            Toast.makeText(requireContext(), "Showing $category locations", Toast.LENGTH_SHORT)
                .show()
        }

        map.invalidate()
    }

    // attach listeners to search Bar
    private fun setupSearch() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // search campus building
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    performSearch(query)
                }
                searchView?.clearFocus()
                return true
            }

            // show search results while still searching
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    hide_Search()
                } else {
                    search_Prof(newText.trim())
                }
                return true
            }
        })

        // hide results when search loses focus
        searchView?.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus && searchView?.query.isNullOrBlank()) {
                hide_Search()
            }
        }
    }

    // search campus location by name
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
            search_Prof(lowercaseQuery)
        }
    }

    // search firestore users by name
    private fun search_Prof(query: String) {
        val currentUid = auth.currentUser?.uid ?: return
        val lowercaseQuery = query.lowercase().trim()

        Log.d("EXPLORE_SEARCH", "searching Firestore for: '$lowercaseQuery'")
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                Log.d("EXPLORE_SEARCH", "Total docs in users collection: ${result.size()}")
                result.documents.forEach { doc ->
                    val uid = doc.getString("uid")
                    val name = doc.getString("name")
                    Log.d("EXPLORE_SEARCH", "Doc id=${doc.id} uid =$uid name=$name")
                    Log.d(
                        "EXPLORE_SEARCH", "name.lowercase()=${name?.lowercase()} " +
                                "contains '$lowercaseQuery' = ${
                                    name?.lowercase()?.contains(lowercaseQuery)
                                }"
                    )
                    Log.d(
                        "EXPLORE_SEARCH",
                        "uid! = currentUid: $uid != $currentUid = ${uid != currentUid}"
                    )
                }

                // filter profiles based on search query
                val searchUsers = result.documents
                    .filter { doc ->
                        val uid = doc.getString("uid") ?: return@filter false
                        val name = doc.getString("name") ?: return@filter false
                        uid != currentUid && name.lowercase().contains(lowercaseQuery)
                    }
                    .mapNotNull { doc ->
                        val uid = doc.getString("uid") ?: return@mapNotNull null
                        val name = doc.getString("name") ?: return@mapNotNull null
                        SearchUser(uid = uid, name = name)
                    }
                    .toMutableList()
                Log.d("EXPLORE_SEARCH", "Matched users: ${searchUsers.size}")
                show_s_Res(searchUsers)

                // search result shows follower count and status
                searchUsers.forEachIndexed { index, searchUser ->
                    db.collection("users").document(searchUser.uid)
                        .collection("followers")
                        .get()
                        .addOnSuccessListener { followers ->
                            searchUsers[index]= searchUser.copy(followCount = followers.size())
                            // check if current user already follows the profile
                            db.collection("users").document(currentUid)
                                .collection("following").document(searchUser.uid)
                                .get()
                                .addOnSuccessListener { followDoc ->
                                    searchUsers[index] = searchUsers[index].copy(
                                        following = followDoc.exists()
                                    )
                                    searchReAdap?.update_prof(searchUsers.toList())
                                }
                        }
                }

            }
            .addOnFailureListener { e->
                Log.e("EXPLORE_SEARCH", "Firestore query failed: ${e.message}, e")
                Toast.makeText(requireContext(), "Search failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    // shows result cards and toggles
    private fun show_s_Res(users: List<SearchUser>){
        searchReAdap?.update_prof(users)
        searchResultsC?.visibility = View.VISIBLE

        if ( users.isEmpty()){
            prof_Res?.visibility = View.GONE
            no_Res?.visibility = View.VISIBLE
            searchResultRe?.visibility = View.GONE
        } else {
            prof_Res?.visibility = View.VISIBLE
            no_Res?.visibility = View.GONE
            searchResultRe?.visibility = View.VISIBLE
        }


}
    // hide search result card and clear adapter
    private fun hide_Search(){
        searchResultsC?.visibility = View.GONE
        searchReAdap?.update_prof(emptyList())
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
