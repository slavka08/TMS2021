package by.slavintodron.babyhelper.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.slavintodron.babyhelper.databinding.MealItemBinding
import by.slavintodron.babyhelper.entity.MealEntity
import by.slavintodron.babyhelper.utils.convertLongToTime

class MealsAdapter: ListAdapter<MealEntity, MealsAdapter.EntriesViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntriesViewHolder {
        return EntriesViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EntriesViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class EntriesViewHolder(private val binder: MealItemBinding) : RecyclerView.ViewHolder(binder.root) {
        fun onBind(data: MealEntity) {
            binder.tvMealType.text = data.meal.type.toString()
            binder.tvMealVolume.text = data.meal.volume.toString()
            binder.tvMealDate.text = convertLongToTime(data.dateTime)
            binder.textViewInfo.text = data.meal.info
            binder.tvMealVolumeUnit.text = data.meal.measUnit.name.toLowerCase()
        }
    }

    companion object {
        val DIFF = object: DiffUtil.ItemCallback<MealEntity>() {
            override fun areItemsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
                return newItem == oldItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
                return newItem === oldItem
            }

        }
    }
}