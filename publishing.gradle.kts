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

apply(plugin = PLUGIN_MAVEN_PUBLISH)
apply(plugin = PLUGIN_SIGNING)

project.group = publishingConfig.artifactGroupId
project.version = publishingConfig.artifactVersion

afterEvaluate {
    configure<PublishingExtension> {
        publications {
            create<MavenPublication>(publishingConfig.mavenPublicationName) {
                groupId = publishingConfig.artifactGroupId
                artifactId = publishingConfig.artifactName
                version = publishingConfig.artifactVersion

                if(project.plugins.hasPlugin(PLUGIN_ANDROID_LIBRARY)) {
                    from(components["release"])
                } else {
                    from(components["java"])
                }

                pom {
                    name.set(publishingConfig.artifactName)
                    description.set(publishingConfig.artifactDescription)
                    url.set(publishingConfig.artifactWebsite)

                    licenses {
                        license {
                            name.set(publishingConfig.licenseName)
                            url.set(publishingConfig.licenseUrl)
                        }
                    }

                    developers {
                        developer {
                            id.set(publishingConfig.developerId)
                            name.set(publishingConfig.developerName)
                            email.set(publishingConfig.developerEmail)
                        }
                    }

                    scm {
                        connection.set(publishingConfig.gitUrl)
                        developerConnection.set(publishingConfig.gitUrl)
                        url.set(publishingConfig.siteUrl)
                    }
                }
            }
        }

        repositories {
            maven {
                name = publishingConfig.hostRepoName
                url = uri(publishingConfig.hostRepoUrl)

                credentials {
                    username = property("SONATYPE_NEXUS_USERNAME", System.getenv("SONATYPE_NEXUS_USERNAME"))
                    password = property("SONATYPE_NEXUS_PASSWORD", System.getenv("SONATYPE_NEXUS_PASSWORD"))
                }
            }
        }
    }

    configure<SigningExtension> {
        val pubExt = checkNotNull(extensions.findByType(PublishingExtension::class.java))
        val publication = pubExt.publications[publishingConfig.mavenPublicationName]

        sign(publication)
    }
}
