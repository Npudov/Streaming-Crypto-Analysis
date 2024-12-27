package stream.analytica.lab4;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.analyticlabs.dataModel.CryptoAggregatedData;

import java.util.List;


@Controller
@RequestMapping("/")
public class WebSocketController {
    private final KafkaConsumerService kafkaConsumerService;
    //private final SimpMessagingTemplate messagingTemplate;
    private final HistoricalKafkaConsumerService historicalKafkaConsumerService;

    public WebSocketController(KafkaConsumerService kafkaConsumerService, HistoricalKafkaConsumerService historicalKafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
        this.historicalKafkaConsumerService = historicalKafkaConsumerService;

    }

    /**
     * Обработка новых данных, поступающих через Kafka.
     * Этот метод будет использоваться для отправки новых данных клиентам через WebSocket.
     */
    /*@MessageMapping("/crypto-updates")
    public void handleCryptoUpdates(CryptoAggregatedData message) {
        // Отправляем данные всем подписанным клиентам
        kafkaConsumerService.getMessagingTemplate().convertAndSend("/topic/updates", message);
    }*/

    @GetMapping("/chart")
    public String chart(Model model) {
        return "chart";
    }

    /**
     * Подписка на получение начальных данных.
     * Когда клиент подключается, он подписывается на /topic/initial-data.
     * Мы отсылаем ему все данные из кэша.
     */
    @SubscribeMapping("/initial-data")
    public void sendInitialData() {
        List<CryptoAggregatedData> cachedData = historicalKafkaConsumerService.getDataCache();
        // Отправляем данные на клиент
        kafkaConsumerService.getMessagingTemplate().convertAndSend("/topic/initial-data", cachedData);
    }
}
