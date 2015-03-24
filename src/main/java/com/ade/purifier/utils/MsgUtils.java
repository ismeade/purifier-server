package com.ade.purifier.utils;

/**
 *
 * Created by ismeade on 2014/9/11.
 */
public class MsgUtils {

    public static byte[] createAcpInstruction(byte msgType, byte... data) {
        int length = 0;
        if (data != null) {
            length = data.length;
        }
        byte[] instruction = new byte[length + 6];
        instruction[0] = (byte) 0x02;
        instruction[1] = (byte) (length + 1);
        instruction[2] = msgType;
        for (int i = 0; i < length; i++) {
            instruction[i + 3] = data[i];
        }
        // TODO
        instruction[instruction.length - 3] = (byte) 0x00;
        instruction[instruction.length - 2] = (byte) 0x00;
        instruction[instruction.length - 1] = (byte) 0x03;
        return instruction;
    }

    public static void main(String[] args) {
        byte[] b = createAcpInstruction((byte)0x35);
        System.out.println(ByteUtils.toStr(b));
    }

}
