package com.example.flatfridge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flatfridge.data.FoodItem
import kotlinx.android.synthetic.main.food_item.view.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit


class FoodAdapter(context: Context): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>()  {

    private var foodList = emptyList<FoodItem>()
    val sp = PreferenceManager.getDefaultSharedPreferences(context)
    val expiryDatePreferece = sp.getString("date", "daysTillExpiry")

    //Called by recycler view when it's time to create new view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.food_item,
        parent, false)
        return FoodViewHolder(itemView)
    }

    //Fills a View with data from the food item at the current index
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        holder.imageView.setImageBitmap(currentItem.foodPhoto)
        holder.foodNameTextView.text = currentItem.foodName

        val currentDate = LocalDate.now()

        val expiryDate: LocalDate = Instant.ofEpochMilli(currentItem.expiryDate).atZone(ZoneId.systemDefault()).toLocalDate()

        val daysTillExpiry: Int = ChronoUnit.DAYS.between(currentDate, expiryDate).toInt()

        var expiryMessage = ""
        if(expiryDatePreferece == "daysTillExpiry") {
            expiryMessage = when {
                daysTillExpiry > 0 -> holder.foodExpiryTextView.context.resources.getString(
                    R.string.days_till_expiry,
                    daysTillExpiry
                )
                daysTillExpiry == 0 -> holder.foodExpiryTextView.context.resources.getString(R.string.expires_today)
                else -> holder.foodExpiryTextView.context.resources.getString(
                    R.string.days_past_expiry_date,
                    daysTillExpiry * -1
                )
            }
        } else {
            expiryMessage = expiryDate.toString()
        }

        holder.foodExpiryTextView.text = expiryMessage
    }

    override fun getItemCount() = foodList.size

    fun getItemAtIndex(index: Int): FoodItem{
        return foodList[index]
    }

    //Represents single row in list. Reuses views for better performance.
    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.food_image
        val foodNameTextView: TextView = itemView.food_name_text_view
        val foodExpiryTextView: TextView = itemView.food_expiry_text_view
    }

    fun setData(foodItems: List<FoodItem>){
        this.foodList = foodItems
        notifyDataSetChanged()
    }
}