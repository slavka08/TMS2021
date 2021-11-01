package by.slavintodron.babyhelper.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.slavintodron.babyhelper.databinding.FragmentMealsBinding
import by.slavintodron.babyhelper.ui.adapter.MealsAdapter
import by.slavintodron.babyhelper.ui.dialogs.ChooseDialog
import by.slavintodron.babyhelper.ui.dialogs.ChooseDialogListener
import by.slavintodron.babyhelper.ui.viewModel.MealsViewModel
import by.slavintodron.babyhelper.utils.ChartUtils
import by.slavintodron.babyhelper.utils.convertLongToDate
import java.util.*

class MealsFragment : Fragment() {
    private var _binding: FragmentMealsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MealsViewModel by activityViewModels()
    private var adapter: MealsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        setOnClickListeners()
        updateDateText()
        ChartUtils.setChartDataCalories(binding.chart, 100f, 300f, requireContext())
    }

    private fun setOnClickListeners() {
        binding.fabAddMeal.setOnClickListener { dialog() }
        binding.buttonNextDate.setOnClickListener {
            viewModel.nextDate()
            updateDateText()
        }
        binding.buttonPrevDate.setOnClickListener {
            viewModel.prevDate()
            updateDateText()
        }
    }

    private fun dialog() {
        ChooseDialog.newInstance(object : ChooseDialogListener {
            override fun breastsFeeding() {
                NavHostFragment.findNavController(this@MealsFragment)
                    .navigate(MealsFragmentDirections.actionMealsFragmentToFeedingTimerFragment())
            }

            override fun manualAdd() {
                NavHostFragment.findNavController(this@MealsFragment)
                    .navigate(MealsFragmentDirections.actionMealsFragmentToMealsAddFragment(0))
            }
        }).show(childFragmentManager, ChooseDialog.TAG)
    }

    private fun loadMeals() {
        viewModel.getMeals()
    }

    private fun updateDateText() {
        binding.textViewCurrentDate.text = convertLongToDate(viewModel.calendar.timeInMillis)
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