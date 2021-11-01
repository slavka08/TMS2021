package by.slavintodron.babyhelper.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.NavHostFragment
import by.slavintodron.babyhelper.R
import by.slavintodron.babyhelper.databinding.FragmentManualFeedingBinding
import by.slavintodron.babyhelper.entity.Breast
import by.slavintodron.babyhelper.service.FeedingService
import by.slavintodron.babyhelper.ui.viewModel.FeedingWithTimerModel
import by.slavintodron.babyhelper.utils.displayTime


class FeedingTimerFragment : Fragment(), LifecycleObserver {
    private var _binding: FragmentManualFeedingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FeedingWithTimerModel by viewModels()
    private var actionNeedToStartService = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentManualFeedingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val leftTimer = arguments?.getLong(FeedingService.LEFT_TIMER, 0) ?: 0
        val rightTimer = arguments?.getLong(FeedingService.RIGHT_TIMER, 0) ?: 0

        viewModel.setTimerValue(leftTimer, rightTimer)
        initObservers()
        initClickListeners()
    }


    private fun initObservers() {
        viewModel.timerDataL.observe(viewLifecycleOwner) {
            binding.tvLeftTimer.text = it.displayTime().dropLast(3)
        }
        viewModel.timerDataR.observe(viewLifecycleOwner) {
            binding.tvRightTimer.text = it.displayTime().dropLast(3)
        }
        viewModel.usedTimer.observe(viewLifecycleOwner) {
            binding.buttonTimerRight.setButtonStatus(it == Breast.RIGHT && viewModel.runTimer)
            binding.buttonTimerLeft.setButtonStatus(it == Breast.LEFT && viewModel.runTimer)
        }
    }

    private fun Button.setButtonStatus(runStatus: Boolean) {
        this.text = if (runStatus) getString(R.string.pause) else getString(R.string.resume)
    }

    private fun initClickListeners() {
        binding.buttonTimerLeft.setOnClickListener { viewModel.toggleTimerStatus(Breast.LEFT) }
        binding.buttonTimerRight.setOnClickListener { viewModel.toggleTimerStatus(Breast.RIGHT) }
        binding.buttonCancel.setOnClickListener {
            actionNeedToStartService = false
            NavHostFragment.findNavController(this).popBackStack()
        }
        binding.buttonSave.setOnClickListener {
            actionNeedToStartService = false
            NavHostFragment.findNavController(this).navigate(
                FeedingTimerFragmentDirections.actionFeedingTimerFragmentToMealsAddFragment(
                    viewModel.timerDataL.value ?: 0L,
                    viewModel.timerDataR.value ?: 0L
                )
            )
        }
    }

    fun onAppBackgrounded() {
        val startIntent = Intent(requireContext(), FeedingService::class.java)
        startIntent.action = FeedingService.COMMAND_START
        startIntent.putExtra(FeedingService.COMMAND_ID, FeedingService.COMMAND_START)
        startIntent.putExtra(FeedingService.LEFT_TIMER, viewModel.timerDataL.value)
        startIntent.putExtra(FeedingService.RIGHT_TIMER, viewModel.timerDataR.value)
        requireContext().startService(startIntent)
    }

    override fun onStop() {
        if (actionNeedToStartService) onAppBackgrounded()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(left: Long, right: Long) =
            FeedingTimerFragment().apply {
                arguments = Bundle().apply {
                    putLong(FeedingService.LEFT_TIMER, left)
                    putLong(FeedingService.RIGHT_TIMER, right)
                }
            }
    }
}