package org.stepic.droid.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import org.stepic.droid.R

class ClearVideosDialog : DialogFragment() {
    companion object {
        const val TAG = "ClearVideosDialog"
        const val REQUEST_CODE = 1599

        fun newInstance(): ClearVideosDialog =
                ClearVideosDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog
            .Builder(context)
            .setTitle(R.string.title_confirmation)
            .setMessage(R.string.clear_videos)
            .setPositiveButton(R.string.yes) { _, _ ->
                targetFragment?.onActivityResult(REQUEST_CODE, Activity.RESULT_OK, null)
            }
            .setNegativeButton(R.string.no, null)
            .create()
}
