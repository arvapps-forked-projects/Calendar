package org.fossify.calendar.dialogs

import android.app.Activity
import org.fossify.calendar.databinding.DialogVerticalLinearLayoutBinding
import org.fossify.calendar.databinding.MyCheckboxBinding
import org.fossify.calendar.extensions.withFirstDayOfWeekToFront
import org.fossify.commons.extensions.getAlertDialogBuilder
import org.fossify.commons.extensions.setupDialogStuff
import org.fossify.commons.extensions.viewBinding
import org.fossify.commons.views.MyAppCompatCheckbox

class RepeatRuleWeeklyDialog(val activity: Activity, val curRepeatRule: Int, val callback: (repeatRule: Int) -> Unit) {
    private val binding by activity.viewBinding(DialogVerticalLinearLayoutBinding::inflate)

    init {
        val days = activity.resources.getStringArray(org.fossify.commons.R.array.week_days)
        var checkboxes = ArrayList<MyAppCompatCheckbox>(7)
        for (i in 0..6) {
            val pow = 1 shl i
            MyCheckboxBinding.inflate(activity.layoutInflater).root.apply {
                isChecked = curRepeatRule and pow != 0
                text = days[i]
                id = pow
                checkboxes.add(this)
            }
        }

        checkboxes = activity.withFirstDayOfWeekToFront(checkboxes)
        checkboxes.forEach {
            binding.dialogVerticalLinearLayout.addView(it)
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(org.fossify.commons.R.string.ok) { _, _ -> callback(getRepeatRuleSum()) }
            .setNegativeButton(org.fossify.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }

    private fun getRepeatRuleSum(): Int {
        var sum = 0
        val cnt = binding.dialogVerticalLinearLayout.childCount
        for (i in 0 until cnt) {
            val child = binding.dialogVerticalLinearLayout.getChildAt(i)
            if (child is MyAppCompatCheckbox) {
                if (child.isChecked)
                    sum += child.id
            }
        }
        return sum
    }
}
