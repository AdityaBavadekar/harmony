/*
 * Copyright 2024 Aditya Bavadekar
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

package com.adityabavadekar.harmony.ui.common.activitybase

import android.content.Intent
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * An abstract activity that handles permission requests.
 * Subclasses must implement the abstract methods to handle
 * different permission states.
 */
abstract class PermissionActivityV2 : ComponentActivity() {


    private var permissionCallbackListeners: MutableMap<String, PermissionResultListener> =
        mutableMapOf()
    private var permissionQueue: MutableSet<String> = mutableSetOf()

    interface PermissionResultListener {
        /**
         * Called when the requested permission has been granted.
         *
         * @param permission The permission that was granted.
         */
        fun onGranted(permission: String)

        /**
         * Called when the requested permission has been denied.
         *
         * @param permission The permission that was denied.
         * @return true if application settings should be opened to allow permission false otherwise
         */
        fun onDenied(permission: String): Boolean

        /**
         * Called when the UI should show an explanation before requesting the permission.
         *
         * @param permission The permission for which the UI should be shown.
         * @return true if permission show be requested false otherwise
         */
        fun onShouldShowPermissionUI(permission: String): Boolean

        class Builder {
            private var onGrantedCallback: () -> Unit = {}
            private var onDeniedCallback: () -> Boolean = { false }
            private var onShouldShowPermissionUICallback: () -> Boolean = { false }

            /**
             * Called when the requested permission has been granted.
             */
            fun doOnGranted(block: () -> Unit): Builder {
                onGrantedCallback = block
                return this
            }

            /**
             * Called when the requested permission has been denied.
             *
             * @return true if application settings should be opened to allow permission false otherwise
             */
            fun doOnDenied(block: () -> Boolean): Builder {
                onDeniedCallback = block
                return this
            }

            /**
             * Called when the UI should show an explanation before requesting the permission.
             *
             * @return true if permission show be requested false otherwise
             */
            fun doOnShouldShowPermissionUI(block: () -> Boolean): Builder {
                onShouldShowPermissionUICallback = block
                return this
            }

            internal fun build(): PermissionResultListener {
                return object : PermissionResultListener {
                    override fun onGranted(permission: String) = onGrantedCallback()
                    override fun onDenied(permission: String): Boolean = onDeniedCallback()
                    override fun onShouldShowPermissionUI(permission: String): Boolean =
                        onShouldShowPermissionUICallback()
                }
            }
        }

        class ListBuilder {
            private var callbackListeners: MutableMap<String, PermissionResultListener> =
                mutableMapOf()

            fun listener(
                permission: String,
                permissionListener: Builder.() -> Builder
            ): ListBuilder {
                callbackListeners[permission] = permissionListener.invoke(Builder()).build()
                return this
            }

            internal fun build(): Map<String, PermissionResultListener> {
                return callbackListeners
            }
        }
    }

    private val multipleRequestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
            permissionMap.forEach { (permissionString, granted) ->
                Log.d(TAG, "result: '$permissionString'=$granted")
                if (!permissionCallbackListeners.containsKey(permissionString)) {
                    return@forEach
                }

                val listener = requireNotNull(permissionCallbackListeners.remove(permissionString))
                if (granted) listener.onGranted(permissionString)
                else {
                    if (listener.onDenied(permissionString)) {
                        openAppSettings()
                    }
                }
            }
            if (permissionQueue.isNotEmpty()) {
                val permission = permissionQueue.first()
                Log.d(TAG, "Queue: has permission = '$permission'")
                permissionQueue.remove(permission)
                requestPermission(permission)
            }
        }

    fun requestMultiple(
        permissions: List<String>,
        listeners: PermissionResultListener.ListBuilder.() -> PermissionResultListener.ListBuilder = { this }
    ) {
        Log.i(TAG, "requestMultiple: Asking Multiple Permission [$permissions]")
        listeners.invoke(PermissionResultListener.ListBuilder()).build()
            .forEach { (permissionString, listener) ->
                if (isGranted(permissionString)) {
                    listener.onGranted(permissionString)
                    return@forEach
                }
                permissionCallbackListeners[permissionString] = listener
            }
        launchPermissions(permissions)
    }

    private fun launchPermissions(permissions: List<String>) {
        if (permissions.isEmpty()) return

        if (permissionQueue.isNotEmpty()) {
            permissionQueue.addAll(permissions)
        } else {
            if (permissions.size > 1) permissionQueue.addAll(permissions.drop(1))
            requestPermission(permissions.first())
        }
    }

    fun requestPermission(
        permission: String,
        permissionListener: PermissionResultListener.Builder.() -> PermissionResultListener.Builder = { this }
    ) {
        val listener = permissionListener.invoke(PermissionResultListener.Builder()).build()
        permissionCallbackListeners[permission] = listener
        launchPermissions(listOf(permission))
    }

    private fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
    }

    fun requestPermission(
        permission: String
    ) {
        Log.i(TAG, "requestPermission: Asking '$permission'")

        if (!permissionCallbackListeners.containsKey(permission)) {
            return
        }
        val listener = requireNotNull(permissionCallbackListeners[permission])

        if (isGranted(permission)) {
            listener.onGranted(permission)
            return
        }

        if (shouldShowRequestRationale(permission)) {
            if (!listener.onShouldShowPermissionUI(permission)) {
                return
            }
        }

        // else
        multipleRequestPermissionLauncher.launch(arrayOf(permission))
    }

    fun openAppSettings() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addCategory(CATEGORY_DEFAULT)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            addFlags(FLAG_ACTIVITY_NO_HISTORY)
            addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG = "[PermissionActivityV2]"
    }
}