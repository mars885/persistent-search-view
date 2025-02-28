/*
 * Copyright 2021 Paul Rybitskyi, oss@paulrybitskyi.com
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

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    gradleVersions()
}

buildscript {
    repositories {
        mavenCentral()
        google()

        // FatAar plugin
        maven {
            setUrl("https://jitpack.io")
            content {
                includeGroup("com.github.aasitnikov")
            }
        }
    }

    dependencies {
        classpath(deps.plugins.androidGradle)
        classpath(deps.plugins.kotlinGradle)
        classpath(deps.plugins.fatAar)
        classpath(deps.plugins.gradleVersions)
    }
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        listOf("alpha", "beta", "rc").any { keyword ->
            candidate.version.lowercase().contains(keyword)
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

val clean by tasks.registering(Delete::class) {
    delete(layout.buildDirectory)
}
