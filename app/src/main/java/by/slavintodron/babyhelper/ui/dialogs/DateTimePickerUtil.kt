package by.slavintodron.babyhelper.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.slavintodron.babyhelper.databinding.DialogDateTimePickerBinding
import by.slavintodron.babyhelper.utils.visibleOrGone
import java.util.*

class DateTimePickerUtil : DialogFragment() {
    private lateinit var binding: DialogDateTimePickerBinding
    private var dateTimeToShow: Date? = null
    private lateinit var okCallback: ((dateTime: Long) -> Unit)
    private var title: String? = null
    private var time24h: Boolean = false
    private var showTimePicker: Boolean = true
    private var showDatePicker: Boolean = true
    private var canSelectFutureDate: Boolean = true

    companion object {
        const val TAG = "datePickerNutritionDialog"
        fun newInstance(
            title: String? = null,
            time24h: Boolean = true,
            canSelectFutureDate: Boolean = true,
            dateTime: Date? = null,
            showTimePicker: Boolean = true,
            showDatePicker: Boolean = true,
            okCallback: (dateTime: Long) -> Unit
        ): DateTimePickerUtil {
            val fragment = DateTimePickerUtil()
            fragment.showTimePicker = showTimePicker
            fragment.okCallback = okCallback
            fragment.dateTimeToShow = dateTime
            fragment.title = title
            fragment.time24h = time24h
            fragment.showDatePicker = showDatePicker
            fragment.canSelectFutureDate = canSelectFutureDate
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogDateTimePickerBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.timePicker.visibleOrGone(showTimePicker)
        binding.datePicker.visibleOrGone(showDatePicker)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setupPickers()
        setOnClickListeners()
    }

    private fun setTitle() {
        title?.let {
            binding.textViewTitle.text = it
            binding.textViewTitle.visibility = View.VISIBLE
        }
    }

    private fun setupPickers() {
        binding.timePicker.setIs24HourView(time24h)
        dateTimeToShow?.let {
            val calendar = GregorianCalendar()
            calendar.timeInMillis = it.time
            binding.datePicker.init(
                calendar.get(GregorianCalendar.YEAR),
                calendar.get(GregorianCalendar.MONTH),
                calendar.get(GregorianCalendar.DAY_OF_MONTH),
                null
            )

            binding.timePicker.minute = calendar.get(GregorianCalendar.MINUTE)
            binding.timePicker.hour = calendar.get(GregorianCalendar.HOUR_OF_DAY)
        }
    }

    private fun setOnClickListeners() {
        binding.buttonOk.setOnClickListener {
            val calendar = GregorianCalendar.getInstance()
            val today = calendar.time
            calendar.set(
                binding.datePicker.year,
                binding.datePicker.month,
                binding.datePicker.dayOfMonth,
                binding.timePicker.hour,
                binding.timePicker.minute
            )
            if (canSelectFutureDate || (today.time >= calendar.time.time)) {
                okCallback(calendar.time.time)
                dismiss()
            } else {
                showDialogFutureDate()
            }
        }
        binding.buttonCancel.setOnClickListener { dismiss() }
    }

    private fun showDialogFutureDate() {
/*        Dialog.newInstance(
            title = getString(R.string.future_date_error_title),
            message = getString(R.string.future_date_error),
            positiveButtonText = getString(R.string.ok),
            onPositiveClick = { }
        ).show(childFragmentManager, Dialog.TAG)*/
    }
}