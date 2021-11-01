package by.slavintodron.babyhelper.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.slavintodron.babyhelper.databinding.ChooseDialogBinding

class ChooseDialog : DialogFragment() {

    private lateinit var listener: ChooseDialogListener
    private lateinit var binder: ChooseDialogBinding


    companion object {
        const val TAG = "ConnectionDialog"
        fun newInstance(connectionDialogListener: ChooseDialogListener) = ChooseDialog().apply {
            this.listener = connectionDialogListener
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = ChooseDialogBinding.inflate(layoutInflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)

        binder.dialogClose.setOnClickListener { dismiss() }
        binder.brastFedding.setOnClickListener {
            listener.breastsFeeding()
                dismiss()

        }
        binder.manualAdd.setOnClickListener {
            listener.manualAdd()
                dismiss()

        }
    }
}

interface ChooseDialogListener {
    fun breastsFeeding()
    fun manualAdd()
}