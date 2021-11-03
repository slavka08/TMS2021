package by.slavintodron.babyhelper.ui.fragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import by.slavintodron.babyhelper.databinding.FragmentMealAddBinding
import by.slavintodron.babyhelper.entity.BabyMeal
import by.slavintodron.babyhelper.entity.MealEntity
import by.slavintodron.babyhelper.entity.MealType
import by.slavintodron.babyhelper.entity.MeasureUnits
import by.slavintodron.babyhelper.ui.dialogs.DateTimePickerUtil
import by.slavintodron.babyhelper.ui.viewModel.EditMealsViewModel
import by.slavintodron.babyhelper.utils.displayTime
import by.slavintodron.babyhelper.utils.toDayOnlyDate
import by.slavintodron.babyhelper.utils.toDayOnlyTime
import by.slavintodron.babyhelper.utils.visibleOrGone
import java.util.*


class MealsAddFragment : Fragment() {
    private var _binding: FragmentMealAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditMealsViewModel by activityViewModels()
    private var edit = false
    val args: MealsAddFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMealAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, MealType.values())
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerMealType.adapter = adapter
        viewModel.clear()
        val adapterUnits = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, MeasureUnits.values())
        adapterUnits.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerMealUnit.adapter = adapterUnits
        edit = false
        when (val mealId = args.mealId) {
            0 -> {
                if (args.timerLeft > 0 || args.timerRight > 0) {
                    binding.textInputTimerLLayout.visibility = View.VISIBLE
                    binding.textInputTimerRLayout.visibility = View.VISIBLE
                    viewModel.timerL = args.timerLeft
                    viewModel.timerR = args.timerRight
                    binding.spinnerMealUnit.setSelection(4)
                    binding.volumeLayout.visibility = View.GONE
                } else {
                    binding.volumeLayout.visibility = View.VISIBLE
                    binding.textInputTimerLLayout.visibility = View.GONE
                    binding.textInputTimerRLayout.visibility = View.GONE
                }
            }
            else -> loadMealForEdit(mealId)
        }
        binding.timerR.setText(viewModel.timerL.displayTime().dropLast(3))
        binding.timerL.setText(viewModel.timerR.displayTime().dropLast(3))
        initObservers()
        setOnClickListeners()
    }


    private fun initObservers() {
        viewModel.mealData.observe(viewLifecycleOwner) { setDataToFragment(it) }
    }

    private fun MakeFieldsViewForVolume(visble: Boolean) {
        binding.textInputTimerLLayout.visibleOrGone(!visble)
        binding.textInputTimerRLayout.visibleOrGone(!visble)
        binding.volumeLayout.visibleOrGone(visble)
    }

    private fun setDataToFragment(meal: MealEntity) {
        if (meal.meal.timerLeft > 0 || meal.meal.timerRight > 0) {
            MakeFieldsViewForVolume(false)
        } else {
            MakeFieldsViewForVolume(true)
        }
        binding.tiVolume.setText(meal.meal.volume.toString())
        binding.tiInfo.setText(meal.meal.info)
        binding.timerR.setText(meal.meal.timerRight.displayTime().dropLast(3))
        binding.timerL.setText(meal.meal.timerLeft.displayTime().dropLast(3))
    }

    private fun loadMealForEdit(mealId: Int) {
        edit = true
        viewModel.getMeal(mealId)
    }

    private fun setOnClickListeners() {
        binding.buttonCancel.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
        binding.buttonOk.setOnClickListener {
            viewModel.setMeal(getMeal())
            viewModel.insertDB()
            NavHostFragment.findNavController(this).popBackStack()
        }
        binding.timerL.setOnClickListener {
            DateTimePickerUtil.newInstance(canSelectFutureDate = false, dateTime = Date(viewModel.timerL), showDatePicker = false) {
                viewModel.timerL = it
                binding.timerL.setText(viewModel.timerL.displayTime().dropLast(3))
            }.show(childFragmentManager, DateTimePickerUtil.TAG)
        }

        binding.timerR.setOnClickListener {
            DateTimePickerUtil.newInstance(canSelectFutureDate = false, dateTime = Date(viewModel.timerR),  showDatePicker = false) {
                viewModel.timerR = it
                binding.timerR.setText(viewModel.timerR.displayTime().dropLast(3))
            }.show(childFragmentManager, DateTimePickerUtil.TAG)
        }

        binding.spinnerMealType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when ( parent?.getItemAtPosition(position)  as MealType) {
                    MealType.BREAST_FEEDING -> MakeFieldsViewForVolume(false)
                    else -> MakeFieldsViewForVolume(true)
                }
            }
        }
    }

    private fun getMeal(): MealEntity {
        return MealEntity(
            id = viewModel.mealData.value?.id,
            dateDay = viewModel.mealData.value?.dateDay ?: GregorianCalendar().toDayOnlyDate(),
            dateTime = viewModel.mealData.value?.dateTime ?: GregorianCalendar().toDayOnlyTime(),
            meal = BabyMeal(
                timerLeft = viewModel.timerL,
                timerRight = viewModel.timerR,
                type = binding.spinnerMealType.selectedItem as MealType,
                measUnit = binding.spinnerMealUnit.selectedItem as MeasureUnits,
                volume = binding.tiVolume.text.toString().toFloatOrNull() ?: 0f,
                info = binding.tiInfo.text.toString()
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}