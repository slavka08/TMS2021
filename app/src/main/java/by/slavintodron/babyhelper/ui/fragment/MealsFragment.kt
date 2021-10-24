package by.slavintodron.babyhelper.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.slavintodron.babyhelper.databinding.FragmentMealsBinding
import by.slavintodron.babyhelper.ui.adapter.MealsAdapter
import by.slavintodron.babyhelper.ui.viewModel.MealsViewModel

class MealsFragment : Fragment() {
    private var _binding: FragmentMealsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MealsViewModel by activityViewModels()

    private var adapter: MealsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MealsAdapter()
        binding.rvMeals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMeals.adapter = adapter
        initObservers()
        loadMeals()
    }

    private fun loadMeals(){
        viewModel.getAllMeals()
    }

    private fun initObservers() {
        viewModel.meals.observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}