package org.stepic.droid.persistence.repository.progress

import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import org.stepic.droid.persistence.model.DownloadProgress
import org.stepic.droid.persistence.model.PersistentItem
import org.stepic.droid.persistence.model.isCorrect
import org.stepic.droid.persistence.repository.countItemProgress
import org.stepic.droid.persistence.storage.dao.PersistentItemDao
import org.stepic.droid.persistence.storage.dao.SystemDownloadsDao
import org.stepic.droid.util.merge
import org.stepic.droid.util.zip

abstract class ProgressRepositoryBase(
        private val updatesObservable: Observable<PersistentItem>,
        private val intervalUpdatesObservable: Observable<kotlin.Unit>,

        private val systemDownloadsDao: SystemDownloadsDao,
        private val persistentItemDao: PersistentItemDao
): ProgressRepository {
    private companion object {
        private fun List<PersistentItem>.getDownloadIdsOfCorrectItems() =
                this.filter { it.status.isCorrect }.map{ it.downloadId }.toLongArray()
    }

    override fun getProgress(vararg ids: Long): Observable<DownloadProgress> =
            ids.toObservable().flatMap(::getItemProgress)

    private fun getItemProgress(itemId: Long) =
            getItemUpdateObservable(itemId)                      // listen for updates
                    .flatMap { getPersistentObservable(itemId) } // fetch from DB
                    .flatMap(::fetchSystemDownloads)
                    .map { (persistentItems, downloadItems) ->   // count progresses
                        DownloadProgress(itemId, countItemProgress(persistentItems, downloadItems))
                    }.distinctUntilChanged()                     // exclude repetitive events

    private fun getPersistentObservable(itemId: Long) =
            persistentItemDao.getItems(mapOf(persistentItemKeyFieldColumn to itemId.toString()))

    private fun fetchSystemDownloads(items: List<PersistentItem>) =
            Observable.just(items) zip systemDownloadsDao.get(*items.getDownloadIdsOfCorrectItems())

    private fun getItemUpdateObservable(itemId: Long) =
            updatesObservable.filter { it.keyFieldValue == itemId }.map { kotlin.Unit } merge intervalUpdatesObservable // ?? debounce

    protected abstract val PersistentItem.keyFieldValue: Long
    protected abstract val persistentItemKeyFieldColumn: String
}