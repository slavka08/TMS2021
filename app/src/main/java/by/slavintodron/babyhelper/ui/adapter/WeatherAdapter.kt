package by.slavintodron.babyhelper.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.slavintodron.babyhelper.R
import by.slavintodron.babyhelper.databinding.WeatherItemBinding
import by.slavintodron.babyhelper.entity.weatherEntity.WeatherData
import com.squareup.picasso.Picasso
import java.util.*


class WeatherAdapter : ListAdapter<WeatherData, WeatherAdapter.WeatherViewHolder>(DIFF) {

    inner class WeatherViewHolder(private val binding: WeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WeatherData) {
            binding.textView.text = data.dt_txt
            binding.textViewTemp.text =
                itemView.context.resources.getString(R.string.temp, data.main.temp)

            binding.textViewHum.text = itemView.context.resources.getString(
                R.string.humidity,
                data.main.humidity,
                itemView.context.resources.getString(R.string.percent)
            )
            binding.textViewWindSpeed.text =
                itemView.context.resources.getString(R.string.wind_speed, data.wind.speed)
            val urlWetherIcon = String.format(
                Locale.getDefault(),
                ("https://openweathermap.org/img/wn/%s@2x.png"),
                data.weather.first().icon
            )
            Picasso.get().load(urlWetherIcon).into(binding.imageViewSun)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<WeatherData>() {
            override fun areItemsTheSame(
                oldItem: WeatherData,
                newItem: WeatherData
            ): Boolean = false

            override fun areContentsTheSame(
                oldItem: WeatherData,
                newItem: WeatherData
            ): Boolean = false
        }
    }
}