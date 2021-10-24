package by.slavintodron.babyhelper.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import by.slavintodron.babyhelper.R
import by.slavintodron.babyhelper.databinding.FragmentWeatherBinding
import by.slavintodron.babyhelper.ui.adapter.WeatherAdapter
import by.slavintodron.babyhelper.ui.viewModel.WeatherViewModel

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by activityViewModels()
    private var adapter: WeatherAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = WeatherAdapter()
        binding.rvWeather.adapter = adapter
        binding.rvWeather.layoutManager = LinearLayoutManager(requireContext())
        initObservers()
        viewModel.getWeather("lviv")
    }

    private fun initObservers() {
        viewModel.weather.observe(viewLifecycleOwner){
            adapter?.submitList(it.list)
        }
        viewModel.error.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), getString(R.string.error, it), Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}