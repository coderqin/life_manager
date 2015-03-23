package youth.freecoder.graduationthesis.listener;

import android.content.Context;
import android.content.Intent;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import youth.freecoder.graduationthesis.entity.MyMessage;

/**
 * Created by freecoder on 15/3/11.
 */
public class MessageReceiver implements PacketListener {
    private Context context;

    public MessageReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void processPacket(Packet packet) {
        Message message = (Message) packet;
        if (message.getBody() != null) {
            //封装在线的信息
            MyMessage myMessage = new MyMessage(
                    message.getFrom(),
                    message.getTo(),
                    1,
                    message.getBody(),
                    String.valueOf(message.getProperty("SendTime")),
                    1
            );
            //广播出去
            Intent intent = new Intent();
            intent.setAction("youth.free.coder.online.message");
            intent.putExtra("online_message", myMessage);
            context.sendBroadcast(intent);
        }
    }
}
