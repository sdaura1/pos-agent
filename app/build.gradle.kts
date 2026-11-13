plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// Override with `-PAUTH_BASE_URL=https://auth.example.com/` or in gradle.properties.
val authBaseUrl: String = providers.gradleProperty("AUTH_BASE_URL")
    .getOrElse("https://api.example.com/")

android {
    namespace = "com.example.pos_agent"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.pos_agent"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "AUTH_BASE_URL", "\"$authBaseUrl\"")
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            // Third-party jars can carry duplicate license/dependency metadata.
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
            )
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // ISO 8583 message packing/unpacking for the NIBSS boundary.
    implementation(libs.jpos) {
        // jPOS bundles its Q2 server, CLI, SSH, BDB and PGP modules. Android
        // only needs the ISO 8583 core, so drop the server-only transitives.
        exclude(group = "org.jline", module = "jline")
        exclude(group = "org.apache-extras.beanshell", module = "bsh")
        exclude(group = "commons-cli", module = "commons-cli")
        exclude(group = "org.bouncycastle", module = "bcprov-lts8on")
        exclude(group = "org.bouncycastle", module = "bcpg-lts8on")
        exclude(group = "org.hdrhistogram", module = "HdrHistogram")
        exclude(group = "org.osgi", module = "org.osgi.core")
        exclude(group = "org.yaml", module = "snakeyaml")
        exclude(group = "jdbm", module = "jdbm")
        exclude(group = "com.sleepycat", module = "je")
        exclude(group = "org.apache.sshd", module = "sshd-core")
        exclude(group = "net.i2p.crypto", module = "eddsa")
    }

    // HTTP transport for the NIBSS REST adapter.
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging.interceptor)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}