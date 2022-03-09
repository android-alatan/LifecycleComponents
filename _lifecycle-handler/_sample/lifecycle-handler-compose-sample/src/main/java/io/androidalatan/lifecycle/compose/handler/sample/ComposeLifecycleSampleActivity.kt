package io.androidalatan.lifecycle.compose.handler.sample

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import io.androidalatan.component.view.compose.api.toolbar.onToolbarMenuItemClick
import io.androidalatan.component.view.compose.api.view.onClick
import io.androidalatan.component.view.compose.api.view.viewSizeEvent
import io.androidalatan.compose.local.ksp.api.ProvidedComposeLocal
import io.androidalatan.lifecycle.handler.compose.activity.ComposeLifecycleActivity
import io.androidalatan.lifecycle.handler.compose.activity.localowners.LocalComposeEventTriggerOwner

class ComposeLifecycleSampleActivity : ComposeLifecycleActivity() {

    @ProvidedComposeLocal
    val viewModel = ComposeSampleViewModel(this)

    override val activateAllListenersWhenInit: Boolean = true

    @SuppressLint("ComposableNaming")
    @Composable
    override fun contentView() {
        ComposeLocalProvider {
            Column {
                TopLayout()
                HelloWorld1(viewModel())
            }
        }
    }

    companion object {
        private const val TAG = "ComposeLifecycleSampleA"
    }

}

@Composable
fun TopLayout() {
    val composeViewInteractionTrigger = LocalComposeEventTriggerOwner.current
    TopAppBar(
        title = { Text("Compose Lifecycle Sample App") },
        actions = {
            IconButton(composeViewInteractionTrigger.onToolbarMenuItemClick(R.id.top_app_bar, R.id.share)) {
                Icon(Icons.Default.Share, contentDescription = "share")
            }
        }
    )
}

@Composable
fun HelloWorld1(composeSampleViewModel: ComposeSampleViewModel = viewModel()) {
    val composeViewInteractionTrigger = LocalComposeEventTriggerOwner.current
    Text("Hello World1~!")
    Button(onClick = composeViewInteractionTrigger.onClick(R.id.button1),
           modifier = Modifier.onGloballyPositioned { coordinates ->
               composeViewInteractionTrigger.viewSizeEvent(R.id.button1, coordinates.size.width, coordinates.size.height)
           }) {
        Text("Click Here1")
    }
    val clickedCount: Int? by composeSampleViewModel.clickedCount.observeAsState()
    Text("Clicked Count : $clickedCount")

    Button(onClick = composeViewInteractionTrigger.onClick(R.id.button2)) {
        Text("Flow button click Here 1")
    }
}