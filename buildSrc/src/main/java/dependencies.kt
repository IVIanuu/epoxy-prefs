@file:Suppress("ClassName", "unused")

object Build {
    const val applicationId = "com.ivianuu.epoxyprefs.sample"
    const val buildToolsVersion = "28.0.3"

    const val compileSdk = 28
    const val minSdk = 17
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "0.0.1"
}

object Publishing {
    const val groupId = "com.ivianuu.epoxyprefs"
    const val vcsUrl = "https://github.com/IVIanuu/epoxy-prefs"
    const val version = "${Build.versionName}-dev-17"
}

object Versions {
    const val androidGradlePlugin = "3.4.0"
    const val androidxAppCompat = "1.1.0-alpha04"
    const val bintray = "1.8.4"
    const val epoxy = "3.4.2"
    const val kotlin = "1.3.40"
    const val materialComponents = "1.1.0-alpha07"
    const val materialDialogs = "2.0.3"
    const val mavenGradle = "2.1"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.androidxAppCompat}"

    const val bintrayGradlePlugin =
        "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}"

    const val epoxy = "com.airbnb.android:epoxy:${Versions.epoxy}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val materialComponents =
        "com.google.android.material:material:${Versions.materialComponents}"

    const val materialDialogs = "com.afollestad.material-dialogs:core:${Versions.materialDialogs}"
    const val materialDialogsInput =
        "com.afollestad.material-dialogs:input:${Versions.materialDialogs}"

    const val mavenGradlePlugin =
        "com.github.dcendents:android-maven-gradle-plugin:${Versions.mavenGradle}"
}