## 1. Создание первого экрана

Этот этап содержит:

1. Создание проекта в Android Studio
2. Верстка простого интерфейса
3. Обработка события по нажатию на кнопку

Создадим проект выбрав шаблон EmptyActivity. Код операции будет выглядеть примерно так:

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
```

Изменим верстку лейаута так, чтобы он содержал текстовое поле, кнопку и элемент отображающий текст.  

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textRssXml"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:textSize="30sp"
        app:layout_constraintBottom_toTopOf="@+id/constraintLayout"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <android.support.constraint.ConstraintLayout
        android:id="@+id/constraintLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

        <EditText
            android:id="@+id/textInputRssUrl"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:inputType="textUri"
            app:layout_constraintBottom_toBottomOf="@+id/btnFetchRss"
            app:layout_constraintEnd_toStartOf="@+id/btnFetchRss"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="@+id/btnFetchRss" />

        <Button
            android:id="@+id/btnFetchRss"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Fetch"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

    </android.support.constraint.ConstraintLayout>

</android.support.constraint.ConstraintLayout>
```

Теперь подпишемся на нажатие кнопки, и сделаем отображение введенных в текстовое поле данных.  

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var mButton: Button
    private lateinit var mUrlInput: EditText
    private lateinit var mTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButton = findViewById(R.id.btnFetchRss)
        mUrlInput = findViewById(R.id.textInputRssUrl);
        mTextView = findViewById(R.id.textRssXml)

        mButton.setOnClickListener {
            mTextView.text = mUrlInput.text.toString()
        }
    }

}
```

В примере используется стандартный для операции метод findViewById<T>(), чтобы обратиться к элементу UI. Такой подход
не безопасен, потому что нельзя во время компиляции опеределить сможет ли метод найти view по идентификатору.  
Подключим gradle-плагин [kotlin-android-extensions](https://kotlinlang.org/docs/tutorials/android-plugin.html) в build.gradle модуля app:

```groovy
apply plugin: 'kotlin-android-extensions'
```

Теперь можно обрааться к UI элементам без поиска через функцию findViewById<T>(). Если элемент будет убран из лейаута
или у него поменятся id, проект не пересоберется без исзменения кода операции.

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFetchRss.setOnClickListener {
            textRssXml.text = textInputRssUrl.text.toString()
        }
    }

}
```

Посмотрим что получилось. После ввода текста и нажатия на кнопку, введенный текст появляется в TextView, которое
находится над полем ввода. Если спровоцировать смену ориентации у устройства, то мы увидим, что данные теряются.

![Скриншот](../img/1_vertical.png)
![Скриншот](../img/1_horizontal.png)

### Полезные материалы:
