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

import im.vector.app.features.session.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnableNotificationsSettingUpdater @Inject constructor(
        private val updateEnableNotificationsSettingOnChangeUseCase: UpdateEnableNotificationsSettingOnChangeUseCase,
) {

    private var job: Job? = null

    fun onSessionsStarted(session: Session) {
        job?.cancel()
        job = session.coroutineScope.launch {
            updateEnableNotificationsSettingOnChangeUseCase.execute(session)
                    .launchIn(this)
        }
    }
}
