package by.slavintodron.babyhelper.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.slavintodron.babyhelper.R
import by.slavintodron.babyhelper.databinding.MealItemBinding
import by.slavintodron.babyhelper.entity.MealEntity
import by.slavintodron.babyhelper.entity.MealType
import by.slavintodron.babyhelper.utils.convertLongToTime
import by.slavintodron.babyhelper.utils.displayTime

class MealsAdapter(
    private val editCallback: (Id: Int) -> Unit,
    private val deleteCallback: (Id: Int) -> Unit
) : ListAdapter<MealEntity, MealsAdapter.EntriesViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntriesViewHolder {
        return EntriesViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EntriesViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class EntriesViewHolder(private val binder: MealItemBinding) : RecyclerView.ViewHolder(binder.root) {
        fun onBind(data: MealEntity) {
            when (data.meal.type) {
                MealType.BREAST_FEEDING -> {
                    binder.bFeedValues.visibility = View.VISIBLE
                    binder.volumeContainer.visibility = View.GONE
                    binder.tvMealLeftB.text = String.format(
                        "%s %s",
                        binder.root.resources.getString(R.string.left),
                        data.meal.timerLeft.displayTime().dropLast(3)
                    )
                    binder.tvMealRightB.text = String.format(
                        "%s %s",
                        binder.root.resources.getString(R.string.right),
                        data.meal.timerRight.displayTime().dropLast(3)
                    )
                }
            }
            binder.tvMealType.text = binder.root.resources.getString(data.meal.type.textResId)
            binder.tvMealVolume.text = data.meal.volume.toString()
            binder.tvMealDate.text = convertLongToTime(data.dateTime)
            binder.textViewInfo.text = data.meal.info
            binder.tvMealVolumeUnit.text = data.meal.measUnit.name.toLowerCase()
            binder.imageMealType.setImageDrawable(
                ResourcesCompat.getDrawable(binder.root.resources, data.meal.type.imgResId, null)
            )
            binder.ivDelete.setOnClickListener { data.id?.let { deleteCallback.invoke(it) } }
            binder.ivEdit.setOnClickListener { data.id?.let { editCallback.invoke(it) } }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<MealEntity>() {
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