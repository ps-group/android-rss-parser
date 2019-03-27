# Парсер RSS лент по шагам

В этих примерах описано как по шагам написать свой парсер RSS лент.  
Код написан на языке Kotlin.  
Версия Android API 15+

## Создаём MVP (Minimal Viable Product)

### 1. Разработка макета и создание проекта в Android Studio

В [1-м примере](rss_parser_1) сверстаем простой экран с полем ввода URL ленты
 и кнопкой запроса данных.

### 2. Простые интерфейсы и ввод

Во [2-м примере](rss_parser_2) напишем простые интерфейсы, чтобы обрабатывать
пользовательский ввод и отображать данные

### 3. Сохранение состояния экрана после уничтожения View

В [3-м примере](rss_parser_3) подключим библиотеку ViewModel для сохранение состояния экрана после пересоздания

### 4. Добавление классов модели

В [4-м примере](rss_parser_4) добавим классы основных сущностей - парсера, хранилища данных и API для получения RSS из сети
