package org.stepic.droid.di

import dagger.Binds
import dagger.Module
import org.stepic.droid.storage.repositories.Repository
import org.stepic.droid.storage.repositories.assignment.AssignmentRepository
import org.stepic.droid.storage.repositories.assignment.AssignmentRepositoryImpl
import org.stepic.droid.storage.repositories.course.CourseRepositoryImpl
import org.stepic.droid.storage.repositories.lesson.LessonRepositoryImpl
import org.stepic.droid.storage.repositories.progress.ProgressRepository
import org.stepic.droid.storage.repositories.progress.ProgressRepositoryImpl
import org.stepic.droid.storage.repositories.section.SectionRepositoryImpl
import org.stepic.droid.storage.repositories.step.StepRepositoryImpl
import org.stepic.droid.storage.repositories.unit.UnitRepositoryImpl
import org.stepik.android.model.*
import org.stepik.android.model.Unit

@Module
interface RepositoryModule {

    @Binds
    fun bindCourseRepository(courseRepositoryImpl: CourseRepositoryImpl): Repository<Course>

    @Binds
    fun bindSectionRepository(sectionRepositoryImpl: SectionRepositoryImpl): Repository<Section>

    @Binds
    fun bindUnitRepository(unitRepositoryImpl: UnitRepositoryImpl): Repository<Unit>

    @Binds
    fun bindLessonRepository(lessonRepositoryImpl: LessonRepositoryImpl): Repository<Lesson>

    @Binds
    fun bindStepRepository(stepRepositoryImpl: StepRepositoryImpl): Repository<Step>

    @Binds
    fun bindAssignmentRepository(assignmentRepositoryImpl: AssignmentRepositoryImpl): AssignmentRepository

    @Binds
    fun bindProgressRepository(progressRepositoryImpl: ProgressRepositoryImpl): ProgressRepository
}
