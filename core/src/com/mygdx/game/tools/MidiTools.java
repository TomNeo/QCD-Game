package com.mygdx.game.tools;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import static javax.sound.midi.MidiSystem.getMidiDevice;

public class MidiTools {

    Sequencer seq; //= MidiSystem.getSequencer();
    Synthesizer synth; //= MidiSystem.getSynthesizer();
    Boolean notNull;
    int deviceNum = 0;

    public MidiTools() {
        try {
            Sequencer seq = MidiSystem.getSequencer();
            Synthesizer synth = MidiSystem.getSynthesizer();
            notNull = true;
        } catch (Exception e) {
            notNull = false;
        }
    }

    public void shortMessage(int messageType, int channel,int data1, int data2){
        try {
            ShortMessage myMsg = new ShortMessage();
            myMsg.setMessage(messageType, channel, data1, data2);
            long timeStamp = -1;
            MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
            Receiver receiver = MidiSystem.getReceiver();//= MidiSystem.getMidiDevice(info[deviceNum]);
            //device.open();
            //Receiver rcvr = device.getReceiver();//MidiSystem.getReceiver();//
            receiver.send(myMsg, timeStamp);
            //device.close();
        }catch(Exception ex){
            String breakpoint = ex.getMessage();
        }
        deviceNum++;
        if(deviceNum == 4){
            deviceNum = 0;
        }
    }
}
