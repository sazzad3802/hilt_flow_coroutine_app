import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id ("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.shk.hiltfeed"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shk.hiltfeed"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "FEED_BASE_URL", "\"https://hellohasan.com/wp-json/wp/v2/\"")
        buildConfigField("String", "BLOG_BASE_URL", "\"https://dummyjson.com\"")
        buildConfigField("String", "LOCAL_DB", "\"hiltfeed_db\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kapt {
        correctErrorTypes = true
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    val lifecycleVersion = "2.6.2"
    val roomVersion = "2.6.1"
    val hiltVersion = "2.48"
    val workManagerVersion = "2.9.0"
    val retrofitVersion = "2.9.0"
    val recyclerviewVersion = "1.3.1"
    val glideVersion = "4.16.0"

    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.activity:activity-ktx:1.7.2")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //CardView and RecyclerView
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.recyclerview:recyclerview:$recyclerviewVersion")

    // for Retrofit and GSON library
    implementation ("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Glide
    implementation ("com.github.bumptech.glide:glide:$glideVersion")

    // coil
    implementation ("io.coil-kt:coil:2.4.0")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    // livedata
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // fragment
    implementation ("androidx.fragment:fragment-ktx:1.8.5")
    // activity
    implementation ("androidx.activity:activity-ktx:1.9.3")

    // Hilt
    implementation ("com.google.dagger:hilt-android:$hiltVersion")
    kapt ("com.google.dagger:hilt-compiler:$hiltVersion")

    // Hilt integration with androidx features like workManager, room, viewmodel
    implementation ("androidx.hilt:hilt-work:1.1.0")
    kapt ("androidx.hilt:hilt-compiler:1.1.0")

    // room + hilt
    implementation ("androidx.room:room-runtime:$roomVersion")
    kapt ("androidx.room:room-compiler:$roomVersion")
    implementation ("androidx.room:room-ktx:$roomVersion")

    // WorkManager
    implementation ("androidx.work:work-runtime-ktx:$workManagerVersion")







    // compose
    implementation ("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.6.3")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.3")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("androidx.compose.material:material:1.6.3")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.paging:paging-compose:3.3.0")

    implementation ("androidx.compose.material:material:1.6.3")


    implementation ("com.google.accompanist:accompanist-pager:0.28.0")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.28.0")

}