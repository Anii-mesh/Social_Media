package com.animesh.social_media.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Adapter.PostAdapter
import com.animesh.social_media.Adapter.StoryAdapter
import com.animesh.social_media.Model.StoryModel
import com.animesh.social_media.Model.Post
import com.animesh.social_media.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    lateinit var storyRv: RecyclerView
    lateinit var storyList: ArrayList<StoryModel>

    lateinit var dashboardRv: RecyclerView
    lateinit var postList: ArrayList<Post>

    lateinit var database : FirebaseDatabase
    lateinit var auth : FirebaseAuth

    private lateinit var postAdapter : PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        storyRv = view.findViewById(R.id.storyRV)
        storyList = ArrayList()
        storyList.add(StoryModel(R.drawable.p1, R.drawable.profile, R.drawable.ic_live, "Animesh"))
        storyList.add(StoryModel(R.drawable.p2, R.drawable.profile, R.drawable.ic_live, "Animesh"))
        storyList.add(StoryModel(R.drawable.p3, R.drawable.profile, R.drawable.ic_live, "Animesh"))
        storyList.add(StoryModel(R.drawable.p1, R.drawable.profile, R.drawable.ic_live, "Animesh"))
        storyList.add(StoryModel(R.drawable.p2, R.drawable.profile, R.drawable.ic_live, "Animesh"))

        storyRv.adapter = StoryAdapter(storyList, storyRv.context)
        val linearLayoutManager = LinearLayoutManager(
            storyRv.context,
            LinearLayoutManager.HORIZONTAL, false
        )
        storyRv.layoutManager = linearLayoutManager
        storyRv.isNestedScrollingEnabled = false

        dashboardRv = view.findViewById(R.id.dashboardRv)
        postList = ArrayList()
        postAdapter = PostAdapter(postList, requireContext())  // ✅ Initialize here
        dashboardRv.layoutManager = LinearLayoutManager(requireContext())
        dashboardRv.adapter = postAdapter

        database.getReference().child("posts").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snapshot1 in snapshot.children){
                    val post: Post = snapshot1.getValue(Post::class.java) as Post
                    postList.add(post)
                }
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        dashboardRv.isNestedScrollingEnabled = false

        return (view)
    }



}