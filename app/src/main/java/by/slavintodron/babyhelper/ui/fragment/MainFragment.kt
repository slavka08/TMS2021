package by.slavintodron.babyhelper.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import by.slavintodron.babyhelper.R
import by.slavintodron.babyhelper.databinding.FragmentMainBinding
import by.slavintodron.babyhelper.ui.viewModel.MealsViewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setObservers()
    }

    private fun setObservers() {

    }

    private fun setOnClickListeners() {
        binding.button.setOnClickListener { NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_mealsFragment) }
        binding.button2.setOnClickListener { NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_weatherFragment) }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}