# Streaming-Crypto-Analysis

## Data Descryption
We will get streaming data with Binance API on Stream WebSocket.

Basic endpoint for API:

```wss://stream.binance.com:9443 или wss://stream.binance.com:443```

Input data is a cryptocurrency stock quotes.

Input data (for example): 
* price
* time
* cryptocurrency name

Detailed description from API:

Stream Name: <symbol>@kline_<interval>

Update Speed: 1000ms for 1s, 2000ms for the other intervals

Payload:

```
{
  "e": "kline",         // Event type
  "E": 1672515782136,   // Event time
  "s": "BNBBTC",        // Symbol
  "k": {
    "t": 1672515780000, // Kline start time
    "T": 1672515839999, // Kline close time
    "s": "BNBBTC",      // Symbol
    "i": "1m",          // Interval
    "f": 100,           // First trade ID
    "L": 200,           // Last trade ID
    "o": "0.0010",      // Open price
    "c": "0.0020",      // Close price
    "h": "0.0025",      // High price
    "l": "0.0015",      // Low price
    "v": "1000",        // Base asset volume
    "n": 100,           // Number of trades
    "x": false,         // Is this kline closed?
    "q": "1.0000",      // Quote asset volume
    "V": "500",         // Taker buy base asset volume
    "Q": "0.500",       // Taker buy quote asset volume
    "B": "123456"       // Ignore
  }
```

Output data is a moving average using the window function of a specific cryptocurrency. This will allow you to see the trends of price changes in recent minutes, which is useful for predictive analytics.

## System Relibiality
For a streaming system with financial data, it is extremely important to avoid data loss or duplication during processing. We can use Exactly-once delivery to ensure that all data is received exactly once without duplicates.

## System Latency

* Low latency

For a real-time data processing streaming system, the delay should be minimal (less than a second). This is necessary to make decisions based on the most up-to-date data from the Binance exchange.

## Описание данных
Мы будем получать данных при помощи Binace API, работающем на Stream Websocket.

Базовая конечная точка для API:

```wss://stream.binance.com:9443 or wss://stream.binance.com:443```

Входные данные это котировки криптовалюты.

Входные данные (для примера):
* цена
* время
* имя криптовалюты

Более подробное описание из API:

Stream Name: <symbol>@kline_<interval>

Update Speed: 1000ms for 1s, 2000ms for the other intervals

Payload:

```
{
  "e": "kline",         // Event type
  "E": 1672515782136,   // Event time
  "s": "BNBBTC",        // Symbol
  "k": {
    "t": 1672515780000, // Kline start time
    "T": 1672515839999, // Kline close time
    "s": "BNBBTC",      // Symbol
    "i": "1m",          // Interval
    "f": 100,           // First trade ID
    "L": 200,           // Last trade ID
    "o": "0.0010",      // Open price
    "c": "0.0020",      // Close price
    "h": "0.0025",      // High price
    "l": "0.0015",      // Low price
    "v": "1000",        // Base asset volume
    "n": 100,           // Number of trades
    "x": false,         // Is this kline closed?
    "q": "1.0000",      // Quote asset volume
    "V": "500",         // Taker buy base asset volume
    "Q": "0.500",       // Taker buy quote asset volume
    "B": "123456"       // Ignore
  }
```

Выходные данные - скользящее среднее, вычисленное с помощью оконных функций по конкретной криптовалюте. Это позволит нам увидеть тренды в изменении цен за последние минуты, которые полезны для предиктивной аналитики.
## Надежность системы
Для потоковой системы с финансовыми данными очень важно избегать потери данных или дубликатов при обработке. Мы можем использовать доставку Exactly-once, что означает, что все данные были получены один раз без дубликатов.
## Задержка системы
* Низкая задержка

Для потоковой системы обработки данных в реальном времени задержка должна быть минимальной (менее секунды). Это необходимо для принятия решений на основе самых актуальных данных с биржи Binance.
## Ответы на вопросы
1.	Какие данные для этой области являются потоковыми?

Данные о котировках криптовалют.

2.	Какие результаты мы хотим получить в результате обработки?

Скользящее среднее цены криптовалюты (можно использовать в предсказательной аналитике).

3.	Как в процессе обработки можно задействовать машинное обучение?

Машинное обучение можно задействовать например для построения линии тренда в данных цены криптовалюты.

4.	Как предметная область относится к запаздыванию обработки? Насколько это критично?

Задержка должна быть низкой (в идеале менее секунды) поскольку оперативное получение данных важно для корректного результата.

5.	Как предметная область относится к потере данных? Насколько это критично? Какую семантику (не менее одного раза, не более одного раза, ровно один раз) следует выбрать?

Небольшие потери данных возможны (поскольку для построения скользящего среднего используется оконная функция, то незначительные потери не окажут значимого влияния на результат).

Что касается семантики, то стоит выбрать ровно один раз, что означает, что данные получены один раз без дубликатов.



## Обзор приложения

Для разработки нашего клиент-серверного приложения использовался SpringBoot, WebSocket.

Для хранения данных использовался брокер сообщений Kafka

Для потоковой обработки поступающих данных из Kafka использовался Flink.

Схема работы нашего приложения следующая:

1) Данные котировок криптовалюты с Binance при помощи протокола Websocket и с использованием Binance API поступают в реальном времени
   в топик Kafka crypto_kline_data.
2) Данные из Kafka поступают в систему потоковой обработки на Flink, где проводится усреднение цены криптовалюты с помощью плавающего окна. Окно
   срабатывает каждые три секунды, новое окно появляется каждые 0.5 секунды (некий сдвиг окон во времени)
3) Обработанные данные из Flink поступают в новый топик Kafka crypto_data_aggregated (агрегированные данные)
4) Далее агрегированные данные из Kafka поступают на разработанный нами Websocket-сервер, к которому подключаются клиенты
   и в браузере можно видеть в реальном времени график изменения котировок криптовалюты (агрегированные). Эти данные можно
   в последующем использовать для систем прогнозирования в машинном обучении.

Для реализации описанной схемы работы были разработаны модули:
1) Модуль обработки начальных данных- класс WebSocketClientCaller. Его задача стягивать начальные данные с Binance и заносить
   нужные нам данные в топик Kafka
2) Модуль потоковой обработки данных FlinkModule. В данном модуле мы определяем схемы наших данных для сериализации в Kafka и десериализации из Kafka.
   Flink в реальном времени обрабатывает поступающие начальные данные из Kafka, производит с ними операции (агрегирования, усреднения) и выдает результат, который записывается в новый топик Kafka.
3) Модуль ClientServerApp отвечает за сбор агрегированных данных из топика Kafka с использованием WebSocket сервера (серверная часть) и
   отображение графика изменения агрегированных котировок криптовалюты за определенный период (например две минуты) в браузере (клиентская часть)


## Как запустить приложение?

Весь проект скомпилирован на JDK-17 в том числе Gradle. Проверьте настройки перед запуском классов проекта

1) Перейдите в папку ProducerPython/pythonKafka/redis-docker и выполните команду ```docker-compose build``` и ```docker-compose up -d``` для запуска redis
2) Перейдите в папку ProducerPython/pythonKafka/kafka-docker и выполните команду ```docker-compose build``` и ```docker-compose up -d``` для запуска kafka
3) Запустите класс kafkaProducer.py для получения данных с Binance в реальном времени
4) Перейдите в главный модуль, выполните в Gradle shadowJar. В папке модуля build/libs сформируется JAR файл
5) Установите Flink на свою локальную машину, настройте конфигурацию кластера Flink согласно официальной документации.
   После настройки кластера выполните команду: ```./bin/start-cluster.sh```, находясь в папке с установленным Flink.
   В кластере сделайте submit job и поместите туда сформированный на шаге 4 Jar файл. Теперь Flink обрабатывает данные, поступающие ему из топика Kafka в реальном времени
   Для остановки кластера выполните команду: ```./bin/stop-cluster.sh``` (Не останавливайте кластер до пункта 6, поскольку иначе вы не увидете формирующийся график в фронт части приложения, поскольку WebSocketServer не будет реагировать на новые данные в Kafka, поступающие после обработки Flink)
6) Перейдите в модуль ClientServerApp и запустите класс MainApplication. Затем перейдите по адресу http://localhost:8084/chart. Вы увидите график изменения агрегированных котировок криптовалюты (агрегация под будущее скользящее среднее) в реальном времени.
   Со временем поступления новых данных график будет обновляться и сдвигаться.
   Конфигурация Spring Server может быть изменена в модуле ClientServerApp в папке resources/application.properties


Пример работы приложения:

![graphs_price.png](assets%2Fgraphs_price.png)

Пример работы Flink:

![Flink work.png](assets%2FFlink%20work.png)