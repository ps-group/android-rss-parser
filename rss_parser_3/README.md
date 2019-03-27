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

    val addButtonViewState: LiveData<AddButtonViewState>
        get() = mAddButtonViewState
    val articlesViewState: LiveData<ArticlesViewState>
        get() = mArticlesViewState
    val refresherViewState: LiveData<Boolean>
        get() = mRefresherViewState

    private val mAddButtonViewState = MutableLiveData<AddButtonViewState>()
        .apply { value = AddButtonViewState.Hidden }
    private val mArticlesViewState = MutableLiveData<ArticlesViewState>()
        .apply { value = ArticlesViewState.Hidden }
    private val mRefresherViewState = MutableLiveData<Boolean>()
        .apply { value = false }

    fun onAddRss(url: Editable) = Unit
    fun onRefresh() = Unit
    fun onDeleteFeed() = Unit

}
```

**FeedViewModel** позволит абстрагироваться от **жизненного цикла FeedActivity** и будет содержать актуальное состояние экрана, а так же получать от него события пользовательского ввода.

После создания FeedViewModel, напишем код получения экземпляра внутри FeedActivity:

```kotlin
private fun initViewModel() {
    val provider = ViewModelProvider(this, ModelProvider.NewInstanceFactory())
    mViewModel = provider[FeedViewModel::class.java]

    mViewModel.addButtonViewState.observe(this, Observer { teAddButtonView(it) })
    mViewModel.articlesViewState.observe(this, Observer { updateArticlesView })
    mViewModel.refresherViewState.observe(this, Observer { teRefresherView(it) })
}

private fun updateAddButtonView(viewState: AddButtonViewState) {
    when (viewState) {
        AddButtonViewState.Hidden -> {
            addRssButtonBackground.visibility = View.GONE
        }
        AddButtonViewState.Locked -> {
            addRssButtonBackground.visibility = View.VISIBLE
            addRssButton.isEnabled = false
        }
        AddButtonViewState.ShowButton -> {
            addRssButtonBackground.visibility = View.VISIBLE
            addRssButton.isEnabled = true
        }
    }
}

private fun updateArticlesView(viewState: ArticlesViewState) {
    when (viewState) {
        ArticlesViewState.Hidden -> {
            newsList.visibility = View.GONE
            emptyListText.visibility = View.GONE
        }
        ArticlesViewState.Empty -> {
            newsList.visibility = View.GONE
            emptyListText.visibility = View.VISIBLE
        }
        is ArticlesViewState.ShowArticles -> {
            newsList.visibility = View.VISIBLE
            emptyListText.visibility = View.GONE
        }
    }
}

private fun updateRefresherView(viewState: Boolean) {
    listRefresher.isRefreshing = viewState
}
```

Как видно из примера, мы подписались на обновления значений в полях **FeedViewModel** и вызываем методы внутри **FeedActivity**, когда происходит уведомление.

Чтобы при добавлении нового ViewState оператор **when** не компилировался, необходимо сделать его результат не игнорируемым. Есть несколько способов это сделать, например, можно присваивать результат функции или вызывать у результата свойство / функцию.

Добавим такое свойство - расширение и будем вызывать его у всех when-операторов, где может ожидаться добавление случаев, чтобы предотвратить ошибку на уровне компиляции.

```kotlin
val <T> T.exhaustive: T
    get() = this

/*
* Пример: при добавлении нового AddButtonViewState, произойдет ошибка компиляции
* */
private fun updateAddButtonView(viewState: AddButtonViewState) {
    when (viewState) {
        AddButtonViewState.Hidden -> {
            addRssButtonBackground.visibility = View.GONE
        }
        AddButtonViewState.Locked -> {
            addRssButtonBackground.visibility = View.VISIBLE
            addRssButton.isEnabled = false
        }
        AddButtonViewState.ShowButton -> {
            addRssButtonBackground.visibility = View.VISIBLE
            addRssButton.isEnabled = true
        }
    }.exhaustive
}
```

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