package com.etax.movies.nowplaying.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.etax.movies.nowplaying.domain.CacheManager
import com.etax.movies.nowplaying.domain.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class NowPlayingSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MovieRepository,
    private val cacheManager: CacheManager
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "NowPlayingSyncWorker"
        private const val MAX_RETRIES = 3

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val request = PeriodicWorkRequestBuilder<NowPlayingSyncWorker>(
                24, TimeUnit.HOURS  // Sync every 24h
            ).setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    30, TimeUnit.MINUTES  // Retry with backoff
                )
                .addTag(TAG)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.UPDATE,  // Replace existing
                request
            )
        }
    }

    override suspend fun doWork(): Result {
        return if (cacheManager.shouldInvalidateCache()) {
            repository.sync().fold(
                onSuccess = {
                    Result.success()
                },
                onFailure = { e ->
                    if (runAttemptCount < MAX_RETRIES) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                }
            )
        } else {
            Result.success()
        }
    }
}
