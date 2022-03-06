[![Main Commit](https://github.com/android-alatan/LifecycleComponents/actions/workflows/lib-main-branch.yml/badge.svg?branch=main)](https://github.com/android-alatan/LifecycleComponents/actions/workflows/lib-main-branch.yml)
[![Release](https://jitpack.io/v/android-alatan/lifecyclecomponents.svg)](https://jitpack.io/#android-alatan/lifecyclecomponents)
# Lifecycle Components

In Android world, we are getting lots of Design Patterns : MVP, MVVM, MVI and so on.

There are lots of discussions. But there is common implementation : Ping-pong among Activity/Fragment and Logics.

This tools are to focus on Logic at all.

### Installation
There is 2 different ways to declare dependencies

1. app/build.gradle.kts
```kotlin
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-activity:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-impl:$version")
implementation("com.github.android-alatan.lifecyclecomponents:result-handler:$version")
implementation("com.github.android-alatan.lifecyclecomponents:bundle-collector:$version")
implementation("com.github.android-alatan.lifecyclecomponents:request-permission:$version")
implementation("com.github.android-alatan.lifecyclecomponents:back-key-handler:$version")
implementation("com.github.android-alatan.lifecyclecomponents:view-event-viewinteractionstream:$version")
implementation("com.github.android-alatan.lifecyclecomponents:router:$version")
implementation("com.github.android-alatan.lifecyclecomponents:view-event-legacy-impl:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-invokeadapter-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lazy-provider:$version")
implementation("com.github.android-alatan.lifecyclecomponents:coroutine-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-annotations:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-api:$version")

// prerequisite
implementation("com.google.android.material:material:${material_version}")

// below dependencies are optional
implementation("com.github.android-alatan.lifecyclecomponents:dagger-scope:$version")
implementation("com.github.android-alatan.lifecyclecomponents:dagger-base-builder:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-activity-dagger:$version")

implementation("com.github.android-alatan.lifecyclecomponents:bundle-collector-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:bundle-collector-rx-adapter:$version")
implementation("com.github.android-alatan.lifecyclecomponents:bundle-collector-flow-adapter:$version")
testImplementation("com.github.android-alatan.lifecyclecomponents:bundle-collector-assertion:$version")

implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-invokeadapter-rx:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-invokeadapter-flow:$version")
testImplementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-assertion:$version")

implementation("com.github.android-alatan.lifecyclecomponents:back-key-handler-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:back-key-handler-adapter-rx:$version")
implementation("com.github.android-alatan.lifecyclecomponents:back-key-handler-adapter-flow:$version")
testImplementation("com.github.android-alatan.lifecyclecomponents:back-key-handler-assertion:$version")

implementation("com.github.android-alatan.lifecyclecomponents:router-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:router-web-api:$version")
testImplementation("com.github.android-alatan.lifecyclecomponents:router-assertion:$version")

implementation("com.github.android-alatan.lifecyclecomponents:view-event-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:view-event-adapter-rx:$version")
implementation("com.github.android-alatan.lifecyclecomponents:view-event-adapter-flow:$version")
testImplementation("com.github.android-alatan.lifecyclecomponents:view-event-assertion:$version")

implementation("com.github.android-alatan.lifecyclecomponents:result-handler-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:result-handler-rx-adapter:$version")
implementation("com.github.android-alatan.lifecyclecomponents:result-handler-flow-adapter:$version")
testImplementation("com.github.android-alatan.lifecyclecomponents:result-handler-assertion:$version")

implementation("com.github.android-alatan.lifecyclecomponents:request-permission-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:request-permission-flow-handler:$version")
implementation("com.github.android-alatan.lifecyclecomponents:request-permission-rx-handler:$version")
testImplementation("com.github.android-alatan.lifecyclecomponents:request-permission-assertion:$version")

// for compose
implementation("com.github.android-alatan.lifecyclecomponents:composable-lifecycle-listener-activator:$version")
implementation("com.github.android-alatan.lifecyclecomponents:provided-compose-local-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-compose-util:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-compose-activity-dagger:$version")
implementation("com.github.android-alatan.lifecyclecomponents:composable-holder:$version")
implementation("com.github.android-alatan.lifecyclecomponents:provided-compose-local-ksp:$version")
implementation("com.github.android-alatan.lifecyclecomponents:view-event-compose-extension-api:$version")
implementation("com.github.android-alatan.lifecyclecomponents:view-event-compose-extension-impl:$version")
implementation("com.github.android-alatan.lifecyclecomponents:view-event-compose-impl:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-compose-activity:$version")
```
In `app` module, you should declare lots of dependencies.

But there are small dependencies in libs  

2. libs/build.gradle.kts
```kotlin
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-activity:$version")
implementation("com.github.android-alatan.lifecyclecomponents:lifecycle-handler-annotations:$version")

implementation("com.google.android.material:material:${material_version}")
```

### Example of LifecycleComponents
```kotlin
class MyApplication: Application {
  fun onCreate() {
    InvokeAdapterInitializer.initialize(
      factories = listOf(FlowInvokeAdapter.Factory(), RxInvokeAdapter.Factory()),
      errorLogger = { Log.e("InvokeManager", "InvokeManager got error", it) }
    )
  }
}

class MyActivity: LifecycleActivity() {
  fun onCreate() {
      SampleViewModel(this)
  }
}

class SampleViewModel(lifecycleSource: LifecycleSource): LifecycleListener(lifecycleSource) {
    
  @CreatedToDestroy
  fun observeBtnClick(viewInteractionStream: ViewInteractionStream, router: Router): Observable<Long> {
      return viewInteractionStream.asObservable()
          .switchMap { view -> view.find(R.id.btn).onClickAsObservable() }
          .doOnNext {
              router.newJourneyBuilder()
                  .activity(NextActivity::class.java)
                  .build()
                  .visit() // same as startActivity()
                  .end()   // close current activity
          }
  }

  @CreatedToDestroy
  fun observePreviousScreenData(
      bundleCollectorStream: BundleCollectorStream, // getIntent() wrapper
      viewAccessorStream: ViewAccessorStream        // real view accessor
  ): Observable<Long> {
      return bundleCollectorStream.intentData()
          .switchMap { intentData ->
              val name = intentData.getStringOrNull("name")
              viewAccessorStream.asObservable()
                  .doOnNext { it.view<TextView>(R.id.text).setText(name) }
          }
  }
}
```
As you can see, all annotated functions can be injected various types of accessor as stream type.

### Sample
[Lifecycle Handler Sample](https://github.com/android-alatan/LifecycleComponents/tree/main/_lifecycle-handler/_sample/lifecycle-handler-sample)

[Compose Lifecycle Handler Sample](https://github.com/android-alatan/LifecycleComponents/blob/main/_lifecycle-handler/_sample/lifecycle-handler-compose-sample/build.gradle.kts)

### References : WIP