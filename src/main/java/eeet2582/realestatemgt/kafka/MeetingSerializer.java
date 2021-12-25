package eeet2582.realestatemgt.kafka;

import eeet2582.realestatemgt.model.Meeting;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MeetingSerializer implements Serializer<Meeting> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, Meeting meeting) {
        int sizeOfNote;
        int sizeOfDate;
        int sizeOfTime;
        int sizeOfUser;
        int sizeOfHouse;
        byte[] serializedNote;
        byte[] serializedDate;
        byte[] serializedTime;
        byte[] serializedUser;
        byte[] serializedHouse;
        try {
            if (meeting == null)
                return null;
            DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");

            String encoding = "UTF8";
            serializedDate = meeting.getDate().format(date).getBytes(encoding);
            serializedTime = meeting.getTime().format(time).getBytes(encoding);
            serializedNote = meeting.getNote().getBytes(encoding);
            serializedUser = meeting.getUserHouse().getUserId().toString().getBytes(encoding);
            serializedHouse = meeting.getUserHouse().getHouseId().toString().getBytes(encoding);

            sizeOfDate = serializedDate.length;
            sizeOfTime = serializedTime.length;
            sizeOfUser = serializedUser.length;
            sizeOfHouse = serializedHouse.length;
            sizeOfNote = serializedNote.length;

            ByteBuffer buffer = ByteBuffer.allocate(sizeOfDate + sizeOfTime + sizeOfUser + sizeOfHouse + sizeOfNote + 20);

            buffer.putInt(sizeOfDate);
            buffer.put(serializedDate);

            buffer.putInt(sizeOfTime);
            buffer.put(serializedTime);

            buffer.putInt(sizeOfNote);
            buffer.put(serializedNote);

            buffer.putInt(sizeOfUser);
            buffer.put(serializedUser);

            buffer.putInt(sizeOfHouse);
            buffer.put(serializedHouse);
            return buffer.array();

        } catch (Exception e) {
            throw new SerializationException(e.getMessage());
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Meeting data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
