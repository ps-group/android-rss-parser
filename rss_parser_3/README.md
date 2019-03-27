# 3. Сохранение состояния View

## Этот пример содержит

1. Подключение библиотеки ViewModel для отделения кода модели от жизненного цикла Activity

### Переход на библиотеки AndroidX

До версии Android SDK 28 (включительно) обратную совместимость поддерживала библиотека Android Support Library. Версия 28 библиотеки ASL была переименовали в AndroidX. Начиная с версии 29, Android Support Library обновляться не будет, необходимо начать использовать библиотеку AndroidX. По-этому изменим наши зависимости так, чтобы начать использовать AndroidX.

Нажмите по проекту app правой кнопкой мыши и выберите **Refactor > Migrate to AndroidX...**. Миграция должна пройти безболезненно.

После этого вы можете посмотреть, как изменились зависимости в файле build.gradle, а так же подключаемые пакеты в файлах Activity. В большинстве случаев сменилось только название пакета.

### Подключение ViewModel

Создадим класс FeedViewModel, который будет унаследован от **androidx.lifecycle.ViewModel**:

```kotlin
class FeedViewModel : ViewModel() {

    val getAddButtonViewState: LiveData<Boolean>
        get() = mIsFeedExists
    val getRefresherViewState: LiveData<Boolean>
        get() = mIsRefreshing
    val articles: LiveData<List<Any>>
        get() = mArticles

    private val mIsFeedExists = MutableLiveData<Boolean>()
    private val mArticles = MutableLiveData<List<Any>>()
    private val mIsRefreshing = MutableLiveData<Boolean>()

    init {
        mIsFeedExists.value = false
        mArticles.value = listOf()
        mIsRefreshing.value = false
    }

    fun onAddRss(url: Editable) = Unit
    fun onRefresh() = Unit
    fun onDeleteFeed() = Unit

}
```

**FeedViewModel** позволит абстрагироваться от **жизненного цикла FeedActivity** и будет содержать актуальное состояние экрана, а так же получать от него события пользовательского ввода.

После создания FeedViewModel, напишем код получения экземпляра внутри FeedActivity:

```kotlin
private fun initViewModel() {
    val provider = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
    mViewModel = provider[FeedViewModel::class.java]

    mViewModel.articles.observe(this, Observer { updateArticles(it) })
    mViewModel.getAddButtonViewState.observe(this, Observer { updateFeed(it) })
    mViewModel.getRefresherViewState.observe(this, Observer { updateRefresher(it) })
}

private fun updateFeed(getAddButtonViewState: Boolean) {
    if (getAddButtonViewState) {
        addRssButtonBackground.visibility = View.GONE
    } else {
        addRssButtonBackground.visibility = View.VISIBLE
    }
}

private fun updateArticles(articles: List<Any>) {
    val isArticlesExists = articles.isNotEmpty()
    if (isArticlesExists) {
        newsList.visibility = View.VISIBLE
        emptyListText.visibility = View.GONE
    } else {
        newsList.visibility = View.GONE
        emptyListText.visibility = View.VISIBLE
    }
}

private fun updateRefresher(getRefresherViewState: Boolean) {
    listRefresher.getRefresherViewState = getRefresherViewState
}
```

Как видно из примера, мы подписались на обновления значений в полях **FeedViewModel** и вызываем методы внутри **FeedActivity**, когда происходит уведомление.

Теперь можно добавить перенаправление событий пользовательского ввода в **FeedViewModel**:

```kotlin
private fun initRefresher() {
    listRefresher.setOnRefreshListener {
        mViewModel.onRefresh()
    }
}

private fun openUrlDialog() {
    val view = layoutInflater.inflate(R.layout.dialog_input_field, null, e)
    mUrlDialog = AlertDialog.Builder(this)
            .setTitle("Введите ссылку на RSS")
            .setPositiveButton("Добавить") { dialog, buttonId ->
                mViewModel.onAddRss(view.rssUrl.text)
            }
            .setNegativeButton("Отмена") { dialog, buttonId -> }
            .setView(R.layout.dialog_input_field)
            .show()
}
```

В коде FeedViewModel добавлена примерная логика обновления и получения списка новостей. Попробуйте собрать APK с кодом из примера, запустить и добавить RSS, а потом обновить свайпом вниз.

### Полезные материалы

#### [<< Предыдущий пример](../rss_parser_2) / [Следующий пример >>](../rss_parser_4)