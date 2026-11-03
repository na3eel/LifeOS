plugins {
 id("com.android.application")
 id("org.jetbrains.kotlin.android")
 id("org.jetbrains.kotlin.plugin.compose")
 id("com.google.devtools.ksp")
}
android {
 namespace="com.lifeos.android"; compileSdk=36
 defaultConfig { applicationId="com.lifeos.android"; minSdk=26; targetSdk=36; versionCode=2; versionName="0.2.0"; testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner" }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget="17" }
 buildFeatures { compose=true }
}
dependencies {
 // Keep Lumen and app dependencies aligned to the Compose version supported by AGP 8.10.1 / SDK 36.
 val bom=enforcedPlatform("androidx.compose:compose-bom:2025.08.00")
 implementation(bom)
 implementation("androidx.activity:activity-compose:1.10.1")
 implementation("androidx.compose.ui:ui")
 implementation("androidx.compose.ui:ui-tooling-preview")
 implementation("androidx.compose.material3:material3")
 implementation("com.santi020k:lumen-compose:4.0.1")
 implementation("androidx.compose.material:material-icons-extended")
 implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.2")
 implementation("androidx.room:room-runtime:2.7.2")
 implementation("androidx.room:room-ktx:2.7.2")
 androidTestImplementation("androidx.room:room-testing:2.7.2")
 androidTestImplementation("androidx.test:runner:1.6.2")
 androidTestImplementation("androidx.test.ext:junit:1.2.1")
 androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
 ksp("androidx.room:room-compiler:2.7.2")
 debugImplementation("androidx.compose.ui:ui-tooling")
}