package org.stepic.droid.ui.adapters.viewhoders

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.new_course_item.view.*
import org.stepic.droid.R
import org.stepic.droid.analytic.Analytic
import org.stepic.droid.base.App
import org.stepic.droid.configuration.Config
import org.stepic.droid.core.ScreenManager
import org.stepic.droid.core.presenters.ContinueCoursePresenter
import org.stepic.droid.core.presenters.DroppingPresenter
import org.stepic.droid.model.Course
import org.stepic.droid.model.CoursesCarouselColorType
import org.stepic.droid.ui.util.changeVisibility
import org.stepic.droid.util.*
import java.util.*
import javax.inject.Inject

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE",
        justification = "Kotlin adds null check for lateinit properties, but" +
                "Findbugs highlights it as redundant")
class CourseItemViewHolder(
        val view: View,
        private val contextActivity: Activity,
        private val showMore: Boolean,
        private val joinTitle: String,
        private val continueTitle: String,
        private val coursePlaceholder: Drawable,
        private val courses: List<Course>,
        private val droppingPresenter: DroppingPresenter,
        private val continueCoursePresenter: ContinueCoursePresenter,
        private val colorType: CoursesCarouselColorType) : CourseViewHolderBase(view) {

    @Inject
    lateinit var screenManager: ScreenManager

    @Inject
    lateinit var analytic: Analytic

    @Inject
    lateinit var config: Config


    private val continueColor: Int by lazy {
        ColorUtil.getColorArgb(colorType.textColor, itemView.context)
    }
    private val joinColor: Int by lazy {
        ColorUtil.getColorArgb(R.color.join_text_color, itemView.context)
    }
    private val infoTitle: String by lazy {
        itemView.context.resources.getString(R.string.course_item_info)
    }
    private var imageViewTarget: BitmapImageViewTarget

    private val courseItemImage = view.courseItemImage
    private val courseWidgetButton = view.courseWidgetButton
    private val courseItemName = view.courseItemName
    private val learnersCountImage = view.learnersCountImage
    private val learnersCountText = view.learnersCountText
    private val courseItemMore = view.courseItemMore
    private val coursePropertiesContainer = view.coursePropertiesContainer
    private val courseWidgetInfo = view.courseWidgetInfo
    private val courseItemProgress = view.courseItemProgressView
    private val courseItemProgressTitle = view.courseItemProgressTitle
    private val courseRatingImage = view.courseRatingImage
    private val courseRatingText = view.courseRatingText


    init {
        App.component().inject(this)

        applyColorType(colorType)


        imageViewTarget = object : BitmapImageViewTarget(itemView.courseItemImage) {
            override fun setResource(resource: Bitmap) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(itemView.context.resources, resource)
                circularBitmapDrawable.cornerRadius = itemView.context.resources.getDimension(R.dimen.course_image_radius)
                courseItemImage.setImageDrawable(circularBitmapDrawable)
            }
        }
        courseWidgetButton.setOnClickListener {
            val adapterPosition = adapterPosition
            val course = getCourseSafety(adapterPosition)
            if (course != null) {
                onClickWidgetButton(course, isEnrolled(course))
            }
        }
        itemView.setOnClickListener({ onClickCourse(adapterPosition) })

        itemView.setOnLongClickListener({ v ->
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            itemView.showContextMenu()
            true
        })

        itemView.courseItemMore.setOnClickListener { view ->
            getCourseSafety(adapterPosition)?.let {
                showMore(view, it)
            }
        }

        courseWidgetInfo.applyToButton(infoTitle, continueColor, colorType.continueResource)
        courseWidgetInfo.setOnClickListener {
            getCourseSafety(adapterPosition)?.let {
                if (isEnrolled(it)) {
                    screenManager.showSections(contextActivity, it)
                } else {
                    screenManager.showCourseDescription(contextActivity, it, false)
                }
            }
        }
    }

    private fun applyColorType(colorType: CoursesCarouselColorType) {
        courseItemName.setTextColor(ColorUtil.getColorArgb(colorType.textColor, itemView.context))
        learnersCountText.setTextColor(ColorUtil.getColorArgb(colorType.textColor, itemView.context))
        learnersCountImage.setColorFilter(ColorUtil.getColorArgb(colorType.textColor, itemView.context))
        courseWidgetButton.setTextColor(ColorUtil.getColorArgb(colorType.textColor, itemView.context))
        courseRatingText.setTextColor(ColorUtil.getColorArgb(colorType.textColor, itemView.context))
        courseRatingImage.setColorFilter(ColorUtil.getColorArgb(colorType.textColor, itemView.context))
        courseItemProgress.backgroundPaintColor = ColorUtil.getColorArgb(colorType.textColor, itemView.context)
    }

    private fun showMore(view: View, course: Course) {
        val morePopupMenu = PopupMenu(itemView.context, view)
        morePopupMenu.inflate(ContextMenuCourseUtil.getMenuResource(course))

        morePopupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item_info -> {
                    screenManager.showCourseDescription(itemView.context, course)
                    true
                }
                R.id.menu_item_unroll -> {
                    droppingPresenter.dropCourse(course)
                    true
                }
                else -> false
            }
        }
        morePopupMenu.show()
    }

    private fun onClickCourse(position: Int) {
        if (position >= courses.size || position < 0) return
        analytic.reportEvent(Analytic.Interaction.CLICK_COURSE)
        val course = courses[position]
        if (course.enrollment != 0) {
            screenManager.showSections(contextActivity, course)
        } else {
            screenManager.showCourseDescription(contextActivity, course)
        }
    }

    private fun onClickWidgetButton(course: Course, enrolled: Boolean) {
        if (enrolled) {
            analytic.reportEvent(Analytic.Interaction.CLICK_CONTINUE_COURSE)
            continueCoursePresenter.continueCourse(course) //provide position?
        } else {
            screenManager.showCourseDescription(contextActivity, course, true)
        }
    }

    private fun getCourseSafety(adapterPosition: Int): Course? {
        return if (adapterPosition >= courses.size || adapterPosition < 0) {
            null
        } else {
            courses[adapterPosition]
        }
    }

    override fun setDataOnView(position: Int) {
        val course = courses[position]

        courseItemName.text = course.title
        Glide
                .with(itemView.context)
                .load(StepikLogicHelper.getPathForCourseOrEmpty(course, config))
                .asBitmap()
                .placeholder(coursePlaceholder)
                .fitCenter()
                .into(imageViewTarget)

        val needShowLearners = course.learnersCount > 0
        if (needShowLearners) {
            learnersCountText.text = String.format(Locale.getDefault(), "%d", course.learnersCount)
        }
        learnersCountImage.changeVisibility(needShowLearners)
        learnersCountText.changeVisibility(needShowLearners)

        if (isEnrolled(course)) {
            courseWidgetInfo.setText(R.string.course_item_syllabus)
            showContinueButton()
        } else {
            courseWidgetInfo.setText(R.string.course_item_info)
            showJoinButton()
        }

        val needShowProgress = bindProgressView(course)
        val needShowRating = bindRatingView(course)

        val showContainer = needShowLearners || needShowProgress || needShowRating
        coursePropertiesContainer.changeVisibility(showContainer)

        courseItemMore.changeVisibility(showMore)
    }

    private fun bindProgressView(course: Course): Boolean {
        val progressPercent: Int? = ProgressUtil.getProgressPercent(course.progressObject)
        val needShow =
                if (progressPercent != null && progressPercent > 0) {
                    prepareViewForProgress(progressPercent)
                    true
                } else {
                    false
                }
        courseItemProgress.changeVisibility(needShow)
        courseItemProgressTitle.changeVisibility(needShow)
        return needShow
    }

    private fun prepareViewForProgress(progressPercent: Int) {
        courseItemProgress.progress = progressPercent / 100f
        courseItemProgressTitle.text = itemView
                .resources
                .getString(R.string.percent_symbol, progressPercent)
    }

    private fun bindRatingView(course: Course): Boolean {
        val needShow = course.rating > 0
        if (needShow) {
            courseRatingText.text = String.format(Locale.ROOT, itemView.resources.getString(R.string.course_rating_value), course.rating)
        }
        courseRatingImage.changeVisibility(needShow)
        courseRatingText.changeVisibility(needShow)
        return needShow
    }

    private fun isEnrolled(course: Course?): Boolean =
            course != null && course.enrollment != 0

    private fun showJoinButton() {
        courseWidgetButton.applyToButton(joinTitle, joinColor, colorType.joinResource)
    }

    private fun showContinueButton() {
        courseWidgetButton.applyToButton(continueTitle, continueColor, colorType.continueResource)
    }

    private fun TextView.applyToButton(title: String, @ColorInt textColor: Int, @DrawableRes background: Int) {
        this.text = title
        this.setTextColor(textColor)
        this.setBackgroundResource(background)
    }

}