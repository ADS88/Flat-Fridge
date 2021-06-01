package com.example.flatfridge

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    lateinit var viewModel: HomeViewModel
    lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_home, container, false)
        val foodRecylerView = layout.findViewById<RecyclerView>(R.id.food_recycler_view)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(foodRecylerView)

        adapter = FoodAdapter(requireContext())
        foodRecylerView.adapter = adapter

        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val recyclerViewPreference = sp.getString("recyclerViewDisplay", "list")

        if(recyclerViewPreference == "list"){
            foodRecylerView.layoutManager = LinearLayoutManager(this.context)
        } else {
            foodRecylerView.layoutManager = GridLayoutManager(this.context, 2, LinearLayoutManager.VERTICAL, false)
        }

        val noItemImageView = layout.findViewById<ImageView>(R.id.noItemImageView)
        noItemImageView.setImageResource(R.drawable.donut)

        noItemImageView.setOnClickListener {
            Navigation.findNavController(layout).navigate(R.id.action_homeFragment_to_addFoodFragment)
        }

        val coolAnimation = noItemImageView.drawable as AnimatedVectorDrawable
        coolAnimation.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable) {
                coolAnimation.start()
            }
        })
        coolAnimation.start()

        val noItemView = layout.findViewById<LinearLayout>(R.id.noItemView)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.readAllData.observe(viewLifecycleOwner, Observer {foodItems ->
            adapter.setData(foodItems)
            if (viewModel.readAllData.value.isNullOrEmpty()) {
                foodRecylerView.visibility = View.GONE;
                noItemView.visibility = View.VISIBLE;
            }
            else {
                foodRecylerView.visibility = View.VISIBLE
                noItemView.visibility = View.GONE
            }
        })

        foodRecylerView.setHasFixedSize(true)

        return layout
    }


    val itemTouchHelperCallback =
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Delete Item
                viewModel.deleteFoodItem(adapter.getItemAtIndex(viewHolder.adapterPosition))
            }
        }

}