package com.bytesculptor.applib.utilities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bytesculptor.applib.R

class AppRating {

    private var appName: String? = null
    private var fragmentManager: FragmentManager? = null
    private var context: Context? = null
    private var sharedPrefsEditor: Editor? = null

    fun appLaunch(ctx: Context, fragMgr: FragmentManager?, daysUntilPrompt: Int, startsUntilPrompt: Int, name: String?) {
        require(!(daysUntilPrompt < 0 || startsUntilPrompt < 0))
        fragmentManager = fragMgr
        context = ctx
        appName = name
        val APP_RATING_PREF = ctx.packageName + ".apprating"
        val prefs = ctx.getSharedPreferences(APP_RATING_PREF, Context.MODE_PRIVATE)
        sharedPrefsEditor = prefs.edit()

        // check if already asked
        if (prefs.getBoolean(NEVER_ASK, false)) {
            return
        }

        // increment counter
        val launchCount = 1 + prefs.getLong(COUNTER, 0)
        sharedPrefsEditor!!.putLong(COUNTER, launchCount)

        // update timestamp if 0
        var firstLaunchTimestamp = prefs.getLong(FIRST_LAUNCH, 0)
        if (firstLaunchTimestamp == 0L) {
            firstLaunchTimestamp = System.currentTimeMillis()
            sharedPrefsEditor!!.putLong(FIRST_LAUNCH, firstLaunchTimestamp)
        }
        sharedPrefsEditor!!.apply()

        // prompt dialog if limit reached
        if (launchCount >= startsUntilPrompt &&
                System.currentTimeMillis() >= firstLaunchTimestamp + daysUntilPrompt * 24 * 3600 * 1000) {
            if (prefs.getBoolean(LIKE_QUESTION_DONE, false)) {
                val rate = DialogQuestionRateApp()
                rate.show(fragmentManager!!, "likeDialog")
            } else {
                val like = DialogQuestionLike()
                like.show(fragmentManager!!, "rateDialog")
            }
        }
    }

    private fun resetCounterAndTimestamp() {
        if (sharedPrefsEditor != null) {
            sharedPrefsEditor!!.putLong(FIRST_LAUNCH, 0)
            sharedPrefsEditor!!.putLong(COUNTER, 0)
            sharedPrefsEditor!!.apply()
        }
    }

    private fun setNeverAsk() {
        if (sharedPrefsEditor != null) {
            sharedPrefsEditor!!.putBoolean(NEVER_ASK, true)
            sharedPrefsEditor!!.apply()
        }
    }

    private fun setGaveFeedback() {
        if (sharedPrefsEditor != null) {
            sharedPrefsEditor!!.putBoolean(GAVE_FEEDBACK, true)
            sharedPrefsEditor!!.apply()
        }
    }

    private fun setLikeQuestionAsked() {
        if (sharedPrefsEditor != null) {
            sharedPrefsEditor!!.putBoolean(LIKE_QUESTION_DONE, true)
            sharedPrefsEditor!!.apply()
        }
    }

    inner class DialogQuestionLike : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.szLikeThisAppTitle))
            builder.setMessage(getString(R.string.szLikeThisAppMessage))
            builder.setPositiveButton(getString(R.string.szYes)) { dialog: DialogInterface?, which: Int ->
                setLikeQuestionAsked()
                val rate = DialogQuestionRateApp()
                rate.show(parentFragmentManager, "likeDialog")
            }
            builder.setNegativeButton(getString(R.string.szLikeThisAppNo)) { dialog: DialogInterface?, which: Int ->
                setLikeQuestionAsked()
                val feedback = DialogQuestionGiveFeedback()
                feedback.show(parentFragmentManager, "feedbackDialog")
            }
            return builder.create()
        }
    }


    inner class DialogQuestionRateApp : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.szRateThisApp))
            builder.setMessage(getString(R.string.szRatingMessage))
            builder.setPositiveButton(getString(R.string.szRateNow)) { dialog: DialogInterface?, which: Int ->
                setNeverAsk()
                ExternalLinksHelper.goToAppStore(requireContext(), requireContext().packageName)
            }
            builder.setNegativeButton(getString(R.string.szNoThanks)) { dialog: DialogInterface?, _: Int -> setNeverAsk() }
            builder.setNeutralButton(getString(R.string.szRemindMe)) { dialog: DialogInterface?, _: Int -> resetCounterAndTimestamp() }
            return builder.create()
        }
    }

    inner class DialogQuestionGiveFeedback : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.szFeedback) + "?")
            builder.setMessage(getString(R.string.szAskFeedbackMessage))
            builder.setPositiveButton(getString(R.string.szYes)) { _: DialogInterface?, _: Int ->
                setGaveFeedback()
                resetCounterAndTimestamp()
                ExternalLinksHelper.sendFeedbackMail(context, appName)
            }
            builder.setNegativeButton(getString(R.string.szNoThanks)) { dialog: DialogInterface?, which: Int -> setNeverAsk() }
            return builder.create()
        }
    }

    companion object {
        private const val LIKE_QUESTION_DONE = "like_question"
        private const val GAVE_FEEDBACK = "gave_feedback"
        private const val COUNTER = "launch_counter"
        private const val FIRST_LAUNCH = "first_launch_timestamp"
        private const val NEVER_ASK = "never_ask"
    }
}