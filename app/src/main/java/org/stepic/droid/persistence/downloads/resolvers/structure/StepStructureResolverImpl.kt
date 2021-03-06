package org.stepic.droid.persistence.downloads.resolvers.structure

import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import org.stepic.droid.persistence.di.PersistenceScope
import org.stepic.droid.persistence.model.Structure
import org.stepic.droid.storage.repositories.Repository
import org.stepic.droid.storage.repositories.progress.ProgressRepository
import org.stepic.droid.util.then
import org.stepik.android.model.Step
import javax.inject.Inject

@PersistenceScope
class StepStructureResolverImpl
@Inject
constructor(
        private val stepRepository: Repository<Step>,
        private val progressRepository: ProgressRepository
): StepStructureResolver {
    override fun resolveStructure(
            courseId: Long,
            sectionId: Long,
            unitId: Long,
            lessonId: Long,
            vararg stepIds: Long
    ): Observable<Structure> = Observable
            .just(stepIds)
            .map(stepRepository::getObjects)
            .flatMap { progressRepository.syncProgresses(*it.toList().toTypedArray()) then it.toObservable() }
            .map { Structure(courseId, sectionId, unitId, lessonId, it.id) }
}