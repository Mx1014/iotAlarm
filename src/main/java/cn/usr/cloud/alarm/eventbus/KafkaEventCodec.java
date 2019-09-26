package cn.usr.cloud.alarm.eventbus;

import cn.usr.cloud.alarm.eventbus.event.KafkaEvent;
import cn.usr.cloud.alarm.util.ProtoStuffSerializerUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/20 上午9:32
 * @Describe:
 */
public class KafkaEventCodec implements MessageCodec<KafkaEvent, KafkaEvent>{

    @Override
    public void encodeToWire(Buffer buffer, KafkaEvent kafkaEvent) {
        byte[] bytes = ProtoStuffSerializerUtil.serialize(kafkaEvent);
        //buffer.setBytes(0,bytes);
        int length = bytes.length;

        buffer.appendInt(length);
        buffer.appendBytes(bytes);
    }

    @Override
    public KafkaEvent decodeFromWire(int _pos, Buffer buffer) {
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        byte[] bytes = buffer.getBytes(_pos+=4, _pos+=length);
        return ProtoStuffSerializerUtil.deserialize(bytes, KafkaEvent.class);
    }

    @Override
    public KafkaEvent transform(KafkaEvent kafkaEvent) {
        return kafkaEvent;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
