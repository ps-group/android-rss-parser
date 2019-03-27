# 4. Добавление классов модели

## Этот пример содержит

1. Создание классов модели - Feed, Article, Repository, Parser, Storage
2. Реализация парсера

### Создание классов модели - Feed, Article, Repository, Parser, Storage

Для получения списка новостей FeedViewModel будет использовать класс Repository, который может общаться с API, посволяющим получить нопости из интернета, парсером, который может преобразовать список байтов к доменной модели, и хранилищем данных, для запоминания новостей, после выключения приложения.

Примерная диаграмма классов:

![Результат](../img/4_diagram.png)

Все классы уже созданы, смотрите примеры кода.

### Внедрение IRepository в FeedViewModel

Теперь, если мы хотим, чтобы FeedViewModel работала с репозиторием, то добавим его аргументом конструктора и запустим приложение:

```kotlin
class FeedViewModel(
    private val repository: IRssRepository
) : ViewModel() { ... }
```

При запуске приложение упадет с такой ошибкой:

* *java.lang.RuntimeException: Unable to start activity {activity name}: java.lang.RuntimeException: Cannot create an instance of class io.github.psgroup.rss.presentation.activity.viewmodel.FeedViewModel*
* *Caused by: java.lang.RuntimeException: Cannot create an instance of class io.github.psgroup.rss.presentation.activity.viewmodel.FeedViewModel*
* *Caused by: java.lang.InstantiationException: java.lang.Class\<io.github.psgroup.rss.presentation.activity.viewmodel.FeedViewModel\> has no zero argument constructor*

Это происходит потому что стандартный провайдер ViewModel пытается вызвать конструктор без аргументов и не находит его. Чтобы избежать ошибки, можно переопределить фабрику ViewModel, которую использует провайдер.

Вынесем код создания FeedViewModel в отдельный класс, назовем его **ViewModelInjector**:

```kotlin
object ViewModelInjector {

    fun getFeedViewModel(activity: FeedActivity): FeedViewModel {
        val provider = ViewModelProvider(activity, Factory)
        return provider[FeedViewModel::class.java]
    }

    private fun newRepository(): IRssRepository = RssRepository(
        api = RssApi(),
        parser = RssParser(),
        storage = RssStorage()
    )

    private object Factory: ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == FeedViewModel::class.java) {
                return FeedViewModel(newRepository()) as T
            }

            throw IllegalArgumentException(
                "ViewModel ${modelClass.canonicalName} not supported")
        }

    }

}
```

Затем заменим код получения FeedViewModel внутри FeedActivity на такой:

```kotlin
private fun initViewModel() {
    mViewModel = ViewModelInjector.getFeedViewModel(this)

    // Подписка на LiveData
}
```

### Полезные материалы

#### [<< Предыдущий пример](../rss_parser_3) / [Следующий пример >>](../rss_parser_5)