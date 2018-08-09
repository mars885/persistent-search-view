# PersistentSearchView
Android library designed to simplify the process of implementing search-related functionality.

[ ![Download](https://api.bintray.com/packages/mars885/maven/persistentsearchview/images/download.svg) ](https://bintray.com/mars885/maven/persistentsearchview/_latestVersion)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](http://developer.android.com/index.html)

## Contents

* [Demo](#demo-youtube)
* [Getting Started](#getting-started)
* [Basic Implementation](#basic-implementation)
* [Basic Recent Suggestions Implementation](#basic-recent-suggestions-implementation)
* [Basic Regular Suggestions Implementation](#basic-regular-suggestions-implementation)
* [Advanced Use](#advanced-use)
* [Difference between recent and regular suggestions](#difference-between-recent-and-regular-suggestions)
* [Contribution](#contribution)
* [Hall of Fame](#hall-of-fame)
* [License](#license)

## Demo (YouTube)

<a href="https://www.youtube.com/watch?v=t6AgY0hYJHU">
<img src="https://farm2.staticflickr.com/1834/43887730932_e834c4de7c_h.jpg" width="200" height="356"/>
</a>

## Getting Started

1. Make sure that you've added the `jcenter()` repository to your top-level `build.gradle` file.

````groovy
buildscript {
    //...
    repositories {
        //...
        jcenter()
    }
    //...
}
````

2. Add the library dependency to your module-level `build.gradle` file. 

````groovy
ext {
    //...
    psvVersion = "1.0.0"
}

dependencies {
    //...
    implementation "com.paulrybitskyi.persistentsearchview:persistentsearchview:$psvVersion"
}
````

3. Enable the **jetifier** and **androidX** support in the top-level `gradle.properties` file.

````groovy
//...
android.enableJetifier=true
android.useAndroidX=true
//....
````

4. Update your `compileSdkVersion` in the module-level `build.gradle` file to **28+**.

````groovy
//...
android {
    //...
    compileSdkVersion 28
    //...
}
//...
````

5. Update your `com.android.support.appcompat.*` dependency to the new `androidx.appcompat.*` alternative.

````groovy
//...
dependencies {
    //...
    implementation "androidx.appcompat:appcompat:1.0.0-beta01"
    //...
}
//...
````
6. Proceed with the implementation of your own search view.

## Basic Implementation

Implementation of a PersistentSearchView with basic functionality involves 2 main steps - declaring a widget inside the XML file of your choice and configuring it in one of the Java/Kotlin classes.

Let's implement a PersistentSearchView with basic functionality by following the steps listed above:

1. Declaring a widget inside the XML file.

    <details><summary><b>XML (click to expand)</b></summary>
    <p>

    ````xml
    <?xml version="1.0" encoding="utf-8"?>
    <RelativeLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <!-- Other widgets here -->

        <com.paulrybitskyi.persistentsearchview.PersistentSearchView
            android:id="@+id/persistentSearchView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:paddingTop="4dp"
            android:paddingLeft="4dp"
            android:paddingStart="4dp"
            android:paddingRight="4dp"
            android:paddingEnd="4dp"/>

    </RelativeLayout>
    ````
    </p></details>

2. Configuring the widget in one of the Java/Kotlin classes.

    <details><summary><b>Kotlin (click to expand)</b></summary>
    <p>

    ````kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_layout)

        //...

        with(persistentSearchView) {
            setOnLeftBtnClickListener {
                // Handle the left button click
            }
            setOnClearInputBtnClickListener {
                // Handle the clear input button click
            }

            // Setting a delegate for the voice recognition input
            setVoiceRecognitionDelegate(VoiceRecognitionDelegate(this@DemoActivity))

            setOnSearchConfirmedListener { searchView, query ->
                // Handle a search confirmation. This is the place where you'd
                // want to perform a search against your data provider.
            }

            // Disabling the suggestions since they are unused in
            // the simple implementation
            setSuggestionsDisabled(true)
        }
    }


    //...


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Calling the voice recognition delegate to properly handle voice input results
        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data)
    }
    ````

    </p></details>

    <details><summary><b>Java (click to expand)</b></summary>
    <p>

    ````java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_layout);

        //...

        persistentSearchView.setOnLeftBtnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // Handle the left button click
            }

        });

        persistentSearchView.setOnClearInputBtnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // Handle the clear input button click
            }

        });

        // Setting a delegate for the voice recognition input
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));

        persistentSearchView.setOnSearchConfirmedListener(new OnSearchConfirmedListener() {

            @Override
            public void onSearchConfirmed(PersistentSearchView searchView, String query) {
                // Handle a search confirmation. This is the place where you'd
                // want to perform a search against your data provider.
            }

        });

        // Disabling the suggestions since they are unused in
        // the simple implementation
        persistentSearchView.setSuggestionsDisabled();
    }


    //...


    @Override
    protected fun onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Calling the voice recognition delegate to properly handle voice input results
        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data);
    }
    ````

    </p></details>

## Basic Recent Suggestions Implementation

Implementation of a PersistentSearchView with recent suggestions is pretty much the same as [Basic Implementation](#basic-implementation) with one exception: the view configuration.

In this implementation you'll need to provide a bit more configuration for the widget in order to show recent suggestions to the user, such as providing implementation for a couple of listeners as well as fetching suggestions from your data provider and setting them to the search view.

For example, here is the configuration of the widget with recent suggestions functionality:

<details><summary><b>Java (click to expand)</b></summary>
<p>

````java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.demo_activity_layout);

    //...

    persistentSearchView.setOnLeftBtnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View view) {
            // Handle the left button click
        }

    });

    persistentSearchView.setOnClearInputBtnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View view) {
            // Handle the clear input button click
        }

    });

    // Setting a delegate for the voice recognition input
    persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));

    persistentSearchView.setOnSearchConfirmedListener(new OnSearchConfirmedListener() {

        @Override
        public void onSearchConfirmed(PersistentSearchView searchView, String query) {
            // Handle a search confirmation. This is the place where you'd
            // want to save a new query and perform a search against your
            // data provider.
        }

    });

    persistentSearchView.setOnSearchQueryChangeListener(new OnSearchQueryChangeListener() {
        
        @Override
        public void onSearchQueryChanged(PersistentSearchView searchView, String oldQuery, String newQuery) {
            // Handle a search query change. This is the place where you'd
            // want load new suggestions based on the newQuery parameter.
        }

    });

    persistentSearchView.setOnSuggestionChangeListener(new OnSuggestionChangeListener() {

        @Override
        public void onSuggestionPicked(SuggestionItem suggestion) {
            // Handle a suggestion pick event. This is the place where you'd
            // want to perform a search against your data provider.
        }

        @Override
        public void onSuggestionRemoved(SuggestionItem suggestion) {
            // Handle a suggestion remove event. This is the place where
            // you'd want to remove the suggestion from your data provider.
        }

    });
}
	

//...


@Override
public void onResume() {
    super.onResume();

    List<String> searchQueries = null;

    // Fetching the search queries from the data provider
    if(persistentSearchView.isInputQueryEmpty) {
        searchQueries = mDataProvider.getInitialSearchQueries();
    } else {
        searchQueries = mDataProvider.getSuggestionsForQuery(persistentSearchView.inputQuery);
    }

    // Converting them to recent suggestions and setting them to the widget
    persistentSearchView.setSuggestions(SuggestionCreationUtil.asRecentSearchSuggestions(searchQueries), false);
}

	
//...


@Override
protected fun onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Calling the voice recognition delegate to properly handle voice input results
    VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data);
}
````

</p></details>

<details><summary><b>Kotlin (click to expand)</b></summary>
<p>

````kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.demo_activity_layout)

    //...

    with(persistentSearchView) {
        setOnLeftBtnClickListener {
            // Handle the left button click
        }
        setOnClearInputBtnClickListener {
            // Handle the clear input button click
        }

        // Setting a delegate for the voice recognition input
        setVoiceRecognitionDelegate(VoiceRecognitionDelegate(this@DemoActivity))

        setOnSearchConfirmedListener { searchView, query ->
            // Handle a search confirmation. This is the place where you'd
            // want to save a new query and perform a search against your
            // data provider.
        }

        setOnSearchQueryChangeListener { searchView, oldQuery, newQuery ->
            // Handle a search query change. This is the place where you'd
            // want load new suggestions based on the newQuery parameter.
        }

        setOnSuggestionChangeListener(object : OnSuggestionChangeListener {

            override fun onSuggestionPicked(suggestion: SuggestionItem) {
                // Handle a suggestion pick event. This is the place where you'd
                // want to perform a search against your data provider.
            }

            override fun onSuggestionRemoved(suggestion: SuggestionItem) {
                // Handle a suggestion remove event. This is the place where
                // you'd want to remove the suggestion from your data provider.
            }

        })
    }
}


//...


override fun onResume() {
    super.onResume()

    // Fetching the search queries from the data provider
    val searchQueries = if(persistentSearchView.isInputQueryEmpty) {
        mDataProvider.getInitialSearchQueries()
    } else {
        mDataProvider.getSuggestionsForQuery(persistentSearchView.inputQuery)
    }

    // Converting them to recent suggestions and setting them to the widget
    persistentSearchView.setSuggestions(SuggestionCreationUtil.asRecentSearchSuggestions(searchQueries), false)
}


//...


override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Calling the voice recognition delegate to properly handle voice input results
    VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data)
}
````

</p></details>

## Basic Regular Suggestions Implementation

Implementation of a PersistentSearchView with regular suggestions is identical to the [Basic Recent Suggestions Implementation](#basic-recent-suggestions-implementation) with one exception: suggestions creation method [asRecentSearchSuggestions(searchQueries)](https://github.com/mars885/persistentsearchview/blob/master/persistentsearchview/src/main/java/com/paulrybitskyi/persistentsearchview/utils/SuggestionCreationUtil.java#L47) should be replaced with [asRegularSearchSuggestions(searchQueries)](https://github.com/mars885/persistentsearchview/blob/master/persistentsearchview/src/main/java/com/paulrybitskyi/persistentsearchview/utils/SuggestionCreationUtil.java#L62).

## Advanced Use

See the [Sample app](https://github.com/mars885/persistentsearchview/tree/master/sample).

## Difference between recent and regular suggestions

The difference between recent and regular suggestions is that a user can remove recent suggestions from the list while regular suggestions cannot be removed (there is no remove button on the regular suggestions).

For example, here are screenshots of recent suggestions compared to regular:

<table>
	<tbody>
		<tr>
			<td align="center">Recent</td>
			<td align="center">Regular</td>
		</tr>
		<tr>
			<td align="center">
				<img src="https://farm2.staticflickr.com/1780/43231286544_50d547b9ee_b.jpg" width="341" height="326"/>
			</td>
			<td align="center">
				<img src="https://farm2.staticflickr.com/1815/30081446258_dc4bc1b301_b.jpg" width="341" height="326"/>
			</td>
		</tr>
	</tbody>
</table>

## Contribution

See the [CONTRIBUTING.md](CONTRIBUTING.md) file.

## Hall of Fame

<table>
    <tbody>
        <tr>
            <td align="center">
                <a href="https://play.google.com/store/apps/details?id=com.arthurivanets.owly">
	                <img src="https://lh3.googleusercontent.com/V1-emL7rlp0UTZfJIN-JPpTev1ZSg2RLjC1WAgKVB0A_1ir32JxTBl8E8zz2KyJlyQ=s360" width="70" height="70"/>
                </a>
            </td>
            <td align="center"><b>Owly</b></td>
        </tr>
    </tbody>
</table>

> Using PersistentSearchView in your app and want it to get listed here? Email me at paul.rybitskyi.work@gmail.com!

## License

PersistentSearchView is licensed under the [Apache 2.0 License](LICENSE).
