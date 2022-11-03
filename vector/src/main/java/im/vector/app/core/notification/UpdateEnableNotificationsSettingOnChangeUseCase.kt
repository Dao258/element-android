/*
 * Copyright (c) 2022 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.app.core.notification

import im.vector.app.features.settings.VectorPreferences
import im.vector.app.features.settings.devices.v2.notification.GetNotificationsStatusUseCase
import im.vector.app.features.settings.devices.v2.notification.NotificationsStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onEach
import org.matrix.android.sdk.api.session.Session
import timber.log.Timber
import javax.inject.Inject

/**
 * Listen for changes in either Pusher or Account data to update the local enable notifications
 * setting for the current device.
 */
class UpdateEnableNotificationsSettingOnChangeUseCase @Inject constructor(
        private val vectorPreferences: VectorPreferences,
        private val getNotificationsStatusUseCase: GetNotificationsStatusUseCase,
) {

    // TODO add unit tests
    fun execute(session: Session): Flow<NotificationsStatus> {
        val deviceId = session.sessionParams.deviceId ?: return emptyFlow()
        return getNotificationsStatusUseCase.execute(session, deviceId)
                .onEach(::updatePreference)
    }

    private fun updatePreference(notificationStatus: NotificationsStatus) {
        Timber.d("updatePreference with status=$notificationStatus")
        when (notificationStatus) {
            NotificationsStatus.ENABLED -> vectorPreferences.setNotificationEnabledForDevice(true)
            NotificationsStatus.DISABLED -> vectorPreferences.setNotificationEnabledForDevice(false)
            else -> Unit
        }
    }
}
