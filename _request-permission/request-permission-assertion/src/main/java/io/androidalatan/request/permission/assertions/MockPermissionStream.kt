package io.androidalatan.request.permission.assertions

import androidx.annotation.VisibleForTesting
import io.androidalatan.request.permission.api.PermissionResult
import io.androidalatan.request.permission.api.PermissionStream

class MockPermissionStream(
    @get:VisibleForTesting val handler: MockPermissionHandler = MockPermissionHandler(
        MockPermissionExplanationBuilderFactory()
    )
) : PermissionStream {

    @VisibleForTesting
    internal var callbacks = mutableListOf<PermissionStream.Callback>()

    override fun registerAndRunCallback(callback: PermissionStream.Callback) {
        callbacks.add(callback)
        callback.onCallback(handler)
    }

    override fun unregisterCallback(callback: PermissionStream.Callback) {
        callbacks.remove(callback)
    }

    fun showExplanation(reqCode: Int) {
        handler.showExplanation(reqCode)
    }

    fun permitFully(reqCode: Int, result: PermissionResult) {
        handler.permitFully(reqCode, result)
    }

    fun permitPartially(reqCode: Int, result: PermissionResult) {
        handler.permitPartially(reqCode, result)
    }

    fun denyPermanently(reqCode: Int, deniedList: List<String>, result: PermissionResult) {
        handler.denyPermanently(reqCode, deniedList, result)
    }
}