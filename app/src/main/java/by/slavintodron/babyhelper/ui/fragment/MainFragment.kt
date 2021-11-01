package by.slavintodron.babyhelper.ui.fragment

import android.R.attr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import by.slavintodron.babyhelper.R
import by.slavintodron.babyhelper.databinding.FragmentMainBinding
import by.slavintodron.babyhelper.ui.viewModel.MainViewModel
import by.slavintodron.babyhelper.ui.viewModel.MealsViewModel
import com.squareup.picasso.Picasso
import java.util.*
import android.R.attr.identifier
import android.content.Context

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import by.slavintodron.babyhelper.utils.convertLongToDate
import by.slavintodron.babyhelper.utils.convertLongToTime
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback


class MainFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        binding.mapViewTour.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        binding.mapViewTour.onDestroy()
        _binding = null
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapViewTour.onCreate(savedInstanceState)
        binding.mapViewTour.getMapAsync(this)
        setOnClickListeners()
        setObservers()
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isDarkTheme = prefs.getString("chbCity","Minsk")
        if (isDarkTheme != null) {
            viewModel.getWeather(isDarkTheme)
        }
        viewModel.getLastMeal()
    }

    private fun setObservers() {
        viewModel.weather.observe(viewLifecycleOwner){
            binding.textViewTemp.text = getString(R.string.temp, it.list[0].main.temp)
            binding.textViewHum.text = getString(
                R.string.humidity,
                it.list[0].main.humidity,
                getString(R.string.percent)
            )
            binding.textViewWindSpeed.text = getString(R.string.wind_speed, it.list[0].wind.speed)
            val urlWetherIcon = String.format(
                Locale.getDefault(),
                ("https://openweathermap.org/img/wn/%s@2x.png"),
                it.list[0].weather.first().icon
            )
            Picasso.get().load(urlWetherIcon).into(binding.imageViewSun)
        }
        viewModel.mealData.observe(viewLifecycleOwner){
            binding.tvMealType.text = it.meal.type.toString()
            binding.tvMealVolume.text = String.format("%s %s", it.meal.volume.toString(), it.meal.measUnit.name.toLowerCase())
            binding.tvMealDate.text = String.format("%s %s", convertLongToDate(it.dateDay), convertLongToTime(it.dateTime))
        }
    }

    private fun setOnClickListeners() {
        binding.mealsCard.setOnClickListener { NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToMealsFragment()) }
        binding.weatherCard.setOnClickListener { NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToWeatherFragment()) }
        binding.imageViewWeatherSettings.setOnClickListener { NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment()) }
        binding.tourLayout.setOnClickListener { NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToMapsFragment()) }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapViewTour.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapViewTour.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var mGoogleMap = googleMap;
        mGoogleMap.uiSettings.isZoomControlsEnabled = true;
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}