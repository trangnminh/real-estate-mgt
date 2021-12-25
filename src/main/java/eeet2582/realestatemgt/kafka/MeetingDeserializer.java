package eeet2582.realestatemgt.kafka;

import eeet2582.realestatemgt.helper.UserHouse;
import eeet2582.realestatemgt.model.Meeting;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.DeserializationException;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MeetingDeserializer implements Deserializer<Meeting> {

    private static final Logger logger = LoggerFactory.getLogger(MeetingDeserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //Nothing to configure
    }

    @Override
    public Meeting deserialize(String s, byte[] data) {
        try {
            if (data == null) {
                System.out.println("Null received at deserialize");
                return null;
            }
            ByteBuffer buffer = ByteBuffer.wrap(data);
            String encoding = "UTF8";

            int sizeOfDate = buffer.getInt();
            byte[] dateBytes = new byte[sizeOfDate];
            buffer.get(dateBytes);
            String deserializedDate = new String(dateBytes, encoding);

            int sizeOfTime = buffer.getInt();
            byte[] timeBytes = new byte[sizeOfTime];
            buffer.get(timeBytes);
            String deserializedTime = new String(timeBytes, encoding);

            int sizeOfNote = buffer.getInt();
            byte[] noteBytes = new byte[sizeOfNote];
            buffer.get(noteBytes);
            String deserializedNote = new String(noteBytes, encoding);

            int sizeOfUser = buffer.getInt();
            byte[] userBytes = new byte[sizeOfUser];
            buffer.get(userBytes);
            String deserializedUser = new String(userBytes, encoding);
            Long userId = Long.parseLong(deserializedUser);

            int sizeOfHouse = buffer.getInt();
            byte[] houseBytes = new byte[sizeOfHouse];
            buffer.get(houseBytes);
            String deserializedHouse = new String(houseBytes, encoding);
            Long houseId = Long.parseLong(deserializedHouse);

            DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");

            return new Meeting(new UserHouse(userId, houseId),
                    LocalDate.parse(deserializedDate, date),
                    LocalTime.parse(deserializedTime, time),
                    deserializedNote);

        } catch (Exception e) {
            throw new DeserializationException(s, data, true, new KafkaException());
        }
    }

    @Override
    public void close() {
        // nothing to do
    }
}
