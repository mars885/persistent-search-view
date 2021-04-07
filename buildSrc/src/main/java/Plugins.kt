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

import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec


const val PLUGIN_GRADLE_VERSIONS = "com.github.ben-manes.versions"
const val PLUGIN_ANDROID_APPLICATION = "com.android.application"
const val PLUGIN_ANDROID_LIBRARY = "com.android.library"
const val PLUGIN_KOTLIN_ANDROID = "kotlin-android"
const val PLUGIN_KOTLIN_ANDROID_EXTENSIONS = "kotlin-android-extensions"
const val PLUGIN_MAVEN_PUBLISH = "maven-publish"
const val PLUGIN_SIGNING = "org.gradle.signing"


fun PluginDependenciesSpec.gradleVersions(): PluginDependencySpec {
    return (id(PLUGIN_GRADLE_VERSIONS) version versions.gradleVersionsPlugin)
}


fun PluginDependenciesSpec.androidApplication(): PluginDependencySpec {
    return id(PLUGIN_ANDROID_APPLICATION)
}


fun PluginDependenciesSpec.androidLibrary(): PluginDependencySpec {
    return id(PLUGIN_ANDROID_LIBRARY)
}


fun PluginDependenciesSpec.kotlinAndroid(): PluginDependencySpec {
    return id(PLUGIN_KOTLIN_ANDROID)
}


fun PluginDependenciesSpec.kotlinAndroidExtensions(): PluginDependencySpec {
    return id(PLUGIN_KOTLIN_ANDROID_EXTENSIONS)
}