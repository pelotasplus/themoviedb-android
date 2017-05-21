# themoviedb-android

Demo project using The Movie Database API.

- TMDB API key has to be set in [build.gradle](https://github.com/pelotasplus/themoviedb-android/blob/master/mobile/build.gradle#L16) file.
- [unit tests](https://github.com/pelotasplus/themoviedb-android/tree/master/mobile/src/test/groovy/pl/pelotasplus/themoviedb/demo) are using awesome [spock framework](spockframework.org).
- Model-[View](https://github.com/pelotasplus/themoviedb-android/blob/master/mobile/src/main/java/pl/pelotasplus/themoviedb/demo/screens/main/MainActivity.java)-[Presenter](https://github.com/pelotasplus/themoviedb-android/blob/master/mobile/src/main/java/pl/pelotasplus/themoviedb/demo/screens/main/MainPresenter.java) pattern is being used with extra file to describe [contract](https://github.com/pelotasplus/themoviedb-android/blob/master/mobile/src/main/java/pl/pelotasplus/themoviedb/demo/screens/main/MainContract.java)
- [Retrofit](https://github.com/pelotasplus/themoviedb-android/tree/master/mobile/src/main/java/pl/pelotasplus/themoviedb/demo/api) is used to access TMDB API
- model is being bound to views using [data binding](https://github.com/pelotasplus/themoviedb-android/blob/master/mobile/src/main/java/pl/pelotasplus/themoviedb/demo/MovieViewModel.java) with [custom data bindings](https://github.com/pelotasplus/themoviedb-android/blob/master/mobile/src/main/java/pl/pelotasplus/themoviedb/demo/CustomDataBindings.java) to load images
- obviously RxJava, Dagger, AutoValue and other "standard" libraries are being used.
