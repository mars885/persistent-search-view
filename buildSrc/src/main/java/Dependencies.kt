/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

@file:Suppress("ClassName")

import org.gradle.api.JavaVersion


object appConfig {

    const val compileSdkVersion = 32
    const val targetSdkVersion = 32
    const val minSdkVersion = 21
    const val applicationId = "com.paulrybitskyi.persistentsearchview.sample"

    val javaCompatibilityVersion = JavaVersion.VERSION_1_8
    val kotlinCompatibilityVersion = JavaVersion.VERSION_1_8
}


object publishingConfig {

    const val artifactGroupId = "com.paulrybitskyi.persistentsearchview"
    const val artifactWebsite = "https://github.com/mars885/persistent-search-view"

    const val mavenPublicationName = "release"

    const val licenseName = "The Apache Software License, Version 2.0"
    const val licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    const val developerId = "mars885"
    const val developerName = "Paul Rybitskyi"
    const val developerEmail = "paul.rybitskyi.work@gmail.com"
    const val siteUrl = "https://github.com/mars885/persistent-search-view"
    const val gitUrl = "https://github.com/mars885/persistent-search-view.git"

    const val hostRepoName = "sonatype"
    const val hostRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"

    const val artifactName = "persistentsearchview"
    const val artifactVersion = "1.1.4"
    const val artifactDescription = "An android library designed to simplify the process of implementing search-related functionality."
}


object versions {

    const val kotlin = "2.0.0" // also in buildSrc build.gradle.kts file
    const val androidPlugin = "8.3.1" // also in buildSrc build.gradle.kts file
    const val gradleVersionsPlugin = "0.51.0"
    const val appCompat = "1.4.2"
    const val cardView = "1.0.0"
    const val browser = "1.4.0"
    const val recyclerView = "1.2.1"
    const val adapster = "1.0.13"
    const val annotations = "1.4.0"
    const val coreKtx = "1.8.0"
    const val commonsKtx = "1.0.4"
    const val jUnit = "4.13.2"
    const val testRunner = "1.3.0"
}


object deps {

    object plugins {

        const val androidGradle = "com.android.tools.build:gradle:${versions.androidPlugin}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
        const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${versions.gradleVersionsPlugin}"

    }

    object local {
        const val persistentSearchView = ":persistentsearchview"
    }

    const val appCompat = "androidx.appcompat:appcompat:${versions.appCompat}"
    const val cardView = "androidx.cardview:cardview:${versions.cardView}"
    const val browser = "androidx.browser:browser:${versions.browser}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${versions.recyclerView}"
    const val adapster = "com.github.arthur3486.adapster:adapster:${versions.adapster}"
    const val annotations = "androidx.annotation:annotation:${versions.annotations}"
    const val coreKtx = "androidx.core:core-ktx:${versions.coreKtx}"
    const val commonsKtx = "com.paulrybitskyi.commons:commons-ktx:${versions.commonsKtx}"
    const val jUnit = "junit:junit:${versions.jUnit}"
    const val testRunner = "androidx.test:runner:${versions.testRunner}"
}