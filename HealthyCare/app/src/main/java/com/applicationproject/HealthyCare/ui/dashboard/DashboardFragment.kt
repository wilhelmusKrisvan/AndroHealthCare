package com.applicationproject.HealthyCare.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.applicationproject.HealthyCare.*
import com.applicationproject.HealthyCare.R
import com.applicationproject.HealthyCare.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    lateinit var db: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val uid: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseDatabase.getInstance().getReference("user/$uid")
        val profileUser: TextView = root.findViewById(R.id.showUsername)
        val PhotoView: CircleImageView = root.findViewById(R.id.profile_imageView)
        val btnOut: Button = root.findViewById(R.id.buttonLOGOUT)
        val btnEdit: Button = root.findViewById(R.id.btnEditProfile)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(data: DataSnapshot) {
                val user = data.getValue(User::class.java)
                profileUser.text = user!!.user
                var link = user!!.img
                Picasso.get().load(link).into(PhotoView)
            }
        }
        db.addValueEventListener(listener)

        btnEdit.setOnClickListener {
            val i:Intent = Intent(activity, ProfileActivity::class.java)
            startActivity(i)
        }
        btnOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i:Intent = Intent(activity, AfterSplash::class.java)
            startActivity(i)
            getActivity()?.finish()
        }
        return root
        
    }
}