# Streaming-Crypto-Analysis

## Data Descryption
We will get streaming data with Binance API on WebSocket.

Input data is a cryptocurrency stock quotes.

Input - price, time, cryptocurrency name (for example)

Output data is a moving average using the window function of a specific cryptocurrency. This will allow you to see the trends of price changes in recent minutes, which is useful for predictive analytics.

## System Relibiality
For a streaming system with financial data, it is extremely important to avoid data loss or duplication during processing. We can use Exactly-once delivery to ensure that all data is received exactly once without duplicates.

## System Latency

* Low latency

For a real-time data processing streaming system, the delay should be minimal (less than a second). This is necessary to make decisions based on the most up-to-date data from the Binance exchange.

## Описание данных
Мы будем получать данных при помощи Binace API, работающем на Websocket.

Входные данные это котировки криптовалюты.

Входные данные - цена, время, имя криптовалюты (к примеру)

Выходные данные - скользящее среднее, вычисленное с помощью оконных функций по конкретной криптовалюте. Это позволит нам увидеть тренды в изменении цен за последние минуты, которые полезны для предиктивной аналитики.
## Надежность системы
Для потоковой системы с финансовыми данными очень важно избегать потери данных или дубликатов при обработке. Мы можем использовать доставку Exactly-once, что означает, что все данные были получены один раз без дубликатов.
## Задержка системы
* Низкая задержка

Для потоковой системы обработки данных в реальном времени задержка должна быть минимальной (менее секунды). Это необходимо для принятия решений на основе самых актуальных данных с биржи Binance.
