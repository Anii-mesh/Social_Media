package com.animesh.social_media.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.animesh.social_media.Adapter.UserAdapter
import com.animesh.social_media.Model.User
import com.animesh.social_media.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private val fullList = ArrayList<User>()
    private lateinit var userAdapter: UserAdapter

    // ADDED: A Set to hold UIDs of users the current user is following
    private val followingSet = HashSet<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()

        setupRecyclerView()
        fetchUsers()
        setupSearchWatcher()

        return binding.root
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(mutableListOf(), followingSet)
        binding.userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.userRecyclerView.adapter = userAdapter
    }

    private fun fetchUsers() {
        dbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fullList.clear()
                val me = auth.currentUser?.uid

                for (dataSnapshot in snapshot.children) {
                    val map = dataSnapshot.value as? Map<*, *>
                    if (map != null) {
                        // Safely extract fields
                        val name = map["name"] as? String ?: ""
                        val profession = map["profession"] as? String ?: ""

                        // Skip adding the current user (optional)
                        if (dataSnapshot.key != me) {
                            val user = User(
                                uid = dataSnapshot.key.toString(),
                                name = name,
                                profession = profession
                            )
                            fullList.add(user)
                        }
                    }
                }

                // Update the adapter with the full list
                userAdapter.updateList(fullList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SearchFragment", "Failed to fetch users: ${error.message}")
            }
        })
    }


    private fun setupSearchWatcher() {
        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""

                if (query.isBlank()) {
                    userAdapter.updateList(fullList)
                    return
                }

                val filtered = fullList.filter { user ->
                    user.name.contains(query, ignoreCase = true) ||
                            user.profession.contains(query, ignoreCase = true)
                }
                userAdapter.updateList(filtered)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}