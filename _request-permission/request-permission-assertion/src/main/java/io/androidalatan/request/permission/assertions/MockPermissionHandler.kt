package io.androidalatan.request.permission.assertions

import androidx.annotation.VisibleForTesting
import io.androidalatan.request.permission.api.PermissionExplanation
import io.androidalatan.request.permission.api.PermissionExplanationBuilder
import io.androidalatan.request.permission.api.PermissionHandler
import io.androidalatan.request.permission.api.PermissionResult
import io.androidalatan.request.permission.api.exception.PermissionGrantException

class MockPermissionHandler(
    @get:VisibleForTesting internal val permissionExplanationBuilderFactory: MockPermissionExplanationBuilderFactory
) : PermissionHandler {

    private val failCallback = mutableMapOf<Int, (Exception) -> Unit>()
    private val successCallback = mutableMapOf<Int, (PermissionResult) -> Unit>()
    private val explanationCallback = mutableMapOf<Int, ((PermissionExplanationBuilder) -> PermissionExplanation)?>()

    override fun onResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) = Unit

    override fun request(
        permissions: Array<String>,
        reqCode: Int,
        explanation: ((PermissionExplanationBuilder) -> PermissionExplanation)?,
        fail: (Exception) -> Unit,
        success: (PermissionResult) -> Unit
    ) {
        explanationCallback[reqCode] = explanation
        successCallback[reqCode] = success
        failCallback[reqCode] = fail
    }

    fun showExplanation(reqCode: Int) {
        explanationCallback[reqCode]
            ?.invoke(permissionExplanationBuilderFactory.explanationBuilder())
        clearCallbackOfInvoked(reqCode)
    }

    fun permitFully(reqCode: Int, result: PermissionResult) {
        successCallback[reqCode]
            ?.invoke(result)
        clearCallbackOfInvoked(reqCode)
    }

    fun permitPartially(reqCode: Int, result: PermissionResult) {
        successCallback[reqCode]
            ?.invoke(result)
        clearCallbackOfInvoked(reqCode)
    }

    fun denyPermanently(reqCode: Int, deniedList: List<String>, result: PermissionResult) {
        failCallback[reqCode]?.invoke(PermissionGrantException(deniedList, result))
        clearCallbackOfInvoked(reqCode)
    }

    private fun clearCallbackOfInvoked(reqCode: Int) {
        successCallback.remove(reqCode)
        failCallback.remove(reqCode)
        explanationCallback.remove(reqCode)
    }
}