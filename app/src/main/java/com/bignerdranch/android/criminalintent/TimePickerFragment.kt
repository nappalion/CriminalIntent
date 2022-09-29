package com.bignerdranch.android.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "TimePickerFragment"
private const val ARG_TIME = "time"
private const val ARG_REQUEST_CODE = "timeRequestCode"
private const val RESULT_TIME_KEY = "timeRequestCode"

class TimePickerFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timeListener = TimePickerDialog.OnTimeSetListener {
                _: TimePicker, hourOfDay: Int, minute:Int ->

            val timeInMillis = ((hourOfDay * 3600000) + (minute * 60000)).toLong()

            val calendar = Calendar.getInstance()
            calendar.time = arguments?.getSerializable(ARG_TIME) as Date
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            val resultDate : Date = calendar.time

            // create our result Bundle
            val result = Bundle().apply {
                putSerializable(RESULT_TIME_KEY, resultDate)
            }

            val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE, "")
            Log.d(TAG, resultRequestCode)
            setFragmentResult(resultRequestCode, result)
        }

        val date = arguments?.getSerializable(ARG_TIME) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinutes = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinutes,
            false
        )
    }

    companion object {
        fun newInstance(date: Date, requestCode: String): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, date)
                putString(ARG_REQUEST_CODE, requestCode)
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }

        fun getSelectedDate(result: Bundle) = result.getSerializable(RESULT_TIME_KEY) as Date
    }
}