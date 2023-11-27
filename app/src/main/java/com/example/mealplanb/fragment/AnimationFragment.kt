package com.example.mealplanb.fragment

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentAnimationBinding

class AnimationFragment : Fragment() {
    private lateinit var binding: FragmentAnimationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnimationBinding.inflate(inflater, container, false)
        // Find the ImageView with id 'anim' in the binding
        return binding.root
        //아아

    }

}