/*
 * Copyright 2017 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.persistentsearchview.sample.utils

import com.paulrybitskyi.persistentsearchview.sample.R
import com.paulrybitskyi.persistentsearchview.sample.model.User
import com.paulrybitskyi.persistentsearchview.sample.utils.extensions.random
import java.io.Serializable
import java.util.*

internal class DataProvider : Serializable {


    private val PROFILE_IMAGE_RESOURCE_IDS = listOf(
        R.drawable.ic_astronaut,    R.drawable.ic_businessman,
        R.drawable.ic_captain,      R.drawable.ic_cashier,
        R.drawable.ic_concierge,    R.drawable.ic_cooker,
        R.drawable.ic_courier,      R.drawable.ic_croupier,
        R.drawable.ic_detective,    R.drawable.ic_disc_jockey,
        R.drawable.ic_diver,        R.drawable.ic_doctor,
        R.drawable.ic_farmer,       R.drawable.ic_gentleman,
        R.drawable.ic_journalist,   R.drawable.ic_loader,
        R.drawable.ic_maid,         R.drawable.ic_manager,
        R.drawable.ic_miner,        R.drawable.ic_motorcyclist,
        R.drawable.ic_pilot,        R.drawable.ic_postman,
        R.drawable.ic_scientist,    R.drawable.ic_showman,
        R.drawable.ic_stewardess,   R.drawable.ic_taxi_driver,
        R.drawable.ic_teacher,      R.drawable.ic_waiter,
        R.drawable.ic_worker,       R.drawable.ic_writer
    )


    private var initialSearchQueries: MutableList<String> = mutableListOf(
        "dinosoaring",
        "liquidathor",
        "cobrawl",
        "advicewalker",
        "coachsoul",
        "lightqueen",
        "messymosquito",
        "coldox",
        "froghurt",
        "rangerman"
    )


    fun generateUsers(query: String, count: Int): List<User> {
        val random = Random()

        return MutableList(count) {
            User(it,
                "@$query$it",
                "${query.capitalize()} ${query.capitalize()}",
                (PROFILE_IMAGE_RESOURCE_IDS.random(random) ?: -1),
                random.nextBoolean(),
                random.nextBoolean()
            )
        }
    }


    fun getInitialSearchQueries(): List<String> {
        return initialSearchQueries
    }


    fun getSuggestionsForQuery(query: String): List<String> {
        val pickedSuggestions = mutableListOf<String>()

        if(query.isEmpty()) {
            pickedSuggestions.addAll(initialSearchQueries)
        } else {
            initialSearchQueries.forEach {
                if(it.toLowerCase().startsWith(query.toLowerCase())) {
                    pickedSuggestions.add(it)
                }
            }
        }

        return pickedSuggestions
    }


    fun saveSearchQuery(searchQuery: String) {
        with(initialSearchQueries) {
            remove(searchQuery)
            add(0, searchQuery)
        }
    }


    fun removeSearchQuery(searchQuery: String) {
        initialSearchQueries.remove(searchQuery)
    }


}