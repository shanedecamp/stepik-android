package org.stepic.droid.model;

import android.content.Context;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.stepic.droid.R;
import org.stepic.droid.base.MainApplication;
import org.stepic.droid.configuration.IConfig;

import java.io.Serializable;

import javax.inject.Inject;

public class Course implements Serializable {

    @Inject
    IConfig mConfig;

    Context mContext;

    private DateTimeFormatter mFormatForView;

    private long id;
    private String summary;
    private String workload;
    private String cover;
    private String intro;
    private String course_format;
    private String target_audience;
    private String certificate_footer;
    private String certificate_cover_org;
    private long[] instructors;
    private String certificate;
    private String requirements;
    private String description;
    private int total_units;
    private int enrollment;
    private long owner;
    private boolean is_contest;
    private boolean is_featured;
    private boolean is_spoc;
    private boolean is_active;
    private String certificate_link;
    private String title;
    private String begin_date_source;
    private String last_deadline;
    private String language;
    private boolean is_public;
    private String slug; //link to ../course/#slug#

    private DateTime mBeginDateTime = null;

    private DateTime mEndDateTime = null;

    private String formatForView = null;

    public Course() {
        mContext = MainApplication.getAppContext();
        MainApplication.component(MainApplication.getAppContext()).inject(this);

        mFormatForView = DateTimeFormat
                .forPattern(mConfig.getDatePatternForView())
                .withZone(DateTimeZone.getDefault());
    }

    public String getDateOfCourse() {
        if (formatForView != null) return formatForView;

        StringBuilder sb = new StringBuilder();

        if (begin_date_source == null && last_deadline == null) {
            sb.append("");
        } else if (last_deadline == null) {
            sb.append(mContext.getResources().getString(R.string.begin_date));
            sb.append(": ");

            try {
                sb.append(getPresentOfDate(begin_date_source, mBeginDateTime));
            } catch (Throwable throwable) {
                return "";
            }
        } else if (begin_date_source != null) {
            //both is not null

            try {

                sb.append(getPresentOfDate(begin_date_source, mBeginDateTime));

                sb.append(" - ");

                sb.append(getPresentOfDate(last_deadline, mEndDateTime));
            } catch (Throwable throwable) {
                return "";
            }
        }
        formatForView = sb.toString();
        return formatForView;
    }

    private String getPresentOfDate(String dateInISOformat, DateTime dateTime) {
        dateTime = new DateTime(dateInISOformat);
        String result = mFormatForView.print(dateTime);
        return result;
    }


    @Nullable
    public DateTime getEndDateTime() {
        if (mEndDateTime != null)
            return mEndDateTime;

        if (last_deadline == null)
        {
            mEndDateTime = null; //infinity
        }
        else
        {
            mEndDateTime = new DateTime(last_deadline);
        }
        return mEndDateTime;

    }

    @Nullable
    public DateTime getBeginDateTime() {
        if (mBeginDateTime != null)
            return mBeginDateTime;

        if (begin_date_source == null)
        {
            mBeginDateTime = null; //infinity
        }
        else
        {
            mBeginDateTime = new DateTime(begin_date_source);
        }
        return mBeginDateTime;
    }

    public long getCourseId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getWorkload() {
        return workload;
    }

    public String getCover() {
        return cover;
    }

    public String getIntro() {
        return intro;
    }

    public String getCourse_format() {
        return course_format;
    }

    public String getTarget_audience() {
        return target_audience;
    }

    public String getCertificate_footer() {
        return certificate_footer;
    }

    public String getCertificate_cover_org() {
        return certificate_cover_org;
    }

    public long[] getInstructors() {
        return instructors;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getDescription() {
        return description;
    }

    public int getTotal_units() {
        return total_units;
    }

    public int getEnrollment() {
        return enrollment;
    }

    public boolean is_featured() {
        return is_featured;
    }

    public boolean is_spoc() {
        return is_spoc;
    }

    public String getCertificate_link() {
        return certificate_link;
    }

    public long getOwner() {
        return owner;
    }

    public boolean is_contest() {
        return is_contest;
    }

    public String getLanguage() {
        return language;
    }

    public boolean is_public() {
        return is_public;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public void setmFormatForView(DateTimeFormatter mFormatForView) {
        this.mFormatForView = mFormatForView;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setCourse_format(String course_format) {
        this.course_format = course_format;
    }

    public void setTarget_audience(String target_audience) {
        this.target_audience = target_audience;
    }

    public void setCertificate_footer(String certificate_footer) {
        this.certificate_footer = certificate_footer;
    }

    public void setCertificate_cover_org(String certificate_cover_org) {
        this.certificate_cover_org = certificate_cover_org;
    }

    public void setInstructors(long[] instructors) {
        this.instructors = instructors;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTotal_units(int total_units) {
        this.total_units = total_units;
    }

    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public void setIs_contest(boolean is_contest) {
        this.is_contest = is_contest;
    }

    public void setIs_featured(boolean is_featured) {
        this.is_featured = is_featured;
    }

    public void setIs_spoc(boolean is_spoc) {
        this.is_spoc = is_spoc;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public void setCertificate_link(String certificate_link) {
        this.certificate_link = certificate_link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBegin_date_source(String begin_date_source) {
        this.begin_date_source = begin_date_source;
    }

    public void setLast_deadline(String last_deadline) {
        this.last_deadline = last_deadline;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setmBeginDateTime(DateTime mBeginDateTime) {
        this.mBeginDateTime = mBeginDateTime;
    }

    public void setmEndDateTime(DateTime mEndDateTime) {
        this.mEndDateTime = mEndDateTime;
    }

    public void setFormatForView(String formatForView) {
        this.formatForView = formatForView;
    }

    public String getBegin_date_source() {
        return begin_date_source;
    }

    public String getLast_deadline() {
        return last_deadline;
    }
}
